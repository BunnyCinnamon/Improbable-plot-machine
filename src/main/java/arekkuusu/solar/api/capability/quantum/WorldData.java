package arekkuusu.solar.api.capability.quantum;

import arekkuusu.solar.api.capability.quantum.data.INBTData;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Map;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 14/03/2018.
 * It's distributed as part of Solar.
 */
public abstract class WorldData extends WorldSavedData {

	public static final Map<ResourceLocation, Class<INBTData<?>>> DATA_MAP = Maps.newHashMap();
	public static final String NBT_TAG = "quantum_data";
	public final Map<UUID, INBTData<?>> saved = Maps.newHashMap();

	public WorldData(String name) {
		super(name);
	}
}
