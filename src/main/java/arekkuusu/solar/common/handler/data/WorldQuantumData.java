/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.entanglement.inventory.EntangledIItemHandler;
import arekkuusu.solar.api.entanglement.quantum.data.IQuantumData;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.lib.LibMod;
import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Map;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/08/2017.
 * It's distributed as part of Solar.
 */
public class WorldQuantumData extends WorldSavedData {

	private static final String NAME = LibMod.MOD_ID + ":" + EntangledIItemHandler.NBT_TAG;

	public static WorldQuantumData get(World world) {
		MapStorage storage = world.getMapStorage();
		//noinspection ConstantConditions
		WorldQuantumData data = (WorldQuantumData) storage.getOrLoadData(WorldQuantumData.class, NAME);
		if(data == null) {
			data = new WorldQuantumData(NAME);
			storage.setData(NAME, data);
		}
		return data;
	}

	public final Map<UUID, IQuantumData<?>> DATA_MAP = Maps.newHashMap();

	public WorldQuantumData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = (NBTTagList) compound.getTag(EntangledIItemHandler.NBT_TAG);
		list.forEach(base -> {
			NBTTagCompound tag = (NBTTagCompound) base;
			UUID key = tag.getUniqueId("key");
			String cl = tag.getString("class");
			IQuantumData<?> data;
			try {
				data = (IQuantumData<?>) Class.forName(cl).newInstance();
				data.deserializeNBT(tag);
				DATA_MAP.put(key, data);
			} catch(Exception e) {
				Solar.LOG.fatal("[WorldQuantumData] - Unable to instantiate class :" + cl, e);
			}
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		DATA_MAP.forEach((k, v) -> {
			if(v.save()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setUniqueId("key", k);
				tag.setString("class", v.getClass().getName());
				tag.setTag("data", v.write());
				list.appendTag(tag);
			}
		});
		compound.setTag(EntangledIItemHandler.NBT_TAG, list);
		return compound;
	}
}
