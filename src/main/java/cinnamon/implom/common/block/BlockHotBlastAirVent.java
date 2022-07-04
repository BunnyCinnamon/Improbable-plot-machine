package cinnamon.implom.common.block;

import cinnamon.implom.api.helper.WorldHelper;
import cinnamon.implom.common.block.tile.TileBlastFurnaceAirVent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BlockHotBlastAirVent extends BlockBaseMultiBlock implements EntityBlock {

    protected BlockHotBlastAirVent(Properties builder) {
        super(builder);
        this.registerDefaultState(getStateDefinition().any().setValue(BlockBaseMultiBlock.ACTIVE, false));
    }

    @Override
    public void neighborChanged(BlockState arg, Level arg2, BlockPos arg3, Block arg4, BlockPos arg5, boolean bl) {
        if (!arg2.isClientSide() && arg3.relative(Direction.UP).equals(arg5)) {
            WorldHelper.getTile(TileBlastFurnaceAirVent.class, arg2, arg3).ifPresent(t -> {
                boolean isPowered = arg2.hasSignal(arg3, Direction.DOWN);
                if (isPowered) {
                    if (!t.isStructureActive) {
                        t.okaeriOniichan();
                    } else {
                        t.invalidateStructure();
                    }
                }
            });
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @org.jetbrains.annotations.Nullable Direction direction) {
        return direction == Direction.UP;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(BlockBaseMultiBlock.ACTIVE);
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new TileBlastFurnaceAirVent(arg, arg2);
    }
}
