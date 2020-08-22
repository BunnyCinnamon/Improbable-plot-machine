package arekkuusu.implom.common.block;

import arekkuusu.implom.api.multiblock.IMultiblockImouto;
import arekkuusu.implom.api.multiblock.IMultiblockOniichan;
import arekkuusu.implom.common.block.tile.TileMultiblockImouto;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBaseMultiBlock extends Block {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static void handleBreakBlock(IWorldReader world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IMultiblockImouto) {
            ((IMultiblockImouto) tile).wakeUpOniichan();
        }
    }

    public static void handlePlaceBlock(IWorldReader world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            TileEntity tile = world.getTileEntity(pos.offset(dir));
            if (tile instanceof IMultiblockOniichan) {
                ((IMultiblockOniichan) tile).okaeriOniichan();
            }
            if (tile instanceof IMultiblockImouto) {
                IMultiblockImouto imouto = (IMultiblockImouto) tile;
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
    public void onBlockPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, @Nullable LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        BlockBaseMultiBlock.handlePlaceBlock(p_180633_1_, p_180633_2_);
    }

    @Override
    public void onPlayerDestroy(IWorld p_176206_1_, BlockPos p_176206_2_, BlockState p_176206_3_) {
        BlockBaseMultiBlock.handleBreakBlock(p_176206_1_, p_176206_2_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileMultiblockImouto();
    }
}
