package arekkuusu.implom.api.capability.nbt;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.api.capability.data.INBTData;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

public interface INBTDataCapability<T extends INBTData<?>> {

	BiFunction<Class<?>, Class<?>, INBTData<?>> FUNCTION = (u, c) -> {
		INBTData<?> data = null;
		try {
			data = (INBTData<?>) c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return data;
	};

	default Optional<T> getData(@Nullable UUID uuid) {
		Class<T> c = getDataClass();
		return Optional.ofNullable(uuid)
				.map(key -> IPMApi.getInstance().dataMap.computeIfAbsent(key, k -> Maps.newHashMap()))
				.map(map -> map.computeIfAbsent(c, k -> FUNCTION.apply(k, c)))
				.filter(c::isInstance)
				.map(c::cast);
	}

	Class<T> getDataClass();

	void setKey(@Nullable UUID uuid);

	@Nullable
	UUID getKey();
}
