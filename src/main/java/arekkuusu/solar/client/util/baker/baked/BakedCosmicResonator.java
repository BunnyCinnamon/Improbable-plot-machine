/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.api.state.MoonPhase;
import arekkuusu.solar.client.util.ResourceLibrary;
import com.google.common.collect.Maps;
import net.katsstuff.mirror.client.baked.Baked;
import net.katsstuff.mirror.client.baked.BakedBrightness;
import net.katsstuff.mirror.client.baked.QuadBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static arekkuusu.solar.api.state.MoonPhase.*;
import static net.minecraft.util.EnumFacing.*;

/**
 * Created by <Arekkuusu> on 05/09/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class BakedCosmicResonator extends BakedBrightness {

	private final Map<MoonPhase, TextureAtlasSprite> phases = Maps.newEnumMap(MoonPhase.class);
	private final QuadCache cache = new QuadCache();
	private TextureAtlasSprite full_moon;
	private TextureAtlasSprite new_moon;
	private TextureAtlasSprite eclipse;

	@Override
	public ResourceLocation[] getTextures() {
		return ResourceLibrary.MOON_PHASES.values().toArray(new ResourceLocation[0]);
	}

	@Override
	public ResourceLocation getParticle() {
		return ResourceLibrary.MOON_PHASES.get(MoonPhase.FULL_MOON);
	}

	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		ResourceLibrary.MOON_PHASES.forEach((key, value) -> phases.put(key, sprites.apply(value)));
		this.full_moon = phases.get(FULL_MOON);
		this.new_moon = phases.get(NEW_MOON);
		this.eclipse = phases.get(ECLIPSE);
		this.cache.reloadTextures();
		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format) {
		return cache.compute(state, quads -> {
			if(state == null) {
				quads.addAll(QuadBuilder.withFormat(format)
						.setFrom(5, 5, 5)
						.setTo(11, 11, 11)
						.addAll(5F, 11F, 5F, 11F, full_moon)
						.bakeJava()
				);
			} else {
				MoonPhase phase = state.getValue(MOON_PHASE);
				QuadBuilder builder = QuadBuilder.withFormat(format)
						.setFrom(5, 5, 5)
						.setTo(11, 11, 11)
						.setHasBrightness(true);
				switch(phase) {
					case NEW_MOON:
						builder = builder.addAll(5F, 11F, 5F, 11F, new_moon);
						break;
					case FULL_MOON:
						builder = builder.addAll(5F, 11F, 5F, 11F, full_moon);
						break;
					case ECLIPSE:
						builder = builder.addAll(5F, 11F, 5F, 11F, eclipse);
						break;
					default: {
						boolean inverse = phase.ordinal() > 4;
						builder = builder
								.addFace(EAST, 5F, 11F, 5F, 11F, inverse ? new_moon : full_moon)
								.addFace(WEST, 5F, 11F, 5F, 11F, inverse ? full_moon : new_moon)
								.addFace(UP, 5F, 11F, 5F, 11F, phases.get(phase))
								.addFace(DOWN, 5F, 11F, 5F, 11F, phases.get(phase)).mirror()
								.addFace(NORTH, 5F, 11F, 5F, 11F, phases.get(phase)).mirror()
								.addFace(SOUTH, 5F, 11F, 5F, 11F, phases.get(phase));
					}
				}
				quads.addAll(builder.bakeJava());
			}
		});
	}
}
