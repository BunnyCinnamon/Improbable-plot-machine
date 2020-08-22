package arekkuusu.implom.common.handler.data.capability;

import arekkuusu.implom.common.block.fluid.ModFluids;
import arekkuusu.implom.common.block.tile.TileBlastFurnaceController;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class BlastFurnaceAirTank implements IFluidHandler, INBTSerializable<CompoundNBT> {

    public FluidStack fluid = FluidStack.EMPTY;
    public TileBlastFurnaceController handler;
    public int maxCapacity;

    public BlastFurnaceAirTank(TileBlastFurnaceController handler) {
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

    @Override
    public int getTankCapacity(int tank) {
        return 1000;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return ModFluids.HOT_AIR.get() == stack.getFluid();
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (action.simulate()) {
            if (fluid == null) {
                return Math.min(maxCapacity, resource.getAmount());
            }

            if (!fluid.isFluidEqual(resource)) {
                return 0;
            }

            return Math.min(maxCapacity - fluid.getAmount(), resource.getAmount());
        }

        if (fluid == FluidStack.EMPTY) {
            fluid = new FluidStack(resource, Math.min(maxCapacity, resource.getAmount()));
            handler.airTankChange();
            return fluid.getAmount();
        }

        if (!fluid.isFluidEqual(resource)) {
            return 0;
        }
        int filled = maxCapacity - fluid.getAmount();

        if (resource.getAmount() < filled) {
            fluid.setAmount(fluid.getAmount() + resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(maxCapacity);
        }
        handler.airTankChange();

        return filled;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (fluid == null) {
            return FluidStack.EMPTY;
        }

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
            handler.airTankChange();
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
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        fluid = FluidStack.loadFluidStackFromNBT(nbt);
    }
}
