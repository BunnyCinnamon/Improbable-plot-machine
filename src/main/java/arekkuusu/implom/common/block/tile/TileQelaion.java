/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.Arrays;
import java.util.Set;

/*
 * Created by <Arekkuusu> on 24/02/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileQelaion extends TileBase implements ITickable {

	public final Set<EnumFacing> facings = Sets.newLinkedHashSet(Arrays.asList(EnumFacing.values()));

	@Override
	public void update() {

	}

	public void put(EnumFacing facing) {
		if(facings.contains(facing)) {
			facings.remove(facing);
		} else facings.add(facing);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		markDirty();
		sync();
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 16);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		facings.clear();
		NBTTagList facingsNBT = compound.getTagList("facings", 10);
		for(int i = 0; i < facingsNBT.tagCount(); i++) {
			NBTTagCompound tag = facingsNBT.getCompoundTagAt(i);
			EnumFacing facing = EnumFacing.byName(tag.getString("facing"));
			facings.add(facing);
		}
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		NBTTagList facingsNBT = new NBTTagList();
		facings.forEach(k -> {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("facing", k.getName());
			facingsNBT.appendTag(tag);
		});
		compound.setTag("facings", facingsNBT);
	}
}
