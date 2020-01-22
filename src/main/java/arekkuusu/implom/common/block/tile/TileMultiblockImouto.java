package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.multiblock.IMultiblockImouto;
import arekkuusu.implom.api.multiblock.IMultiblockOniichan;
import arekkuusu.implom.api.state.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class TileMultiblockImouto extends TileBase implements IMultiblockImouto {

	private boolean hasOniichanByHerSide;
	private BlockPos oniichan;
	private Block oniichanBlock;
	private IBlockState oniichanState;

	@Override
	public void wakeUpOniichan() {
		if(this.hasOniichanByHerSide) {
			TileEntity tile = world.getTileEntity(oniichan);
			if(tile instanceof IMultiblockOniichan) {
				((IMultiblockOniichan) tile).okaeriOniichan();
			}
		}
	}

	@Override
	public void overrideOniichan(BlockPos pos) {
		this.oniichan = pos;
		this.oniichanState = world.getBlockState(this.oniichan);
		this.oniichanBlock = this.oniichanState.getBlock();
		this.hasOniichanByHerSide = true;
		markDirty();
		sync();
	}

	@Override
	public boolean setOniichan(BlockPos pos) {
		if(!hasValidOniichan()) {
			this.overrideOniichan(pos);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void removeOniichan() {
		this.oniichan = null;
		this.oniichanState = null;
		this.oniichanBlock = null;
		this.hasOniichanByHerSide = false;
		markDirty();
		sync();
	}

	@Override
	public boolean hasValidOniichan() {
		return this.hasOniichanByHerSide && this.world.getBlockState(this.oniichan) == this.oniichanState
				&& (this.world.getBlockState(this.oniichan).getBlock() == this.oniichanBlock);
	}

	private boolean hasOniichanInfo() {
		return this.oniichan != null && this.oniichanState != null && this.oniichanBlock != null;
	}

	@Override
	public BlockPos getOniichan() {
		return this.oniichan;
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		this.hasOniichanByHerSide = compound.getBoolean("hasOniichanByHerSide");
		if(this.hasOniichanByHerSide) {
			int xCenter = compound.getInteger("xCenter");
			int yCenter = compound.getInteger("yCenter");
			int zCenter = compound.getInteger("zCenter");
			this.oniichan = new BlockPos(xCenter, yCenter, zCenter);
			this.oniichanBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("oniichanBlock")));
			this.oniichanState = Block.getStateById(compound.getInteger("oniichanState"));
		}
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setBoolean("hasOniichanByHerSide", this.hasOniichanByHerSide);
		if(this.hasOniichanByHerSide) {
			compound.setInteger("xCenter", this.oniichan.getX());
			compound.setInteger("yCenter", this.oniichan.getY());
			compound.setInteger("zCenter", this.oniichan.getZ());
			compound.setString("oniichanBlock", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(this.oniichanBlock)).toString());
			compound.setInteger("oniichanState", Block.getStateId(this.oniichanState));
		}
	}
}
