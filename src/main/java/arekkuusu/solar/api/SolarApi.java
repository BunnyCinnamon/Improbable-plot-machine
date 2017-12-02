/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api;

import arekkuusu.solar.api.entanglement.relativity.IRelativeTile;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class was created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
public class SolarApi {

	private static final ImmutableMap<Side, Map<UUID, List<ItemStack>>> ENTANGLED_MAP = ImmutableMap.of(
			Side.SERVER, Maps.newHashMap(),
			Side.CLIENT, Maps.newHashMap()
	);
	private static final Map<UUID, List<IRelativeTile>> RELATIVITY_MAP = Maps.newHashMap();

	/**
	 * Map containing all Items linked to an uuid.
	 * <p>
	 *     Side sensitive.
	 *     Do not modify unless you sync it yourself.
	 * </p>
	 *
	 * @return {@link HashMap}
	 */
	public static Map<UUID, List<ItemStack>> getEntangledStacks() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		return ENTANGLED_MAP.get(side);
	}

	public static Map<UUID, List<IRelativeTile>> getRelativityMap() {
		return RELATIVITY_MAP;
	}
}
