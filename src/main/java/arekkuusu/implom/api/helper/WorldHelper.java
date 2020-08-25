package arekkuusu.implom.api.helper;

import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.Optional;

public final class WorldHelper {

    public static <T extends Comparable<T>> Optional<T> getStateValue(IBlockReader world, BlockPos pos, Property<T> property) {
        BlockState state = world.getBlockState(pos);
        return state.getProperties().contains(property) ? Optional.of(state.get(property)) : Optional.empty();
    }

    public static <T> LazyOptional<T> getCapability(IBlockReader world, BlockPos pos, Capability<T> capability, @Nullable Direction facing) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null) {
            return tile.getCapability(capability, facing);
        }
        return LazyOptional.empty();
    }

    @SuppressWarnings("unchecked")
    public static <T extends TileEntity> Optional<T> getTile(Class<T> clazz, IBlockReader world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return clazz.isInstance(tile) ? Optional.of((T) tile) : Optional.empty();
    }
}
