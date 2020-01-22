package arekkuusu.implom.common.handler.data.capability;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.ListIterator;

public class FluidMultipleTank implements IFluidTank, IFluidHandler, INBTSerializable<NBTTagCompound> {

	public List<FluidStack> liquids = Lists.newArrayList(); // currently contained liquids in the tank
	public IMultipleTankHandler handler;
	public FluidDefaultTank internal;
	public int maxCapacity;

	public FluidMultipleTank(IMultipleTankHandler handler, FluidDefaultTank internal) {
		this.handler = handler;
		this.internal = internal;
	}

	public FluidMultipleTank(IMultipleTankHandler handler) {
		this.handler = handler;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if(StringUtils.isNullOrEmpty(FluidRegistry.getFluidName(resource.getFluid()))) {
			return 0;
		}

		int used = getFluidAmount();
		int usable = Math.min(getCapacity() - used, resource.amount);
		if(usable <= 0) {
			return 0;
		}
		if(!doFill) {
			return usable;
		}
		//Internal Fill
		if(internal != null && internal.canFillFluidType(resource)) {
			FluidStack stack = new FluidStack(resource, usable);
			/*if(internal.getFluid() == null) {
				internal.setFluid(stack);
			}
			else {
				internal.fill(stack, true);
				internal.getFluid().amount += usable;
			}
			handler.onTankChange(liquids, liquids, internal.getFluid());*/
			internal.fillInternal(stack, doFill);
			return usable;
		}
		//Main Fill
		for(FluidStack liquid : liquids) {
			if(liquid.isFluidEqual(resource)) {
				liquid.amount += usable;
				handler.onTankChange(liquids, liquids, liquid);
				return usable;
			}
		}

		List<FluidStack> oldList = Lists.newLinkedList(liquids);
		resource = resource.copy();
		resource.amount = usable;
		liquids.add(resource);
		handler.onTankChange(oldList, liquids, resource);
		return usable;
	}

	@Override
	@Nullable
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if(liquids.isEmpty() && internal.getFluid() == null) {
			return null;
		}

		//noinspection ConstantConditions
		FluidStack liquid = new FluidStack(getFluid(), maxDrain);
		return drain(liquid, doDrain);
	}

	@Override
	@Nullable
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		//Internal Drain
		if(internal != null && internal.canDrainFluidType(resource)) {
			assert internal.getFluid() != null;

			int drainable = Math.min(resource.amount, internal.getFluid().amount);
			if(doDrain) {
				/*internal.getFluid().amount -= drainable;
				handler.onTankChange(liquids, liquids, internal.getFluid());*/
				internal.drainInternal(drainable, doDrain);
			}

			resource = resource.copy();
			resource.amount = drainable;
			return resource;
		}
		//Main Drain
		List<FluidStack> oldList = Lists.newLinkedList(liquids);
		ListIterator<FluidStack> iter = liquids.listIterator();
		while(iter.hasNext()) {
			FluidStack liquid = iter.next();
			if(liquid.isFluidEqual(resource)) {
				int drainable = Math.min(resource.amount, liquid.amount);
				if(doDrain) {
					liquid.amount -= drainable;
					if(liquid.amount <= 0) {
						iter.remove();
					}
					handler.onTankChange(oldList, liquids, liquid);
				}

				resource = resource.copy();
				resource.amount = drainable;
				return resource;
			}
		}

		return null;
	}

	@Nullable
	public FluidStack drainInternal(FluidStack resource, boolean doDrain) {
		//Internal Drain
		if(internal != null && internal.getFluid() != null && internal.getFluid().isFluidEqual(resource)) {
			int drainable = Math.min(resource.amount, internal.getFluid().amount);
			if(doDrain) {
				internal.drainInternal(resource, doDrain);
				/*internal.getFluid().amount -= drainable;
				handler.onTankChange(liquids, liquids, internal.getFluid());*/
			}

			resource = resource.copy();
			resource.amount = drainable;
			return resource;
		}

		return null;
	}

	@Nullable
	@Override
	public FluidStack getFluid() {
		return liquids.size() > 0 ? liquids.get(0) : internal != null ? internal.getFluid() : null;
	}

	@Override
	public int getFluidAmount() {
		int cap = 0;
		for(FluidStack liquid : liquids) {
			cap += liquid.amount;
		}
		if(internal != null) {
			cap += internal.getFluidAmount();
		}

		return cap;
	}

	public void setFluids(List<FluidStack> fluids) {
		this.liquids = fluids;
	}

	public List<FluidStack> getFluids() {
		List<FluidStack> liquids = Lists.newArrayList(this.liquids);
		if(internal != null)
			liquids.add(internal.getFluid());
		return liquids;
	}

	public void setCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	@Override
	public int getCapacity() {
		return maxCapacity + (internal != null ? internal.getCapacity() : 0);
	}

	@Override
	public FluidTankInfo getInfo() {
		FluidStack fluid = getFluid();
		int capacity = getCapacity() - getFluidAmount();
		if(fluid != null) {
			capacity += fluid.amount;
		}
		return new FluidTankInfo(fluid, capacity);
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		int liquidsContained = liquids.size();
		if(internal != null) {
			liquidsContained += 1;
		}

		if(liquidsContained == 0) {
			return new IFluidTankProperties[]{new FluidTankProperties(null, getCapacity(), true, true)};
		}

		IFluidTankProperties[] properties = new IFluidTankProperties[liquidsContained];
		for(int i = 0; i < liquidsContained; i++) {
			FluidStack stack = (i == 0 && internal != null)
					? internal.getFluid()
					: liquids.get(internal != null ? i - 1 : i);
			assert stack != null;
			int capacity = getCapacity() - getFluidAmount();
			properties[i] = new FluidMultipleTankProperties(stack, capacity, true, true);
		}

		return properties;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList taglist = new NBTTagList();

		for(FluidStack liquid : liquids) {
			NBTTagCompound fluidTag = new NBTTagCompound();
			liquid.writeToNBT(fluidTag);
			taglist.appendTag(fluidTag);
		}

		nbt.setTag("liquids", taglist);
		nbt.setInteger("capacity", maxCapacity);

		if(internal != null) {
			nbt.setTag("internal", internal.serializeNBT());
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		NBTTagList taglist = nbt.getTagList("liquids", 10);

		liquids.clear();
		for(int i = 0; i < taglist.tagCount(); i++) {
			NBTTagCompound fluidTag = taglist.getCompoundTagAt(i);
			FluidStack liquid = FluidStack.loadFluidStackFromNBT(fluidTag);
			if(liquid != null) {
				liquids.add(liquid);
			}
		}

		maxCapacity = nbt.getInteger("capacity");

		if(internal != null) {
			internal.deserializeNBT(nbt.getCompoundTag("internal"));
		}
	}

	public interface IMultipleTankHandler {
		void onTankChange(List<FluidStack> old, List<FluidStack> updated, FluidStack changed);
	}

	public static class FluidMultipleTankProperties extends FluidTankProperties {

		public FluidMultipleTankProperties(@Nullable FluidStack contents, int capacity, boolean canFill, boolean canDrain) {
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
