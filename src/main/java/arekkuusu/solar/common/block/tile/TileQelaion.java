/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.relativity.IRelativeTile;
import arekkuusu.solar.api.entanglement.relativity.RelativityHandler;
import arekkuusu.solar.common.block.BlockQelaion;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by <Snack> on 24/02/2018.
 * It's distributed as part of Solar.
 */
public class TileQelaion extends TileRelativeBase implements ITickable {

	private Set<EnumFacing> facings = Sets.newLinkedHashSet();
	private int facingIndex;
	private int nodeIndex;
	private int outputs;
	private UUID nodes;

	private int iteration; //Keeps track of calls to prevent infinite loops
	private boolean fake; //If it first tested a capability before accessing it

	@Override
	public void update() { // A tick has passed, therefore loop is reset
		this.iteration = 0;
		this.fake = false;
	}

	private boolean breakIteration() {
		return iteration++ > 4;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		markDirty();
		this.fake = true; //Validates the fake access
		return hasAccess(capability, facing);
	}

	public boolean hasAccess(Capability<?> capability, @Nullable EnumFacing from) {
		if(from == null || breakIteration()) return false;
		ImmutableList<TileQelaion> nodes;
		if(facingIndex < outputs) {
			return hasFacing(capability, facingIndex);
		} else if((nodes = getNodeList()).isEmpty() && outputs > 0) {
			return hasFacing(capability, 0);
		} else if(!nodes.isEmpty()) {
			if(nodeIndex + 1 > nodes.size()) nodeIndex = 0;
			if(!nodes.get(nodeIndex).hasAccess(capability, null)) {
				nodeIndex++;
				return false;
			}
			return true;
		}
		return super.hasCapability(capability, from);
	}

	private boolean hasFacing(Capability<?> capability, int index) {
		if(fromFacing(capability, index) == null) {
			if(++facingIndex > outputs) {
				facingIndex = 0;
			}
			return false;
		} else {
			return true;
		}
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		markDirty();
		if(this.fake) { //Invalidates the fake access and resets the iteration
			this.fake = false;
			this.iteration = 0;
		}
		return fromAccess(capability, facing);
	}

	@Nullable
	public <T> T fromAccess(Capability<T> capability, @Nullable EnumFacing from) {
		if(from == null || breakIteration()) return null;
		ImmutableList<TileQelaion> nodes;
		if(facingIndex < outputs) {
			return fromFacing(capability, facingIndex++);
		} else if((nodes = getNodeList()).isEmpty() && outputs > 0) {
			facingIndex = 0;
			return fromFacing(capability, facingIndex++);
		} else if(!nodes.isEmpty()) {
			if(nodeIndex + 1 > nodes.size()) nodeIndex = 0;
			facingIndex = 0;
			return nodes.get(nodeIndex++).fromAccess(capability, null);
		}
		return super.getCapability(capability, from);
	}

	@Nullable
	private <T> T fromFacing(Capability<T> capability, int index) {
		EnumFacing facing = getOutputs().get(index);
		BlockPos pos = getPos().offset(facing);
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock().hasTileEntity(state)) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null) {
				return tile.getCapability(capability, facing.getOpposite());
			}
		}
		return null;
	}

	public ImmutableList<TileQelaion> getNodeList() {
		return nodes != null ? ImmutableList.copyOf(
				RelativityHandler.getRelatives(nodes).stream()
						.filter(IRelativeTile::isLoaded)
						.map(n -> (TileQelaion) n)
						.collect(Collectors.toList())
		) : ImmutableList.of();
	}

	@Nullable
	public UUID getNodes() {
		return nodes;
	}

	public void setNodes(@Nullable UUID nodes) {
		IBlockState state = world.getBlockState(getPos());
		world.setBlockState(getPos(), state.withProperty(BlockQelaion.HAS_NODE, nodes != null));
		this.nodes = nodes;
		markDirty();
	}

	public void put(EnumFacing facing) {
		if(facings.contains(facing)) {
			facings.remove(facing);
		} else facings.add(facing);
		outputs = getOutputs().size();
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		updatePosition(world, pos);
		markDirty();
	}

	public List<EnumFacing> getOutputs() {
		return Arrays.stream(EnumFacing.values()).filter(this::isOutput).collect(Collectors.toList());
	}

	public boolean isOutput(EnumFacing facing) {
		return !facings.contains(facing);
	}

	public List<EnumFacing> getInputs() {
		return ImmutableList.copyOf(facings);
	}

	public boolean isInput(EnumFacing facing) {
		return facings.contains(facing);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		super.readNBT(compound);
		if(compound.hasUniqueId("nodes"))
			nodes = compound.getUniqueId("nodes");
		if(compound.hasKey("facingIndex"))
			facingIndex = compound.getInteger("facingIndex");
		if(compound.hasKey("nodeIndex"))
			nodeIndex = compound.getInteger("nodeIndex");
		facings.clear();
		NBTTagList facingsNBT = compound.getTagList("facings", 10);
		for(int i = 0; i < facingsNBT.tagCount(); i++) {
			NBTTagCompound tag = facingsNBT.getCompoundTagAt(i);
			EnumFacing facing = EnumFacing.byName(tag.getString("facing"));
			facings.add(facing);
		}
		outputs = getOutputs().size();
		if(pos != null) world.markBlockRangeForRenderUpdate(pos, pos); // End me
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		super.writeNBT(compound);
		NBTTagList facingsNBT = new NBTTagList();
		facings.forEach(k -> {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("facing", k.getName());
			facingsNBT.appendTag(tag);
		});
		compound.setTag("facings", facingsNBT);
		if(nodes != null)
			compound.setUniqueId("nodes", nodes);
		compound.setInteger("facingIndex", facingIndex);
		compound.setInteger("nodeIndex", nodeIndex);
	}
}
