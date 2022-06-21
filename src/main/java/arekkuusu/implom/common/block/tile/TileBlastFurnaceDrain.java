package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.helper.WorldHelper;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBlastFurnaceDrain extends TileMultiBlockImouto {

    public TileBlastFurnaceDrain() {
        super(ModTiles.BLAST_FURNACE_DRAIN.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasValidOniichan()
                ? WorldHelper.getTile(TileBlastFurnaceController.class, getWorld(), getOniichan()).map(tile -> tile.getMultiBlockCapability(cap, Direction.DOWN)).orElse(LazyOptional.empty())
                : super.getCapability(cap, side);
    }
}
