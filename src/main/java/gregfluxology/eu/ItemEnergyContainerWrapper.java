//
//// Copyright (C) 2018 DBot
//
//// Permission is hereby granted, free of charge, to any person obtaining a copy
//// of this software and associated documentation files (the "Software"), to deal
//// in the Software without restriction, including without limitation the rights
//// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
//// of the Software, and to permit persons to whom the Software is furnished to do so,
//// subject to the following conditions:
//
//// The above copyright notice and this permission notice shall be included in all copies
//// or substantial portions of the Software.
//
//// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
//// INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
//// PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
//// FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
//// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
//// DEALINGS IN THE SOFTWARE.
//
//package gregfluxology.eu;
//import gregfluxology.Gregfluxology;
//
//import gregtech.api.GTValues;
//import gregtech.api.capability.FeCompat;
//import gregtech.api.capability.IElectricItem;
//import net.minecraftforge.energy.IEnergyStorage;
//
//public class ItemEnergyContainerWrapper implements IEnergyStorage {
//	private final IElectricItem container;
//
//	public ItemEnergyContainerWrapper(IElectricItem container) {
//		this.container = container;
//	}
//
//	boolean isValid() {
//		return container != null;
//	}
//
//	private long itemVoltage() {
//		return GTValues.V[container.getTier()];
//	}
//
//	private int getMaxSpeed() {
//		long result = itemVoltage() * FeCompat.ratio(false);
//
//		if (result > Integer.MAX_VALUE) {
//			return Integer.MAX_VALUE;
//		}
//
//		return (int) result;
//	}
//
//	@Override
//	public int receiveEnergy(int maxReceive, boolean simulate) {
//		/* if (!canReceive()) {
//			return 0;
//		}
//
//		if (container.getMaxCharge() <= 0) {
//			return 0;
//		} */
//
//		if (maxReceive == 1 && simulate) {
//			// assuming we hit mekanism or enderio charger
//			maxReceive = Integer.MAX_VALUE;
//		}
//
//		int speed = getMaxSpeed();
//
//		if (maxReceive > speed) {
//			maxReceive = speed;
//		}
//
//		maxReceive -= maxReceive % FeCompat.ratio(false);
//
//		if (maxReceive <= 0) {
//			return 0;
//		}
//
//		long simulated = container.charge(maxReceive / FeCompat.ratio(false), Integer.MAX_VALUE, false, true);
//
//		if (simulated < 0L) {
//		// if (simulated < 0L || simulated < itemVoltage()) {
//			return 0;
//		}
//
//		if (!simulate) {
//			container.charge(simulated, Integer.MAX_VALUE, false, false);
//		}
//
//		return (int) (simulated * FeCompat.ratio(false));
//	}
//
//	@Override
//	public int extractEnergy(int maxExtract, boolean simulate) {
//		/* if (!canExtract()) {
//			return 0;
//		}
//
//		if (container.getMaxCharge() <= 0) {
//			return 0;
//		} */
//
//		if (maxExtract == 1 && simulate) {
//			// assuming we hit mekanism
//			maxExtract = Integer.MAX_VALUE;
//		}
//
//		int speed = getMaxSpeed();
//
//		if (maxExtract > speed) {
//			maxExtract = speed;
//		}
//
//		maxExtract -= maxExtract % FeCompat.ratio(false);
//
//		if (maxExtract <= 0) {
//			return 0;
//		}
//
//		long simulated = container.discharge(maxExtract / FeCompat.ratio(false), Integer.MAX_VALUE, false, true, true);
//
//		if (simulated < 0L) {
//		// if (simulated < 0L || simulated < itemVoltage()) {
//			return 0;
//		}
//
//		if (!simulate) {
//			container.discharge(simulated, Integer.MAX_VALUE, false, true, false);
//		}
//
//		return (int) (simulated * FeCompat.ratio(false));
//	}
//
//	// IElectricItem has no interface for getting current charge
//	// using workaround
//	@Override
//	public int getEnergyStored() {
//		long value = container.getCharge();
//
//		if (value >= Gregfluxology.MAX_VALUE_AS_LONG || value > Gregfluxology.OVERFLOW_CHECK) {
//			return Integer.MAX_VALUE;
//		}
//
//		return (int) (value * FeCompat.ratio(false));
//	}
//
//	@Override
//	public int getMaxEnergyStored() {
//		long value = container.getMaxCharge();
//
//		if (value >= Gregfluxology.MAX_VALUE_AS_LONG || value > Gregfluxology.OVERFLOW_CHECK) {
//			return Integer.MAX_VALUE;
//		}
//
//		return (int) (value * FeCompat.ratio(false));
//	}
//
//	@Override
//	public boolean canExtract() {
//		return container.canProvideChargeExternally();
//	}
//
//	@Override
//	public boolean canReceive() {
//		return container.charge(container.getTransferLimit(), Integer.MAX_VALUE, true, true) != 0L;
//	}
//}
