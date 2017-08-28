/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.common.lib.LibMod;
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

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList list = (NBTTagList) nbt.getTag(SolarApi.QUANTUM_DATA);
		list.forEach(stackList -> {
			NBTTagList stacks = (NBTTagList) ((NBTTagCompound) stackList).getTag(LIST);
			UUID key = ((NBTTagCompound) stackList).getUniqueId(KEY);
			stacks.forEach(tag -> SolarApi.putQuantumItem(key, new ItemStack((NBTTagCompound) tag)));
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
