/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.network.PacketHandler;
import arekkuusu.solar.common.network.QuantumChangeMessage;
import arekkuusu.solar.common.network.QuantumSyncMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/08/2017.
 * It's distributed as part of Solar.
 */
public class WorldQuantumData extends WorldSavedData {

	private static final String NAME = LibMod.MOD_ID + ":" + SolarApi.QUANTUM_DATA;
	private static final String LIST = "list";
	private static final String KEY = "key";

	public WorldQuantumData(String name) {
		super(name);
	}

	@SuppressWarnings("ConstantConditions")
	public static WorldSavedData get(World world) {
		MapStorage storage = world.getMapStorage();
		WorldSavedData data = storage.getOrLoadData(WorldQuantumData.class, NAME);

		if(data == null) {
			data = new WorldQuantumData(NAME);
			storage.setData(NAME, data);
		}

		return data;
	}

	public static void syncChanges(UUID uuid, ItemStack stack, int slot) {
		QuantumChangeMessage message = new QuantumChangeMessage(uuid, stack, slot);
		PacketHandler.INSTANCE.sendToAll(message);
	}

	public static void syncTo(EntityPlayerMP player) {
		QuantumSyncMessage message = new QuantumSyncMessage(SolarApi.QUANTUM_ITEMS);
		PacketHandler.sendTo(message, player);
	}

	public static void syncToAll() {
		QuantumSyncMessage message = new QuantumSyncMessage(SolarApi.QUANTUM_ITEMS);
		PacketHandler.INSTANCE.sendToAll(message);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList list = (NBTTagList) nbt.getTag(SolarApi.QUANTUM_DATA);
		list.forEach(stackList -> {
			NBTTagList stacks = (NBTTagList) ((NBTTagCompound) stackList).getTag(LIST);
			UUID key = ((NBTTagCompound) stackList).getUniqueId(KEY);
			stacks.forEach(tag -> SolarApi.addQuantumAsync(key, new ItemStack((NBTTagCompound) tag)));
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		SolarApi.QUANTUM_ITEMS.forEach((uuid, itemStacks) -> {
			NBTTagList stackList = new NBTTagList();
			itemStacks.forEach(stack -> stackList.appendTag(stack.writeToNBT(new NBTTagCompound())));
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setUniqueId(KEY, uuid);
			nbt.setTag(LIST, stackList);
			list.appendTag(nbt);
		});
		compound.setTag(SolarApi.QUANTUM_DATA, list);
		return compound;
	}
}
