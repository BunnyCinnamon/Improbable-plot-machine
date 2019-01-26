/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.network;

import arekkuusu.implom.api.capability.nbt.IInventoryNBTDataCapability;
import arekkuusu.implom.common.block.tile.TilePhenomena;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

/**
 * Created by <Arekkuusu> on 10/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public final class PacketHelper {

	public static void sendQuantumMirrorPacket(IInventoryNBTDataCapability instance) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("itemstack", instance.getStackInSlot(0).serializeNBT());
		assert instance.getKey() != null; //Don't
		tag.setUniqueId("key", instance.getKey());
		PacketHandler.NETWORK.sendToAll(new ServerToClientPacket(PacketHandler.QUANTUM_MIRROR, tag));
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
