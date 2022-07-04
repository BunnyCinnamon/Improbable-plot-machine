package cinnamon.implom.common.block;

import cinnamon.implom.common.block.tile.TileMultiBlockImouto;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBaseMultiBlockGlass extends AbstractGlassBlock implements EntityBlock {

    public BlockBaseMultiBlockGlass(Properties properties) {
        super(properties.strength(0.3F).sound(SoundType.GLASS).noOcclusion());
    }

    @Override
    public void setPlacedBy(Level arg, BlockPos arg2, BlockState arg3, @org.jetbrains.annotations.Nullable LivingEntity arg4, ItemStack arg5) {
        BlockBaseMultiBlock.handlePlaceBlock(arg, arg2);
    }

    @Override
    public void onRemove(BlockState arg, Level arg2, BlockPos arg3, BlockState arg4, boolean bl) {
        BlockBaseMultiBlock.handleBreakBlock(arg2, arg3);
        super.onRemove(arg, arg2, arg3, arg4, bl);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new TileMultiBlockImouto(arg, arg2);
    }
}
