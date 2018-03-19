/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by <Arekkuusu> on 19/03/2018.
 * It's distributed as part of Solar.
 */
public final class QuadCache {

	private Map<IBlockState, Pair<List<BakedQuad>, Set<BlockRenderLayer>>> formats = Maps.newHashMap();

	public List<BakedQuad> compute(@Nullable IBlockState state, Consumer<List<BakedQuad>> f) {
		if(!formats.containsKey(state)) {
			List<BakedQuad> quads = Lists.newArrayList();
			Set<BlockRenderLayer> layers = Sets.newHashSet(MinecraftForgeClient.getRenderLayer());
			f.accept(quads);
			formats.put(state, Pair.of(quads, layers));
		} else if(state != null && hasRenderLayer(state)) {
			List<BakedQuad> quads = formats.get(state).getKey();
			Set<BlockRenderLayer> layers = formats.get(state).getValue();
			f.accept(quads);
			layers.add(MinecraftForgeClient.getRenderLayer());
		}
		return formats.get(state).getKey();
	}

	private boolean hasRenderLayer(IBlockState state) {
		Set<BlockRenderLayer> layers = formats.get(state).getValue();
		return Arrays.stream(BlockRenderLayer.values())
				.filter(l -> state.getBlock().canRenderInLayer(state, l))
				.anyMatch(l -> !layers.contains(l));
	}

	public void reloadTextures() {
		this.formats.clear();
	}
}
