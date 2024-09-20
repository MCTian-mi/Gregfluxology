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

package gregfluxology.eu;

import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.impl.CapabilityCompatProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;

public class FEToEUProvider extends CapabilityCompatProvider {

    private final EnergyContainerWrapper[] FEWrappers = new EnergyContainerWrapper[7];
    private boolean gettingValue = false;

    public FEToEUProvider(TileEntity tileEntity) {
        super(tileEntity);
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
        if (gettingValue) {
            return false;
        }

        if (capability != CapabilityEnergy.ENERGY) {
            return false;
        }

        int faceID = facing == null ? 6 : facing.getIndex();

        if (FEWrappers[faceID] == null) {
            FEWrappers[faceID] = new EnergyContainerWrapper(getUpvalueCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, facing), facing);
        }

        gettingValue = true;
        boolean result = FEWrappers[faceID].isValid();
        gettingValue = false;
        return result;
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
        if (gettingValue) {
            return null;
        }

        if (capability != CapabilityEnergy.ENERGY) {
            return null;
        }

        int faceID = facing == null ? 6 : facing.getIndex();

        if (FEWrappers[faceID] == null) {
            FEWrappers[faceID] = new EnergyContainerWrapper(getUpvalueCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, facing), facing);
        }

        gettingValue = true;

        if (FEWrappers[faceID].isValid()) {
            gettingValue = false;
            return CapabilityEnergy.ENERGY.cast(FEWrappers[faceID]);
        }

        gettingValue = false;

        return null;
    }
}
