/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.resource;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

/**
 * Created by <Arekkuusu> on 05/07/2017.
 * It's distributed as part of Solar.
 */
public class ResourceUtil {

	public static ResourceLocation[] from(int amount, String name, Function<String, ResourceLocation> function) {
		ResourceLocation[] locations = new ResourceLocation[amount];
		for(int i = 0; i < amount; i++) {
			locations[i] = function.apply(name + i);
		}
		return locations;
	}

	public static <T extends Enum<T> & IStringSerializable> ImmutableMap<T, ResourceLocation> from(Class<T> clazz, String name, Function<String, ResourceLocation> function) {
		ImmutableMap.Builder<T, ResourceLocation> builder = ImmutableMap.builder();
		T[] enums = clazz.getEnumConstants();
		for(T enu : enums) {
			builder.put(enu, function.apply(name + enu.getName()));
		}
		return builder.build();
	}
}
