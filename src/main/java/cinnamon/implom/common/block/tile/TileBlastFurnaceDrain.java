package cinnamon.implom.common.block.tile;

import cinnamon.implom.api.helper.WorldHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBlastFurnaceDrain extends TileMultiBlockImouto {

    public TileBlastFurnaceDrain(BlockPos arg2, BlockState arg3) {
        super(ModTiles.BLAST_FURNACE_DRAIN.get(), arg2, arg3);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasValidOniichan()
                ? WorldHelper.getTile(TileBlastFurnaceController.class, getLevel(), getOniichan()).map(tile -> tile.getMultiBlockCapability(cap, Direction.DOWN)).orElse(LazyOptional.empty())
                : super.getCapability(cap, side);
    }
}
