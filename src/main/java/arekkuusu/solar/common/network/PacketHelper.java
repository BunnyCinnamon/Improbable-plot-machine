/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.network;

import arekkuusu.solar.api.entanglement.inventory.EntangledIItemHandler;
import arekkuusu.solar.api.entanglement.quantum.data.QuantumStackData;
import arekkuusu.solar.common.block.tile.TilePhenomena;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import java.util.UUID;

/**
 * Created by <Arekkuusu> on 10/12/2017.
 * It's distributed as part of Solar.
 */
public class PacketHelper {

	public static void sendQuantumChange(UUID uuid, ItemStack stack, int slot) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
		tag.setUniqueId("uuid", uuid);
		tag.setInteger("slot", slot);
		PacketHandler.NETWORK.sendToAll(new ServerToClientPacket(PacketHandler.QUANTUM_CHANGE, tag));
	}

	public static void sendQuantumChanges(UUID uuid) {
		QuantumStackData data = EntangledIItemHandler.getEntanglement(uuid);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("list", data.write());
		tag.setUniqueId("uuid", uuid);
		PacketHandler.NETWORK.sendToAll(new ServerToClientPacket(PacketHandler.QUANTUM_SYNC, tag));
	}

	public static void syncQuantumTo(EntityPlayerMP player) {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList tags = new NBTTagList();
		EntangledIItemHandler.getEntanglements().forEach((uuid, data) -> {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("list", data.write());
			tag.setUniqueId("uuid", uuid);
			tags.appendTag(tag);
		});
		compound.setTag("tags", tags);
		PacketHandler.NETWORK.sendTo(new ServerToClientPacket(PacketHandler.QUANTUM_SYNC_ALL, compound), player);
	}

	public static void sendPhenomenaPacket(TilePhenomena phenomena) {
		NBTTagCompound tag = new NBTTagCompound();
		BlockPos pos = phenomena.getPos();
		tag.setInteger("x", pos.getX());
		tag.setInteger("y", pos.getY());
		tag.setInteger("z", pos.getZ());
		TargetPoint point = fromTileEntity(phenomena, 25);
		PacketHandler.NETWORK.sendToAllAround(new ServerToClientPacket(PacketHandler.PHENOMENA, tag), point);
	}

	public static TargetPoint fromWorldPos(World world, BlockPos pos, int range) {
		return new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
	}

	public static TargetPoint fromTileEntity(TileEntity te, int range) {
		return new TargetPoint(te.getWorld().provider.getDimension(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), range);
	}

	public static TargetPoint fromEntity(Entity entity, int range) {
		return new TargetPoint(entity.world.provider.getDimension(), entity.posX, entity.posY, entity.posZ, range);
	}
}
