/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler.data;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.api.capability.data.INBTData;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.lib.LibMod;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by <Arekkuusu> on 02/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class WorldNBTData extends WorldSavedData {

	private static final String NAME = LibMod.MOD_ID + ":" + "nbt_data";

	public WorldNBTData(String name) {
		super(name);
	}

	public static void init(ASMDataTable table) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		List<ASMDataTable.ASMData> loaders = Lists.newArrayList(table.getAll(INBTData.NBTHolder.class.getName()));
		loaders.sort((l, r) -> l.getObjectName().compareToIgnoreCase(r.getClassName()));
		for(ASMDataTable.ASMData loader : loaders) {
			try {
				Class<?> data = Class.forName(loader.getClassName());
				if(INBTData.class.isAssignableFrom(data)) {
					if(Stream.of(data.getConstructors()).anyMatch(c -> c.getParameterCount() == 0)) {
						INBTData.NBTHolder nbtData = data.getAnnotation(INBTData.NBTHolder.class);
						String modId = nbtData.modId();
						String name = nbtData.name();
						ResourceLocation location = new ResourceLocation(modId, name);
						//noinspection unchecked
						IPMApi.getInstance().classMap.put(location, (Class<INBTData<?>>) data);
					} else {
						IPM.LOG.error("[WorldQuantumData] - Class {} has no empty constructor", data.getName());
					}
				} else {
					IPM.LOG.error("[WorldQuantumData] - Class {} is annotated with @NBTHolder but is not an INBTData", data.getName());
				}
			} catch (ClassNotFoundException e) {
				IPM.LOG.error("[WorldQuantumData] - Failed to find class {}", loader.getClassName());
				e.printStackTrace();
			}
		}
		IPM.LOG.info("[Discovered {} NBT data holder(s) in {}]", loaders.size(), stopwatch.stop());
	}

	public static WorldNBTData get(World world) {
		MapStorage storage = world.getMapStorage();
		//noinspection ConstantConditions
		WorldNBTData data = (WorldNBTData) storage.getOrLoadData(WorldNBTData.class, NAME);
		if(data == null) {
			data = new WorldNBTData(NAME);
			storage.setData(NAME, data);
		}
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = (NBTTagList) compound.getTag(NAME);
		list.forEach(nbtBase -> {
			NBTTagCompound nbt = (NBTTagCompound) nbtBase;
			NBTTagList subList = (NBTTagList) nbt.getTag("data");
			UUID key = nbt.getUniqueId("key");
			Map<Class<?>, INBTData<?>> map = Maps.newHashMap();
			for(NBTBase base : subList) {
				NBTTagCompound tag = (NBTTagCompound) base;
				String modId = tag.getString("modId");
				String name = tag.getString("name");
				NBTBase data = tag.getTag("data");
				ResourceLocation location = new ResourceLocation(modId, name);
				try {
					Class<INBTData<?>> cl = IPMApi.getInstance().classMap.get(location);
					INBTData<?> v = cl.newInstance();
					v.deserializeNBT(data);
					map.put(cl, v);
				} catch (Exception e) {
					IPM.LOG.fatal("[WorldQuantumData] - Unable to instantiate data holder \"{}\"", location);
					e.printStackTrace();
				}
			}
			IPMApi.getInstance().dataMap.put(key, map);
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		IPMApi.getInstance().dataMap.forEach((k, map) -> {
			NBTTagCompound nbt = new NBTTagCompound();
			NBTTagList subList = new NBTTagList();
			nbt.setTag("data", subList);
			nbt.setUniqueId("key", k);
			map.forEach((cl, v) -> {
				if(v.canDeserialize() && IPMApi.getInstance().classMap.containsValue(v.getClass())) {
					INBTData.NBTHolder nbtData = v.getClass().getAnnotation(INBTData.NBTHolder.class);
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("modId", nbtData.modId());
					tag.setString("name", nbtData.name());
					tag.setTag("data", v.serializeNBT());
					subList.appendTag(tag);
				}
			});
			list.appendTag(nbt);
		});
		compound.setTag(NAME, list);
		return compound;
	}
}
