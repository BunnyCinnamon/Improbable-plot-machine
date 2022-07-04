package cinnamon.implom.common.block;

import cinnamon.implom.api.helper.WorldHelper;
import cinnamon.implom.common.block.tile.TileBlastFurnaceController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class BlockBlastFurnaceController extends HorizontalDirectionalBlock implements EntityBlock {

    public BlockBlastFurnaceController(BlockBehaviour.Properties p_i48377_1_) {
        super(p_i48377_1_);
        registerDefaultState(getStateDefinition().any().setValue(BlockBaseMultiBlock.ACTIVE, false).setValue(FACING, Direction.SOUTH));
    }

    @Override
    public InteractionResult use(BlockState arg, Level arg2, BlockPos arg3, Player arg4, InteractionHand arg5, BlockHitResult arg6) {
        if (!arg2.isClientSide()) {
            if (!arg4.isShiftKeyDown()) {
                WorldHelper.getTile(TileBlastFurnaceController.class, arg2, arg3).ifPresent(tile -> {
                    if (arg4.getItemInHand(arg5).getItem() == Items.FLINT_AND_STEEL)
                        tile.okaeriOniichan();
                    else if (drainWaterBucket(arg4.getItemInHand(arg5)))
                        tile.invalidateStructure();
                    else if (tile.isActiveLazy()) {
                        //NO-OP
                    }
                });
            }
        }
        return super.use(arg, arg2, arg3, arg4, arg5, arg6);
    }

    public boolean drainWaterBucket(ItemStack stack) {
        LazyOptional<IFluidHandlerItem> optional = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, Direction.UP);
        if (optional.isPresent()) {
            IFluidHandler handler = optional.resolve().get();
            FluidStack fluid = handler.drain(1000, IFluidHandler.FluidAction.SIMULATE);
            if (fluid.getAmount() == 1000) {
                handler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState arg) {
        return super.useShapeForLightOcclusion(arg);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState arg, BlockGetter arg2, BlockPos arg3) {
        return true;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        return defaultBlockState().setValue(FACING, arg.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(FACING, BlockBaseMultiBlock.ACTIVE);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new TileBlastFurnaceController(arg, arg2);
    }
}
