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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.client.MinecraftForgeClient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by <Arekkuusu> on 19/03/2018.
 * It's distributed as part of Solar.
 */
public final class QuadCache {

	private Map<IBlockState, Map<BlockRenderLayer, List<BakedQuad>>> formats = Maps.newHashMap();

	public List<BakedQuad> compute(@Nullable IBlockState state, Consumer<List<BakedQuad>> f) {
		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
		if(!formats.containsKey(state)) {
			Map<BlockRenderLayer, List<BakedQuad>> map = Maps.newHashMap();
			List<BakedQuad> quads = Lists.newArrayList();
			f.accept(quads);
			map.put(layer, quads);
			formats.put(state, map);
		} else if(state != null && !hasRenderLayer(state, layer)) {
			List<BakedQuad> quads = Lists.newArrayList();
			f.accept(quads);
			formats.get(state).put(layer, quads);
		}
		return formats.get(state).get(layer);
	}

	private boolean hasRenderLayer(IBlockState state, BlockRenderLayer layer) {
		return formats.get(state).containsKey(layer);
	}

	public void reloadTextures() {
		this.formats.clear();
	}
}
