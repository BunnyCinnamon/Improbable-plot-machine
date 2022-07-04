package cinnamon.implom.common.block.tile;

import cinnamon.implom.api.helper.NBTHelper;
import cinnamon.implom.api.helper.WorldHelper;
import cinnamon.implom.api.multiblock.MultiBlockDetector;
import cinnamon.implom.api.multiblock.MultiBlockOniichan;
import cinnamon.implom.common.block.BlockBaseMultiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMultiBlockOniichan extends TileBase implements MultiBlockOniichan {

    public MultiBlockDetector.MultiblockStructure structure;
    public MultiBlockDetector multiBlockDetector;
    public BlockPos structureMinPos, structureMaxPos;
    public boolean isStructureActive = false;
    public boolean active = false;
    public int tick;

    public TileMultiBlockOniichan(BlockEntityType<?> type, MultiBlockDetector multiBlockDetector, BlockPos arg2, BlockState arg3) {
        super(type, arg2, arg3);
        this.multiBlockDetector = multiBlockDetector;
    }

    public void checkStructure() {
        if (!active && isStructureActive) {
            okaeriOniichan();
        }
    }

    @Override
    public void okaeriOniichan() {
        boolean wasActive = active;
        if (structure == null || getMultiBlockDetector().checkCanStructureBeDetected(level, structure)) {
            structure = getMultiBlockDetector().detectMultiBlock(level, getBlockPos().relative(getFacing().getOpposite()));
            active = structure != null;
            if (active) {
                isStructureActive = true;
            }
            if (wasActive != active) {
                updateStructure();
            }
            if (active) {
                MultiBlockDetector.assignMultiBlockServants(getLevel(), getBlockPos(), structure.blocks);
            }
        } else {
            structure = null;
            active = false;
            if (wasActive) updateStructure();
        }
    }

    public void updateStructure() {
        structureMaxPos = structure != null ? structure.maxPos : BlockPos.ZERO;
        structureMinPos = structure != null ? structure.minPos : BlockPos.ZERO;
        updateStructureData();
        BlockState state = getLevel().getBlockState(getBlockPos());
        getLevel().setBlockAndUpdate(getBlockPos(), state.setValue(BlockBaseMultiBlock.ACTIVE, active));
        setChanged();
        sync();
    }

    public void updateStructureData() {
        //For Rent
    }

    @Override
    public void onLoad() {
        super.onLoad();
        active = false;
    }

    public void invalidateStructure() {
        active = false;
        structure = null;
        isStructureActive = false;
        updateStructure();
    }

    public Direction getFacing() {
        return WorldHelper.getStateValue(getLevel(), getBlockPos(), HorizontalDirectionalBlock.FACING).orElse(Direction.UP);
    }

    public MultiBlockDetector getMultiBlockDetector() {
        return multiBlockDetector;
    }

    @Nullable
    public <T> LazyOptional<T> getMultiBlockCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap, side);
    }

    /* NBT */
    public static final String TAG_IS_STRUCTURE_ACTIVE = "isStructureActive";
    public static final String TAG_ACTIVE = "active";
    public static final String TAG_MIN_POS = "minPos";
    public static final String TAG_MAX_POS = "maxPos";

    @Override
    void loadDisk(CompoundTag compound) {
        isStructureActive = compound.getBoolean(TAG_IS_STRUCTURE_ACTIVE);
        active = compound.getBoolean(TAG_ACTIVE);
        structureMaxPos = NBTHelper.getBlockPos(compound, TAG_MAX_POS);
        structureMinPos = NBTHelper.getBlockPos(compound, TAG_MIN_POS);
    }

    @Override
    void saveDisk(CompoundTag compound) {
        compound.putBoolean(TAG_IS_STRUCTURE_ACTIVE, isStructureActive);
        compound.putBoolean(TAG_ACTIVE, active);
        NBTHelper.putBlockPos(compound, TAG_MAX_POS, structureMaxPos);
        NBTHelper.putBlockPos(compound, TAG_MIN_POS, structureMinPos);
    }

    @Override
    void writeSync(CompoundTag compound) {
        compound.putBoolean("active", active);
        NBTHelper.putBlockPos(compound, TAG_MAX_POS, structureMaxPos);
        NBTHelper.putBlockPos(compound, TAG_MIN_POS, structureMinPos);
    }

    @Override
    void readSync(CompoundTag compound) {
        active = compound.getBoolean("active");
        structureMaxPos = NBTHelper.getBlockPos(compound, TAG_MAX_POS);
        structureMinPos = NBTHelper.getBlockPos(compound, TAG_MIN_POS);
    }
}
