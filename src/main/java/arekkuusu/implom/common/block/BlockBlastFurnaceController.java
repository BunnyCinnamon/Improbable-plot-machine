package arekkuusu.implom.common.block;

import arekkuusu.implom.api.helper.WorldHelper;
import arekkuusu.implom.common.block.tile.TileBlastFurnaceController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Objects;

public class BlockBlastFurnaceController extends HorizontalBlock {

    public BlockBlastFurnaceController(Properties p_i48377_1_) {
        super(p_i48377_1_);
        setDefaultState(getDefaultState().with(BlockBaseMultiBlock.ACTIVE, false).with(HORIZONTAL_FACING, Direction.SOUTH));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        if (!p_225533_2_.isRemote) {
            if (!p_225533_4_.isSneaking()) {
                WorldHelper.getTile(TileBlastFurnaceController.class, p_225533_2_, p_225533_3_).ifPresent(tile -> {
                    if (p_225533_4_.getHeldItem(p_225533_5_).getItem() == Items.FLINT_AND_STEEL)
                        tile.okaeriOniichan();
                    else if (drainWaterBucket(p_225533_4_.getHeldItem(p_225533_5_)))
                        tile.invalidateStructure();
                    else if(tile.isActiveLazy()) {
                        NetworkHooks.openGui((ServerPlayerEntity) p_225533_4_, tile, p_225533_3_);
                    }
                });
            }
        }
        return super.onBlockActivated(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
    }

    public boolean drainWaterBucket(ItemStack stack) {
        LazyOptional<IFluidHandlerItem> optional = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, Direction.UP);
        if (optional.isPresent()) {
            IFluidHandler handler = optional.orElse(Objects.requireNonNull(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.getDefaultInstance()));
            FluidStack fluid = handler.drain(1000, IFluidHandler.FluidAction.SIMULATE);
            if (fluid.getAmount() == 1000) {
                handler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(BlockBaseMultiBlock.ACTIVE) ? 5 : 0;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, p_196258_1_.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(HorizontalBlock.HORIZONTAL_FACING, BlockBaseMultiBlock.ACTIVE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileBlastFurnaceController();
    }
}
