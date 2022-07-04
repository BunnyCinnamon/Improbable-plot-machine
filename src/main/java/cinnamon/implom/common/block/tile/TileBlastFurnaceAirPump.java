package cinnamon.implom.common.block.tile;

import cinnamon.implom.api.helper.WorldHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBlastFurnaceAirPump extends TileMultiBlockImouto {

    private int tick;

    public TileBlastFurnaceAirPump(BlockPos arg2, BlockState arg3) {
        super(ModTiles.BLAST_FURNACE_AIR_PUMP.get(), arg2, arg3);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasValidOniichan()
                ? WorldHelper.getTile(TileBlastFurnaceAirVent.class, getLevel(), getOniichan()).map(tile -> tile.getMultiBlockCapability(cap, side)).orElse(LazyOptional.empty())
                : super.getCapability(cap, side);
    }

    public Direction getFacingLazy() {
        return WorldHelper.getStateValue(getLevel(), getBlockPos(), HorizontalDirectionalBlock.FACING).orElse(Direction.NORTH);
    }

    public static class Ticking implements BlockEntityTicker<TileBlastFurnaceAirPump> {

        @Override
        public void tick(Level arg, BlockPos arg2, BlockState arg3, TileBlastFurnaceAirPump instance) {
            if (instance.isClientWorld()) return;
            if (instance.hasValidOniichan() && instance.tick % 10 == 0) {
                WorldHelper.getTile(TileBlastFurnaceAirVent.class, instance.getLevel(), instance.getOniichan()).ifPresent(tileFrom -> {
                    tileFrom.getMultiBlockCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(handlerFrom -> {
                        WorldHelper.getCapability(instance.getLevel(), instance.getBlockPos().relative(instance.getFacingLazy()), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, instance.getFacingLazy().getOpposite()).ifPresent(handlerTo -> {
                            int filled = handlerTo.fill(handlerFrom.drain(1000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE);
                            if (filled > 0) {
                                FluidStack drained = handlerFrom.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                                handlerTo.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                            }
                        });
                    });
                });
            }

            instance.tick = (instance.tick + 1) % 20;
        }
    }
}
