package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.helper.WorldHelper;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBlastFurnaceAirPump extends TileMultiBlockImouto implements ITickableTileEntity {

    private int tick;

    public TileBlastFurnaceAirPump() {
        super(ModTiles.BLAST_FURNACE_AIR_PUMP.get());
    }

    @Override
    public void tick() {
        if (isClientWorld()) return;
        if (hasValidOniichan() && tick % 10 == 0) {
            WorldHelper.getTile(TileBlastFurnaceAirVent.class, getWorld(), getOniichan()).ifPresent(tileFrom -> {
                tileFrom.getMultiBlockCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(handlerFrom -> {
                    WorldHelper.getCapability(getWorld(), getPos().offset(getFacingLazy()), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getFacingLazy().getOpposite()).ifPresent(handlerTo -> {
                        int filled = handlerTo.fill(handlerFrom.drain(1000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE);
                        if (filled > 0) {
                            FluidStack drained = handlerFrom.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                            handlerTo.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                        }
                    });
                });
            });
        }

        tick = (tick + 1) % 20;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasValidOniichan()
                ? WorldHelper.getTile(TileBlastFurnaceAirVent.class, getWorld(), getOniichan()).map(tile -> tile.getMultiBlockCapability(cap, side)).orElse(LazyOptional.empty())
                : super.getCapability(cap, side);
    }

    public Direction getFacingLazy() {
        return WorldHelper.getStateValue(getWorld(), getPos(), HorizontalBlock.HORIZONTAL_FACING).orElse(Direction.NORTH);
    }
}
