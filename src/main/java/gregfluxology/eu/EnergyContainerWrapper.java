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

import gregfluxology.Gregfluxology;
import gregtech.api.capability.FeCompat;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.EUToFEProvider;
import gregtech.api.pipenet.tile.IPipeTile;
import gregtech.common.metatileentities.converter.ConverterTrait;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyContainerWrapper implements IEnergyStorage {

    private final IEnergyContainer container;
    private EnumFacing facing = null;

    public EnergyContainerWrapper(IEnergyContainer container, EnumFacing facing) {
        this.container = container;
        this.facing = facing;
    }

    boolean isValid() {
        return container != null && !(container instanceof EUToFEProvider) && !(container instanceof ConverterTrait.EUContainer);
    }

    private int maxSpeedIn() {
        long result = container.getInputAmperage() * container.getInputVoltage() * FeCompat.ratio(false);

        return Gregfluxology.safeCastLongToInt(result);
    }

    private int maxSpeedOut() {
        long result = container.getOutputAmperage() * container.getOutputVoltage() * FeCompat.ratio(false);

        return Gregfluxology.safeCastLongToInt(result);
    }

    private int voltageIn() {
        long result = container.getInputVoltage() * FeCompat.ratio(false);

        return Gregfluxology.safeCastLongToInt(result);
    }

    private int voltageOut() {
        long result = container.getOutputVoltage() * FeCompat.ratio(false);

        return Gregfluxology.safeCastLongToInt(result);
    }

    // eNet in gregtech is private
    // im unable to workaround cable burning.
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }

        if (maxReceive == 1 && simulate) {
            // assuming we hit mekanism
            return container.getEnergyCanBeInserted() > 0L ? 1 : 0;
        }

        int speed = maxSpeedIn();

        if (maxReceive > speed) {
            maxReceive = speed;
        }

        int voltageIn = voltageIn();

        maxReceive -= maxReceive % FeCompat.ratio(false);
        maxReceive -= maxReceive % voltageIn;

        if (maxReceive <= 0 || maxReceive < voltageIn) {
            return 0;
        }

        long missing = container.getEnergyCanBeInserted();

        if (missing <= 0L || missing < voltageIn) {
            return 0;
        }

        if (missing >= Gregfluxology.MAX_VALUE_AS_LONG) {
            missing = Gregfluxology.MAX_VALUE_AS_LONG;
        }

        missing *= FeCompat.ratio(false);

        if (missing < maxReceive) {
            maxReceive = (int) missing;
        }

        if (!simulate) {
            long voltage = container.getInputVoltage();
            int ampers = (int) container.acceptEnergyFromNetwork(this.facing, voltage, maxReceive / (FeCompat.ratio(false) * voltage));
            return ampers * voltageIn;
        }

        return maxReceive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) {
            return 0;
        }

        int speed = maxSpeedOut();

        if (maxExtract > speed) {
            maxExtract = speed;
        }

        maxExtract -= maxExtract % FeCompat.ratio(false);
        maxExtract -= maxExtract % voltageOut();

        if (maxExtract <= 0) {
            return 0;
        }

        long stored = container.getEnergyStored();

        if (stored <= 0L) {
            return 0;
        }

        if (stored >= Gregfluxology.MAX_VALUE_AS_LONG) {
            stored = Gregfluxology.MAX_VALUE_AS_LONG;
        }

        stored *= FeCompat.ratio(false);

        if (stored < maxExtract) {
            maxExtract = (int) stored;
        }

        if (!simulate) {
            return (int) (container.removeEnergy(maxExtract / FeCompat.ratio(false)) * FeCompat.ratio(false));
        }

        return maxExtract;
    }

    @Override
    public int getEnergyStored() {
        long value = container.getEnergyStored();

        if (value >= Gregfluxology.MAX_VALUE_AS_LONG || value > Gregfluxology.OVERFLOW_CHECK) {
            return Integer.MAX_VALUE;
        }

        return (int) (value * FeCompat.ratio(false));
    }

    @Override
    public int getMaxEnergyStored() {
        long value = container.getEnergyCapacity();

        if (value >= Gregfluxology.MAX_VALUE_AS_LONG || value > Gregfluxology.OVERFLOW_CHECK) {
            return Integer.MAX_VALUE;
        }

        return (int) (value * FeCompat.ratio(false));
    }

    @Override
    public boolean canExtract() {
        if (container instanceof IPipeTile<?, ?>) {
            return false;
        }

        return container.outputsEnergy(this.facing);
    }

    @Override
    public boolean canReceive() {
        return container.inputsEnergy(this.facing);
    }
}
