/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.entanglement.quantum.QuantumHandler;
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

	private static final String NAME = LibMod.MOD_ID + ":" + QuantumHandler.NBT_TAG;
	private static final String LIST = "list";
	private static final String KEY = "key";

	public WorldQuantumData(String name) {
		super(name);
	}

	@SuppressWarnings("ConstantConditions")
	public static WorldQuantumData get(World world) {
		MapStorage storage = world.getMapStorage();
		WorldQuantumData data = (WorldQuantumData) storage.getOrLoadData(WorldQuantumData.class, NAME);

		if(data == null) {
			data = new WorldQuantumData(NAME);
			storage.setData(NAME, data);
		}

		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = (NBTTagList) compound.getTag(QuantumHandler.NBT_TAG);
		list.forEach(stackList -> {
			NBTTagList stacks = (NBTTagList) ((NBTTagCompound) stackList).getTag(LIST);
			UUID key = ((NBTTagCompound) stackList).getUniqueId(KEY);
			stacks.forEach(tag -> QuantumHandler.addQuantumAsync(key, new ItemStack((NBTTagCompound) tag)));
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		SolarApi.getEntangledStacks().forEach((uuid, itemStacks) -> {
			NBTTagList stackList = new NBTTagList();
			itemStacks.forEach(stack -> stackList.appendTag(stack.writeToNBT(new NBTTagCompound())));
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setUniqueId(KEY, uuid);
			nbt.setTag(LIST, stackList);
			list.appendTag(nbt);
		});
		compound.setTag(QuantumHandler.NBT_TAG, list);
		return compound;
	}
}
