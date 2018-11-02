/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler.data;

import arekkuusu.implom.common.lib.LibMod;
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
 * Created by <Arekkuusu> on 27/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class WorldAlternatorData extends WorldSavedData {

	private static final String NAME = LibMod.MOD_ID + ":" + "alternator_data";
	public final Map<UUID, Set<UUID>> active = Maps.newHashMap();

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
		return active.containsKey(key) ? active.get(key).size() : 0;
	}

	public void add(UUID key, UUID value) {
		active.putIfAbsent(key, Sets.newHashSet());
		active.get(key).add(value);
		markDirty();
	}

	public void remove(UUID key, UUID value) {
		active.putIfAbsent(key, Sets.newHashSet());
		active.get(key).remove(value);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = compound.getTagList("map", 9);
		list.forEach(tag -> {
			UUID key = ((NBTTagCompound) tag).getUniqueId("key");
			Set<UUID> value = Sets.newHashSet();
			NBTTagList subList = compound.getTagList("set", 9);
			for(NBTBase subTag : subList) {
				UUID uuid = ((NBTTagCompound) subTag).getUniqueId("key");
				value.add(uuid);
			}
			if(!value.isEmpty()) {
				active.put(key, value);
			}
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		active.forEach((k, v) -> {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setUniqueId("key", k);
			NBTTagList subList = new NBTTagList();
			for(UUID uuid : v) {
				NBTTagCompound subTag = new NBTTagCompound();
				subTag.setUniqueId("key", uuid);
				subList.appendTag(subTag);
			}
			tag.setTag("set", subList);
			list.appendTag(tag);
		});
		compound.setTag("map", list);
		return compound;
	}
}
