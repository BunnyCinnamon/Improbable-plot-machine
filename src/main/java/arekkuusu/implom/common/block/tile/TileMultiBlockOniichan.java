package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.api.helper.WorldHelper;
import arekkuusu.implom.api.multiblock.MultiBlockDetector;
import arekkuusu.implom.api.multiblock.MultiBlockOniichan;
import arekkuusu.implom.common.block.BlockBaseMultiBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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

    public TileMultiBlockOniichan(TileEntityType<?> type, MultiBlockDetector multiBlockDetector) {
        super(type);
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
        if (structure == null || getMultiBlockDetector().checkCanStructureBeDetected(world, structure)) {
            structure = getMultiBlockDetector().detectMultiBlock(world, getPos().offset(getFacing().getOpposite()));
            active = structure != null;
            if (active) {
                isStructureActive = true;
            }
            if (wasActive != active) {
                updateStructure();
            }
            if (active) {
                MultiBlockDetector.assignMultiBlockServants(getWorld(), getPos(), structure.blocks);
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
        BlockState state = getWorld().getBlockState(getPos());
        getWorld().setBlockState(getPos(), state.with(BlockBaseMultiBlock.ACTIVE, active));
        markDirty();
        sync();
    }

    public void updateStructureData() {
        //For Rent
    }

    @Override
    public void validate() {
        super.validate();
        active = false;
    }

    public void invalidateStructure() {
        active = false;
        structure = null;
        isStructureActive = false;
        updateStructure();
    }

    public Direction getFacing() {
        return WorldHelper.getStateValue(getWorld(), getPos(), HorizontalBlock.HORIZONTAL_FACING).orElse(Direction.UP);
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
    void load(CompoundNBT compound) {
        isStructureActive = compound.getBoolean(TAG_IS_STRUCTURE_ACTIVE);
        active = compound.getBoolean(TAG_ACTIVE);
        structureMaxPos = NBTHelper.getBlockPos(compound, TAG_MAX_POS);
        structureMinPos = NBTHelper.getBlockPos(compound, TAG_MIN_POS);
    }

    @Override
    void save(CompoundNBT compound) {
        compound.putBoolean(TAG_IS_STRUCTURE_ACTIVE, isStructureActive);
        compound.putBoolean(TAG_ACTIVE, active);
        NBTHelper.setBlockPos(compound, TAG_MAX_POS, structureMaxPos);
        NBTHelper.setBlockPos(compound, TAG_MIN_POS, structureMinPos);
    }

    @Override
    void readSync(CompoundNBT compound) {
        active = compound.getBoolean("active");
        structureMaxPos = NBTHelper.getBlockPos(compound, TAG_MAX_POS);
        structureMinPos = NBTHelper.getBlockPos(compound, TAG_MIN_POS);
    }

    @Override
    void writeSync(CompoundNBT compound) {
        compound.putBoolean("active", active);
        NBTHelper.setBlockPos(compound, TAG_MAX_POS, structureMaxPos);
        NBTHelper.setBlockPos(compound, TAG_MIN_POS, structureMinPos);
    }
}
