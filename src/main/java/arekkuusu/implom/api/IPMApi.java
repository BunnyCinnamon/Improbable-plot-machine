/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api;

import arekkuusu.implom.api.capability.data.INBTData;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
//If you modify any of these I will break your bones
public abstract class IPMApi {

	private static IPMApi instance;
	public final Map<UUID, Map<Class<?>, INBTData<?>>> dataMap = Maps.newHashMap();
	public final Map<ResourceLocation, Class<INBTData<?>>> classMap = Maps.newHashMap();

	public static void setInstance(IPMApi instance) {
		if(IPMApi.instance == null) {
			IPMApi.instance = instance;
		}
	}

	public static IPMApi getInstance() {
		return instance;
	}

	public abstract void loadWorld(World world);

	public abstract void unloadWorld();

	public abstract void markWorldDirty();
}
