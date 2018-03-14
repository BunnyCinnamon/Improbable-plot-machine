/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.common.lib.LibMod;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by <Snack> on 27/01/2018.
 * It's distributed as part of Solar.
 */
public class WorldAlternatorData extends WorldSavedData {

	private static final String NAME = LibMod.MOD_ID + ":" + "alternator_data";
	public final Map<UUID, Set<UUID>> active_map = Maps.newHashMap();

	public WorldAlternatorData(String name) {
		super(name);
	}

	public static WorldAlternatorData get(World world) {
		MapStorage storage = world.getMapStorage();
		//noinspection ConstantConditions
		WorldAlternatorData data = (WorldAlternatorData) storage.getOrLoadData(WorldAlternatorData.class, NAME);
		if(data == null) {
			data = new WorldAlternatorData(NAME);
			storage.setData(NAME, data);
		}
		return data;
	}

	public int getSize(UUID key) {
		return active_map.containsKey(key) ? active_map.get(key).size() : 0;
	}

	public void add(UUID key, UUID value) {
		active_map.putIfAbsent(key, Sets.newHashSet());
		active_map.get(key).add(value);
		markDirty();
	}

	public void remove(UUID key, UUID value) {
		active_map.putIfAbsent(key, Sets.newHashSet());
		active_map.get(key).remove(value);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = compound.getTagList("list", 9);
		list.forEach(tag -> {
			UUID key = ((NBTTagCompound) tag).getUniqueId("key");
			Set<UUID> value = Sets.newHashSet();
			NBTTagList subList = compound.getTagList("value", 9);
			for(NBTBase subTag : subList) {
				UUID uuid = ((NBTTagCompound) subTag).getUniqueId("uuid");
				value.add(uuid);
			}
			if(!value.isEmpty()) {
				active_map.put(key, value);
			}
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		active_map.forEach((k, v) -> {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setUniqueId("key", k);
			NBTTagList subList = new NBTTagList();
			for(UUID uuid : v) {
				NBTTagCompound subTag = new NBTTagCompound();
				subTag.setUniqueId("uuid", uuid);
				subList.appendTag(subTag);
			}
			tag.setTag("value", subList);
			list.appendTag(tag);
		});
		compound.setTag("list", list);
		return compound;
	}
}
