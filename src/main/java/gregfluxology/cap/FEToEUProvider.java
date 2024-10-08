// Copyright (C) 2018 DBot

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
// of the Software, and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all copies
// or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
// INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
// PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
// FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
// DEALINGS IN THE SOFTWARE.

package gregfluxology.cap;

import gregfluxology.GFyConfig;
import gregfluxology.util.GFyUtility;
import gregtech.api.capability.FeCompat;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.CapabilityCompatProvider;
import gregtech.common.pipelike.cable.net.EnergyNetHandler;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class FEToEUProvider extends CapabilityCompatProvider {

    public FEToEUProvider(ICapabilityProvider upValue) {
        super(upValue);
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY &&
                hasUpvalueCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
        if (capability != CapabilityEnergy.ENERGY) {
            return null;
        }

        IEnergyContainer energyContainer = getUpvalueCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, facing);
        return energyContainer == null ? null : CapabilityEnergy.ENERGY.cast(new FEEnergyWrapper(energyContainer, facing));
    }

    public class FEEnergyWrapper implements IEnergyStorage {

        private final IEnergyContainer energyContainer;
        private final EnumFacing facing;

        public FEEnergyWrapper(IEnergyContainer energyContainer, EnumFacing facing) {
            this.energyContainer = energyContainer;
            this.facing = facing;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) { // TODO: add a config here to remove voltage restrictions.

            if (!canReceive()) return 0;

            if (maxReceive == 1 && simulate) {
                // Damn you mekanism
                return energyContainer.getEnergyCanBeInserted() > 0L ? 1 : 0;
            }

            long maxIn = maxReceive / FeCompat.ratio(true);
            long missing = energyContainer.getEnergyCanBeInserted();
            long voltage = energyContainer.getInputVoltage();
            maxIn = Math.min(missing, maxIn);
            long maxAmp = Math.min(energyContainer.getInputAmperage(), maxIn / voltage);

            if (GFyConfig.ignoreCableCapacity && energyContainer instanceof EnergyNetHandler) { // TODO: add a config here to remove this check, thus protecting the cables from burning.
                maxIn = maxReceive / FeCompat.ratio(true);
                maxAmp = maxIn / voltage;
            }

            if (maxAmp < 1L) return 0;

            if (!simulate) {
                maxAmp = energyContainer.acceptEnergyFromNetwork(facing, voltage, maxAmp);
            }

            return GFyUtility.safeConvertEUToFE(maxAmp * voltage);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0; // this is done in base CEu
        }

        @Override
        public int getEnergyStored() {
            return GFyUtility.safeConvertEUToFE(energyContainer.getEnergyStored());
        }

        @Override
        public int getMaxEnergyStored() {
            return GFyUtility.safeConvertEUToFE(energyContainer.getEnergyCapacity());
        }

        @Override
        public boolean canExtract() {
            return false; // this is done in base CEu
        }

        @Override
        public boolean canReceive() {
            return energyContainer.inputsEnergy(this.facing);
        }
    }
}
