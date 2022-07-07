package cinnamon.implom.common.block;

import cinnamon.implom.api.helper.WorldHelper;
import cinnamon.implom.common.block.tile.TileBlastFurnaceAirVent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class BlockHotBlastAirVent extends BlockBaseMultiBlock implements EntityBlock {

    protected BlockHotBlastAirVent(Properties builder) {
        super(builder.randomTicks());
        this.registerDefaultState(getStateDefinition().any().setValue(BlockBaseMultiBlock.ACTIVE, false));
    }

    @Override
    public void randomTick(BlockState arg, ServerLevel arg2, BlockPos arg3, RandomSource arg4) {
        if (!arg2.isClientSide()) {
            WorldHelper.getTile(TileBlastFurnaceAirVent.class, arg2, arg3).ifPresent(t -> {
                if (!arg2.hasSignal(arg3, Direction.UP)) {
                    t.okaeriOniichan();
                } else {
                    t.invalidateStructure();
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

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level arg, BlockState arg2, BlockEntityType<T> arg3) {
        return (BlockEntityTicker<T>) new TileBlastFurnaceAirVent.Ticking();
    }
}
