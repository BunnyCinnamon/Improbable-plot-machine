package arekkuusu.implom.common.block;

import arekkuusu.implom.api.multiblock.MultiBlockImouto;
import arekkuusu.implom.api.multiblock.MultiBlockOniichan;
import arekkuusu.implom.common.block.tile.TileMultiBlockImouto;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBaseMultiBlock extends Block {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static void handleBreakBlock(IWorldReader world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof MultiBlockImouto) {
            ((MultiBlockImouto) tile).wakeUpOniichan();
        }
    }

    public static void handlePlaceBlock(IWorldReader world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            TileEntity tile = world.getTileEntity(pos.offset(dir));
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
    public void onBlockPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, @Nullable LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        BlockBaseMultiBlock.handlePlaceBlock(p_180633_1_, p_180633_2_);
    }

    @Override
    public void onReplaced(BlockState p_196243_1_, World p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_, boolean p_196243_5_) {
        BlockBaseMultiBlock.handleBreakBlock(p_196243_2_, p_196243_3_);
        super.onReplaced(p_196243_1_, p_196243_2_, p_196243_3_, p_196243_4_, p_196243_5_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileMultiBlockImouto();
    }
}
