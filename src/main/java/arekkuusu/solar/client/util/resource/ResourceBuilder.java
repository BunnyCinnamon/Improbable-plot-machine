/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.util.resource;

import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

/**
 * Created by <Arekkuusu> on 05/07/2017.
 * It's distributed as part of Solar.
 */
public class ResourceBuilder {

	public static ResourceLocation[] toArray(int amount, String name, Function<String, ResourceLocation> function) {
		ResourceLocation[] locations = new ResourceLocation[amount];
		for(int i = 0; i < amount; i++) {
			locations[i] = function.apply(name + i);
		}
		return locations;
	}
}
