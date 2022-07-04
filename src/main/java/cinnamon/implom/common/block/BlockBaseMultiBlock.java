package cinnamon.implom.common.block;

import cinnamon.implom.api.multiblock.MultiBlockImouto;
import cinnamon.implom.api.multiblock.MultiBlockOniichan;
import cinnamon.implom.common.block.tile.TileMultiBlockImouto;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BlockBaseMultiBlock extends Block implements EntityBlock {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static void handleBreakBlock(Level world, BlockPos pos) {
        var tile = world.getBlockEntity(pos);
        if (tile instanceof MultiBlockImouto) {
            ((MultiBlockImouto) tile).wakeUpOniichan();
        }
    }

    public static void handlePlaceBlock(Level world, BlockPos pos) {
        for (var dir : Direction.values()) {
            var tile = world.getBlockEntity(pos.relative(dir));
            if (tile instanceof MultiBlockOniichan) {
                ((MultiBlockOniichan) tile).okaeriOniichan();
            }
            if (tile instanceof MultiBlockImouto) {
                MultiBlockImouto imouto = (MultiBlockImouto) tile;
                if (imouto.hasValidOniichan()) {
                    imouto.wakeUpOniichan();
                }
            }
        }
    }

    public BlockBaseMultiBlock(Properties properties) {
        super(properties);
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

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level arg, BlockState arg2, BlockEntityType<T> arg3) {
        return EntityBlock.super.getTicker(arg, arg2, arg3);
    }
}
