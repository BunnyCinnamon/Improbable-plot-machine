package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.api.multiblock.IMultiblockOniichan;
import arekkuusu.implom.api.multiblock.MultiblockDetector;
import arekkuusu.implom.api.state.Properties;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMultiblockOniichan extends TileBase implements IMultiblockOniichan {

	public MultiblockDetector.MultiblockStructure structure;
	public MultiblockDetector multiblockDetector;
	public BlockPos structureMinPos, structureMaxPos;
	public boolean isStructureActive = false;
	public boolean active = false;
	public int tick;

	public TileMultiblockOniichan(MultiblockDetector multiblockDetector) {
		this.multiblockDetector = multiblockDetector;
	}

	public void checkStructure() {
		if(!active && isStructureActive) {
			okaeriOniichan();
		}
	}

	@Override
	public void okaeriOniichan() {
		boolean wasActive = active;
		if(structure == null || getMultiblockDetector().checkCanStructureBeDetected(world, structure)) {
			structure = getMultiblockDetector().detectMultiblock(world, getPos().offset(getFacing().getOpposite()));
			active = structure != null;
			if(active) {
				isStructureActive = true;
			}
			if(wasActive != active) {
				updateStructure();
			}
			if(active) {
				MultiblockDetector.assignMultiBlockServants(getWorld(), getPos(), structure.blocks);
			}
		}
		else {
			structure = null;
			active = false;
			if(wasActive) updateStructure();
		}
	}

	public void updateStructure() {
		structureMaxPos = structure != null ? structure.maxPos : BlockPos.ORIGIN;
		structureMinPos = structure != null ? structure.minPos : BlockPos.ORIGIN;
		updateStructureData();
		IBlockState state = world.getBlockState(getPos());
		world.setBlockState(getPos(), state.withProperty(Properties.ACTIVE, active));
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

	public void setInvalid() {
		active = false;
		structure = null;
		isStructureActive = false;
		updateStructure();
	}

	public EnumFacing getFacing() {
		return getStateValue(BlockHorizontal.FACING, getPos()).orElse(EnumFacing.UP);
	}

	public MultiblockDetector getMultiblockDetector() {
		return multiblockDetector;
	}

	public boolean hasSecretCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return hasCapability(capability, facing);
	}

	@Nullable
	public <T> T getSecretCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return getCapability(capability, facing);
	}

	/* NBT */
	public static final String TAG_IS_STRUCTURE_ACTIVE = "isStructureActive";
	public static final String TAG_ACTIVE = "active";
	public static final String TAG_MIN_POS = "minPos";
	public static final String TAG_MAX_POS = "maxPos";

	@Override
	void readNBT(NBTTagCompound compound) {
		isStructureActive = compound.getBoolean(TAG_IS_STRUCTURE_ACTIVE);
		active = compound.getBoolean(TAG_ACTIVE);
		structureMaxPos = NBTHelper.getBlockPos(compound, TAG_MAX_POS);
		structureMinPos = NBTHelper.getBlockPos(compound, TAG_MIN_POS);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setBoolean(TAG_IS_STRUCTURE_ACTIVE, isStructureActive);
		compound.setBoolean(TAG_ACTIVE, active);
		NBTHelper.setBlockPos(compound, TAG_MAX_POS, structureMaxPos);
		NBTHelper.setBlockPos(compound, TAG_MIN_POS, structureMinPos);
	}

	@Override
	void readSync(NBTTagCompound compound) {
		active = compound.getBoolean("active");
		structureMaxPos = NBTHelper.getBlockPos(compound, TAG_MAX_POS);
		structureMinPos = NBTHelper.getBlockPos(compound, TAG_MIN_POS);
	}

	@Override
	void writeSync(NBTTagCompound compound) {
		compound.setBoolean("active", active);
		NBTHelper.setBlockPos(compound, TAG_MAX_POS, structureMaxPos);
		NBTHelper.setBlockPos(compound, TAG_MIN_POS, structureMinPos);
	}
}
