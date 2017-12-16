/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.network;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.common.block.tile.TilePhenomena;
import arekkuusu.solar.common.entity.EntityCrystalQuartzItem;
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

	public static void syncQuantumChange(UUID uuid, ItemStack stack, int slot) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
		tag.setUniqueId("uuid", uuid);
		tag.setInteger("slot", slot);
		PacketHandler.NETWORK.sendToAll(new ServerToClientPacket(PacketHandler.Q_SYNC_CHANGE, tag));
	}

	public static void syncQuantumChanges(UUID uuid) {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		SolarApi.getEntangledStacks().get(uuid).forEach(stack -> {
			list.appendTag(stack.writeToNBT(new NBTTagCompound()));
		});
		tag.setTag("list", list);
		tag.setUniqueId("uuid", uuid);
	}

	public static void syncQuantumTo(EntityPlayerMP player) {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList tags = new NBTTagList();
		SolarApi.getEntangledStacks().forEach((uuid, stacks) -> {
			NBTTagCompound tag = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			stacks.forEach(stack -> list.appendTag(stack.writeToNBT(new NBTTagCompound())));
			tag.setTag("list", list);
			tag.setUniqueId("uuid", uuid);
			tags.appendTag(tag);
		});
		compound.setTag("tags", tags);
		PacketHandler.NETWORK.sendTo(new ServerToClientPacket(PacketHandler.Q_SYNC_ALL, compound), player);
	}

	public static void syncQuantumToAll() {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList tags = new NBTTagList();
		SolarApi.getEntangledStacks().forEach((uuid, stacks) -> {
			NBTTagCompound tag = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			stacks.forEach(stack -> list.appendTag(stack.writeToNBT(new NBTTagCompound())));
			tag.setTag("list", list);
			tag.setUniqueId("uuid", uuid);
			tags.appendTag(tag);
		});
		compound.setTag("tags", tags);
		PacketHandler.NETWORK.sendToAll(new ServerToClientPacket(PacketHandler.Q_SYNC_ALL, compound));
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

	public static void sendQuartzEffectPacket(EntityCrystalQuartzItem item, Vector3 from, Vector3 to) {
		TargetPoint point = fromEntity(item, 25);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("from", from.writeToNBT(new NBTTagCompound()));
		tag.setTag("to", to.writeToNBT(new NBTTagCompound()));
		PacketHandler.NETWORK.sendToAllAround(new ServerToClientPacket(PacketHandler.QUARTZ_EFFECT, tag), point);
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
