package arekkuusu.implom.common.handler.data.capability;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MultipleTank implements IFluidHandler, INBTSerializable<CompoundNBT> {

    public List<FluidStack> fluids = new LinkedList<>();
    public TriConsumer<List<FluidStack>, List<FluidStack>, FluidStack> handler;
    public int maxCapacity;

    public MultipleTank(TriConsumer<List<FluidStack>, List<FluidStack>, FluidStack> handler) {
        this.handler = handler;
    }

    public int getCurrent() {
        return 0;
    }

    @Override
    public int getTanks() {
        return fluids.size();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return !fluids.isEmpty() ? fluids.get(getCurrent()) : FluidStack.EMPTY;
    }

    public int getFluidAmount() {
        int cap = 0;
        for (FluidStack liquid : fluids) {
            cap += liquid.getAmount();
        }

        return cap;
    }

    public void setTankCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public int getTankCapacity(int tank) {
        return (!fluids.isEmpty() ? fluids.get(getCurrent()).getAmount() : 0) + (maxCapacity - getFluidAmount());
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return !fluids.isEmpty() && fluids.get(getCurrent()).isFluidEqual(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int used = getFluidAmount();
        int usable = Math.min(getTankCapacity(0) - used, resource.getAmount());
        if (usable <= 0) {
            return 0;
        }
        if (action.simulate()) {
            return usable;
        }
        //Main Fill
        for (FluidStack liquid : fluids) {
            if (liquid.isFluidEqual(resource)) {
                liquid.setAmount(liquid.getAmount() + usable);
                handler.accept(fluids, fluids, liquid);
                return usable;
            }
        }

        List<FluidStack> oldList = Lists.newLinkedList(fluids);
        resource = resource.copy();
        resource.setAmount(usable);
        fluids.add(resource);
        handler.accept(oldList, fluids, resource);
        return usable;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        List<FluidStack> oldList = Lists.newLinkedList(fluids);
        ListIterator<FluidStack> iterator = fluids.listIterator();
        while (iterator.hasNext()) {
            FluidStack liquid = iterator.next();
            if (liquid.isFluidEqual(resource)) {
                int drainable = Math.min(resource.getAmount(), liquid.getAmount());
                if (action.execute()) {
                    liquid.setAmount(liquid.getAmount() - drainable);
                    if (liquid.getAmount() <= 0) {
                        iterator.remove();
                    }
                    handler.accept(oldList, fluids, liquid);
                }

                resource = resource.copy();
                resource.setAmount(drainable);
                return resource;
            }
        }

        return FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return drain(new FluidStack(fluids.get(getCurrent()), maxDrain), action);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (FluidStack fluid : fluids) {
            CompoundNBT fluidTag = new CompoundNBT();
            fluid.writeToNBT(fluidTag);
            list.add(fluidTag);
        }
        nbt.put("list", list);
        nbt.putInt("maxCapacity", maxCapacity);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT list = nbt.getList("list", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT fluidTag = list.getCompound(i);
            FluidStack liquid = FluidStack.loadFluidStackFromNBT(fluidTag);
            if (liquid != null) {
                fluids.add(liquid);
            }
        }
        maxCapacity = nbt.getInt("maxCapacity");
    }
}
