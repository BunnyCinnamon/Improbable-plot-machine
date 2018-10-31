/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler.data;

import arekkuusu.implom.api.capability.quantum.WorldData;
import arekkuusu.implom.api.capability.quantum.data.INBTData;
import arekkuusu.implom.api.capability.quantum.data.INBTData.NBTHolder;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.lib.LibMod;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by <Arekkuusu> on 02/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class WorldQuantumData extends WorldData {

	private static final String NAME = LibMod.MOD_ID + ":" + "quantum_data";

	public WorldQuantumData(String name) {
		super(name);
	}

	public static void init(ASMDataTable table) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		List<ASMDataTable.ASMData> loaders = Lists.newArrayList(table.getAll(NBTHolder.class.getName()));
		loaders.sort((l, r) -> l.getObjectName().compareToIgnoreCase(r.getClassName()));
		for(ASMDataTable.ASMData loader : loaders) {
			try {
				Class<?> data = Class.forName(loader.getClassName());
				if(INBTData.class.isAssignableFrom(data)) {
					if(Stream.of(data.getConstructors()).anyMatch(c -> c.getParameterCount() == 0)) {
						NBTHolder nbtData = data.getAnnotation(NBTHolder.class);
						String modId = nbtData.modId();
						String name = nbtData.name();
						ResourceLocation location = new ResourceLocation(modId, name);
						//noinspection unchecked
						DATA_MAP.put(location, (Class<INBTData<?>>) data);
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

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = (NBTTagList) compound.getTag(WorldData.NBT_TAG);
		list.forEach(base -> {
			NBTTagCompound tag = (NBTTagCompound) base;
			String modId = tag.getString("modId");
			String name = tag.getString("name");
			ResourceLocation location = new ResourceLocation(modId, name);
			UUID key = tag.getUniqueId("key");
			try {
				INBTData<?> data = DATA_MAP.get(location).newInstance();
				data.deserializeNBT(tag);
				saved.put(key, data);
			} catch (Exception e) {
				IPM.LOG.fatal("[WorldQuantumData] - Unable to instantiate data holder \"{}\"", location);
				e.printStackTrace();
			}
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		saved.forEach((k, v) -> {
			//noinspection SuspiciousMethodCalls
			if(v.canDeserialize() && DATA_MAP.containsValue(v.getClass())) {
				NBTHolder nbtData = v.getClass().getAnnotation(NBTHolder.class);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("modId", nbtData.modId());
				tag.setString("name", nbtData.name());
				tag.setUniqueId("key", k);
				tag.setTag("data", v.serialize());
				list.appendTag(tag);
			}
		});
		compound.setTag(WorldData.NBT_TAG, list);
		return compound;
	}
}
