package arekkuusu.implom.api.multiblock.layer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public abstract class MultiBlockLayer {

    public final LayerData layerData;

    public MultiBlockLayer(LayerData layerData) {
        this.layerData = layerData;
    }

    public boolean validate(World world, List<BlockPos> candidates) {
        for (RequirementData requirement : layerData.requirements) {
            int count = 0;
            for (BlockPos candidate : candidates) {
                if (requirement.isValid(layerData, world, candidate)) ++count;
            }
            if (count < requirement.min || count > requirement.max) {
                return false;
            }
        }
        return true;
    }

    public abstract boolean detect(World world, BlockPos center, int[] edges, List<BlockPos> candidates);

    public static class LayerData {
        public final Map<LayerPiece, BiFunction<World, BlockPos, Boolean>> info = Maps.newEnumMap(LayerPiece.class);
        public final List<RequirementData> requirements = Lists.newLinkedList();
        public final boolean hasFrame;
        public final int minHeight;
        public final int maxHeight;

        public LayerData(boolean hasFrame, int minHeight, int maxHeight) {
            info.put(LayerPiece.FRAME, LayerData.ofAny());
            info.put(LayerPiece.WALL, LayerData.ofAny());
            info.put(LayerPiece.INSIDE, LayerData.ofAny());
            this.hasFrame = hasFrame;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
        }

        public boolean isValidBlock(LayerPiece piece, World world, BlockPos pos) {
            return info.get(piece).apply(world, pos);
        }

        public LayerData add(LayerPiece piece, BiFunction<World, BlockPos, Boolean> function) {
            info.put(piece, function);
            return this;
        }

        public LayerData with(RegistryObject<Block> block, int min, int max, Facing facing) {
            requirements.add(new RequirementData(block, facing, min, max));
            return this;
        }

        //Use responsibly...
        public static BiFunction<World, BlockPos, Boolean> of(Object... blocks) {
            return (w, p) -> Arrays.stream(blocks).map(o -> o instanceof RegistryObject<?> ? ((RegistryObject<?>) o).get() : (Block) o).anyMatch(b -> b == w.getBlockState(p).getBlock());
        }

        public static BiFunction<World, BlockPos, Boolean> of(Material... materials) {
            final Set<Material> set = Sets.newHashSet(materials);
            return (w, p) -> set.contains(w.getBlockState(p).getMaterial());
        }

        public static BiFunction<World, BlockPos, Boolean> of(BiPredicate<World, BlockPos> f) {
            return f::test;
        }

        public static BiFunction<World, BlockPos, Boolean> ofAir() {
            return World::isAirBlock;
        }

        public static BiFunction<World, BlockPos, Boolean> ofAny() {
            return (w, p) -> true;
        }
    }

    public static class RequirementData {

        private final RegistryObject<Block> block;
        private final Facing facing;
        private final int min;
        private final int max;

        public RequirementData(RegistryObject<Block> block, Facing facing, int min, int max) {
            this.block = block;
            this.facing = facing;
            this.min = min;
            this.max = max;
        }

        public boolean isValid(LayerData info, World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() != block.get()) return false;
            Direction facing = null;
            for (Property<?> p : state.getProperties()) {
                if (p.getValueClass().equals(Direction.class) && p.getName().toLowerCase(Locale.ROOT).contains("facing")) {
                    //noinspection unchecked
                    Property<Direction> property = (Property<Direction>) p;
                    facing = state.get(property);
                    break;
                }
            }
            if (facing == null) return this.facing == Facing.ANY;
            BlockPos offset = pos.offset(facing);
            switch (this.facing) {
                case SIDEWAYS:
                    if ((info.hasFrame && info.isValidBlock(LayerPiece.FRAME, world, offset))
                            || info.isValidBlock(LayerPiece.WALL, world, offset))
                        return true;
                    break;
                case INWARDS_OUTWARDS:
                    boolean isFacingFrame = info.hasFrame ? info.isValidBlock(LayerPiece.FRAME, world, offset)
                            : info.isValidBlock(LayerPiece.WALL, world, offset.offset(facing));
                    boolean isFacingWall = info.isValidBlock(LayerPiece.WALL, world, offset);
                    if (!isFacingFrame && !isFacingWall)
                        return true;
                    break;
                case ANY:
                    return true;
            }
            return false;
        }
    }
}
