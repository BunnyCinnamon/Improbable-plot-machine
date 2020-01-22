package arekkuusu.implom.common.handler.data.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class FluidDefaultTank implements IFluidTank, IFluidHandler, INBTSerializable<NBTTagCompound> {

	public FluidStack fluid;
	public ITankHandler handler;
	public int maxCapacity;

	public FluidDefaultTank(int maxCapacity, ITankHandler handler) {
		this.maxCapacity = maxCapacity;
		this.handler = handler;
	}

	public boolean isFluidType(Fluid fluid) {
		return true;
	}

	public boolean canDrainFluidType(FluidStack fluid) {
		return (this.fluid != null && this.fluid.isFluidEqual(fluid)) && isFluidType(fluid.getFluid());
	}

	public boolean canFillFluidType(FluidStack fluid) {
		return (this.fluid == null || this.fluid.isFluidEqual(fluid)) && isFluidType(fluid.getFluid());
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if(StringUtils.isNullOrEmpty(FluidRegistry.getFluidName(resource.getFluid()))) {
			return 0;
		}
		if(!canFillFluidType(resource)) {
			return 0;
		}

		return fillInternal(resource, doFill);
	}

	public int fillInternal(FluidStack resource, boolean doFill) {
		if(!doFill) {
			if(fluid == null) {
				return Math.min(maxCapacity, resource.amount);
			}

			if(!fluid.isFluidEqual(resource)) {
				return 0;
			}

			return Math.min(maxCapacity - fluid.amount, resource.amount);
		}

		if(fluid == null) {
			fluid = new FluidStack(resource, Math.min(maxCapacity, resource.amount));
			handler.onTankChange(null, fluid);
			return fluid.amount;
		}

		if(!fluid.isFluidEqual(resource)) {
			return 0;
		}
		FluidStack old = fluid.copy();
		int filled = maxCapacity - fluid.amount;

		if(resource.amount < filled) {
			fluid.amount += resource.amount;
			filled = resource.amount;
		}
		else {
			fluid.amount = maxCapacity;
		}
		handler.onTankChange(old, fluid);

		return filled;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if(StringUtils.isNullOrEmpty(FluidRegistry.getFluidName(resource.getFluid()))) {
			return null;
		}
		if(!canDrainFluidType(resource)) {
			return null;
		}
		return drainInternal(resource, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if(!canDrainFluidType(fluid)) {
			return null;
		}
		return drainInternal(maxDrain, doDrain);
	}

	@Nullable
	public FluidStack drainInternal(FluidStack resource, boolean doDrain) {
		if(!resource.isFluidEqual(getFluid())) {
			return null;
		}
		return drainInternal(resource.amount, doDrain);
	}

	@Nullable
	public FluidStack drainInternal(int maxDrain, boolean doDrain) {
		if(fluid == null || maxDrain <= 0) {
			return null;
		}

		int drained = maxDrain;
		if(fluid.amount < drained) {
			drained = fluid.amount;
		}

		FluidStack stack = new FluidStack(fluid, drained);
		if(doDrain) {
			FluidStack old = fluid.copy();
			fluid.amount -= drained;
			if(fluid.amount <= 0) {
				fluid = null;
			}
			handler.onTankChange(old, fluid);
		}
		return stack;
	}

	@Nullable
	@Override
	public FluidStack getFluid() {
		return fluid;
	}

	@Override
	public int getFluidAmount() {
		int cap = 0;
		if(this.fluid != null) {
			cap += fluid.amount;
		}
		return cap;
	}

	public void setCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	@Override
	public int getCapacity() {
		return maxCapacity;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(getFluid(), getCapacity());
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		IFluidTankProperties[] tankProperties = new IFluidTankProperties[1];
		tankProperties[0] = new OverrideTankProperties(this.fluid, getCapacity(), true, true);
		return tankProperties;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("capacity", maxCapacity);

		if(this.fluid != null) {
			this.fluid.writeToNBT(nbt);
		}
		else {
			nbt.setString("Empty", "");
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(!nbt.hasKey("Empty")) {
			this.fluid = FluidStack.loadFluidStackFromNBT(nbt);
		}
		else {
			this.fluid = null;
		}

		maxCapacity = nbt.getInteger("capacity");
	}

	public interface ITankHandler {
		void onTankChange(@Nullable FluidStack old, @Nullable FluidStack updated);
	}

	public static class OverrideTankProperties extends FluidTankProperties {

		public OverrideTankProperties(@Nullable FluidStack contents, int capacity, boolean canFill, boolean canDrain) {
			super(contents, capacity, canFill, canDrain);
		}

		@Override
		public boolean canFillFluidType(FluidStack fluidStack) {
			return getContents() == null || getContents().isFluidEqual(fluidStack);
		}

		@Override
		public boolean canDrainFluidType(FluidStack fluidStack) {
			return getContents() == null || getContents().isFluidEqual(fluidStack);
		}
	}
}
