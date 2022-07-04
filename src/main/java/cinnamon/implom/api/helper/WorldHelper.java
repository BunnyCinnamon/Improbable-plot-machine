package cinnamon.implom.api.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.Optional;

public final class WorldHelper {

    public static <T extends Comparable<T>> Optional<T> getStateValue(LevelAccessor world, BlockPos pos, Property<T> property) {
        BlockState state = world.getBlockState(pos);
        return state.getProperties().contains(property) ? Optional.of(state.getValue(property)) : Optional.empty();
    }

    public static <T> LazyOptional<T> getCapability(LevelAccessor world, BlockPos pos, Capability<T> capability, @Nullable Direction facing) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null) {
            return tile.getCapability(capability, facing);
        }
        return LazyOptional.empty();
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> Optional<T> getTile(Class<T> clazz, LevelReader world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        return clazz.isInstance(tile) ? Optional.of((T) tile) : Optional.empty();
    }
}
