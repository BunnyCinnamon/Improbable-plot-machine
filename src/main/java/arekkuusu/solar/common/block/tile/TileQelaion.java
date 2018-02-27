/*******************************************************************************
 * Arekkuusu / Solar 2017
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
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by <Snack> on 24/02/2018.
 * It's distributed as part of Solar.
 */
public class TileQelaion extends TileRelativeBase {

	private List<EnumFacing> facings = Lists.newArrayList();
	private int facingIndex;
	private UUID nodes;
	private int nodeIndex;

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		markDirty();
		return hasAccess(capability, facing, 0);
	}

	public boolean hasAccess(Capability<?> capability, @Nullable EnumFacing from, int level) {
		if(level < 0 || level++ > 4) return false; //Do not go deeper than 4 levels. Why? Yes.
		if(from != null && facings.contains(from.getOpposite())) return false;
		ImmutableList<TileQelaion> nodes;
		if(facingIndex < facings.size()) {
			return hasFacing(capability, facingIndex, level);
		} else if((nodes = getNodeList()).isEmpty() && !facings.isEmpty()) {
			return hasFacing(capability, 0, level);
		} else if(!nodes.isEmpty()) {
			if(nodeIndex + 1 > nodes.size()) nodeIndex = 0;
			if(!nodes.get(nodeIndex).hasAccess(capability, null, level)) {
				nodeIndex++;
				return false;
			}
			return true;
		}
		return super.hasCapability(capability, from);
	}

	private boolean hasFacing(Capability<?> capability, int index, int level) {
		if(fromFacing(capability, index, level) == null) {
			if(++facingIndex > facings.size()) {
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
		return access(capability, facing, 0);
	}

	@Nullable
	public <T> T access(Capability<T> capability, @Nullable EnumFacing from, int level) {
		if(level < 0 || level++ > 4) return null; //Do not go deeper than 4 levels. Why? Yes.
		if(from != null && facings.contains(from.getOpposite())) return null;
		ImmutableList<TileQelaion> nodes;
		if(facingIndex < facings.size()) {
			return fromFacing(capability, facingIndex++, level);
		} else if((nodes = getNodeList()).isEmpty() && !facings.isEmpty()) {
			facingIndex = 0;
			return fromFacing(capability, facingIndex++, level);
		} else if(!nodes.isEmpty()) {
			if(nodeIndex + 1 > nodes.size()) nodeIndex = 0;
			facingIndex = 0;
			return nodes.get(nodeIndex++).access(capability, null, level);
		}
		return super.getCapability(capability, from);
	}

	@Nullable
	private <T> T fromFacing(Capability<T> capability, int index, int level) {
		EnumFacing facing = facings.get(index);
		BlockPos pos = getPos().offset(facing);
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock().hasTileEntity(state)) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null) {
				return tile instanceof TileQelaion ?
						((TileQelaion) tile).access(capability, facing, level)
						: tile.getCapability(capability, facing.getOpposite());
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

	public ImmutableList<EnumFacing> getFacings() {
		return ImmutableList.copyOf(facings);
	}

	public void putFacing(EnumFacing facing) {
		if(facings.contains(facing)) {
			facings.remove(facing);
		} else facings.add(facing);
		updatePosition(world, getPos());
		markDirty();
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		super.readNBT(compound);
		if(compound.hasUniqueId("nodes")) {
			nodes = compound.getUniqueId("nodes");
		}
		if(compound.hasKey("facingIndex")) {
			facingIndex = compound.getInteger("facingIndex");
		}
		if(compound.hasKey("nodeIndex")) {
			nodeIndex = compound.getInteger("nodeIndex");
		}
		facings.clear();
		NBTTagList list = compound.getTagList("facings", 10);
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			facings.add(EnumFacing.byName(tag.getString("facing")));
		}
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		super.writeNBT(compound);
		NBTTagList list = new NBTTagList();
		facings.forEach(facing -> {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("facing", facing.getName());
			list.appendTag(tag);
		});
		compound.setTag("facings", list);
		if(nodes != null) {
			compound.setUniqueId("nodes", nodes);
		}
		compound.setInteger("facingIndex", facingIndex);
		compound.setInteger("nodeIndex", nodeIndex);
	}
}
