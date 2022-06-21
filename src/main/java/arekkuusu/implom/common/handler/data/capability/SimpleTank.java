package arekkuusu.implom.common.handler.data.capability;

import arekkuusu.implom.common.block.fluid.ModFluids;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class SimpleTank implements IFluidHandler, INBTSerializable<CompoundNBT> {

    public FluidStack fluid = FluidStack.EMPTY;
    public Runnable handler;
    public int maxCapacity;

    public SimpleTank(Runnable handler) {
        this.handler = handler;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluid;
    }

    public void setTankCapacity(int capacity) {
        this.maxCapacity = capacity;
    }

    @Override
    public int getTankCapacity(int tank) {
        return maxCapacity;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return ModFluids.HOT_AIR.get() == stack.getFluid();
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (action.simulate()) {
            if (!isFluidValid(0, resource)) {
                return 0;
            }

            return Math.min(maxCapacity - fluid.getAmount(), resource.getAmount());
        }

        if (fluid.getRawFluid() == Fluids.EMPTY) {
            fluid = new FluidStack(resource, Math.min(maxCapacity, resource.getAmount()));
            handler.run();
            return fluid.getAmount();
        }

        if (!isFluidValid(0, resource)) {
            return 0;
        }
        int filled = maxCapacity - fluid.getAmount();

        if (resource.getAmount() < filled) {
            fluid.setAmount(fluid.getAmount() + resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(maxCapacity);
        }
        handler.run();

        return filled;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        int drained = resource.getAmount();
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (action.execute()) {
            fluid.setAmount(fluid.getAmount() - drained);
            if (fluid.getAmount() <= 0) {
                fluid = FluidStack.EMPTY;
            }
            handler.run();
        }
        return stack;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return drain(new FluidStack(fluid, maxDrain), action);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        fluid.writeToNBT(nbt);
        nbt.putInt("maxCapacity", maxCapacity);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        fluid = FluidStack.loadFluidStackFromNBT(nbt);
        maxCapacity = nbt.getInt("maxCapacity");
    }
}
