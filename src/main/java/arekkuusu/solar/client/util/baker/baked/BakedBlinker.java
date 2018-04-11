/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.util.ResourceLibrary;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.mirror.client.baked.Baked;
import net.katsstuff.mirror.client.baked.BakedBrightness;
import net.katsstuff.mirror.client.baked.BakedPerspective;
import net.katsstuff.mirror.client.baked.QuadBuilder;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Predef;
import scala.collection.JavaConverters;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static net.minecraft.util.EnumFacing.DOWN;
import static net.minecraft.util.EnumFacing.UP;

/**
 * Created by <Arekkuusu> on 05/09/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class BakedBlinker extends BakedBrightness {

	public static final Map<ItemCameraTransforms.TransformType, TRSRTransformation> TRANSFORMS = ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
			.put(ItemCameraTransforms.TransformType.GUI, BakedPerspective.mkTransform(0F, 4F, 0F, 30F, 45F, 0F, 0.75F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, 3.5F, 0F, 75F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, 3.5F, 0F, 75F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, 4F, 0F, 0F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, 4F, 0F, 0F, 225F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.GROUND, BakedPerspective.mkTransform(0F, 3.8F, 0F, 0F, 0F, 0F, 0.25F))
			.put(ItemCameraTransforms.TransformType.FIXED, BakedPerspective.mkTransform(0F, 1F, 0F, 0F, -90F, 90F, 0.5F))
			.build();
	private final QuadCache cache = new QuadCache();
	private TextureAtlasSprite base;
	private TextureAtlasSprite top_on;
	private TextureAtlasSprite bottom_on;
	private TextureAtlasSprite top_off;
	private TextureAtlasSprite bottom_off;

	@Override
	public ResourceLocation[] getTextures() {
		return new ResourceLocation[]{
				ResourceLibrary.BLINKER_BASE,
				ResourceLibrary.BLINKER_TOP_ON,
				ResourceLibrary.BLINKER_BOTTOM_ON,
				ResourceLibrary.BLINKER_TOP_OFF,
				ResourceLibrary.BLINKER_BOTTOM_OFF
		};
	}

	@Override
	public ResourceLocation getParticle() {
		return ResourceLibrary.BLINKER_BASE;
	}

	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		this.base = sprites.apply(ResourceLibrary.BLINKER_BASE);
		this.top_on = sprites.apply(ResourceLibrary.BLINKER_TOP_ON);
		this.bottom_on = sprites.apply(ResourceLibrary.BLINKER_BOTTOM_ON);
		this.top_off = sprites.apply(ResourceLibrary.BLINKER_TOP_OFF);
		this.bottom_off = sprites.apply(ResourceLibrary.BLINKER_BOTTOM_OFF);
		this.cache.reloadTextures();
		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state) {
		return cache.compute(state, quads -> {
			if(state == null) {
				addBaseQuads(quads, DefaultVertexFormats.ITEM, EnumFacing.DOWN);
				addOverlayQuads(quads, DefaultVertexFormats.ITEM, EnumFacing.DOWN, true);
			} else {
				EnumFacing facing = state.getValue(BlockDirectional.FACING);
				switch(MinecraftForgeClient.getRenderLayer()) {
					case SOLID:
						addBaseQuads(quads, format(), facing);
						break;
					case CUTOUT_MIPPED:
						addOverlayQuads(quads, format(), facing, state.getValue(State.ACTIVE));
						break;
				}
			}
		});
	}

	private void addBaseQuads(List<BakedQuad> quads, VertexFormat format, EnumFacing facing) {
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(2, 0, 2)
				.setTo(14, 1, 14)
				.addAll(2F, 14F, 2F, 2F, base)
				.addFace(UP, 2F, 14F, 3F, 14F, base)
				.addFace(DOWN, 2F, 14F, 2F, 14F, base)
				.rotate(facing, DOWN)
				.bakeJava()
		);
	}

	private void addOverlayQuads(List<BakedQuad> quads, VertexFormat format, EnumFacing facing, boolean on) {
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(2, 0, 2)
				.setTo(14, 1, 14)
				.addAll(2F, 14F, 2F, 2F, on ? top_on : top_off)
				.addFace(UP, 2F, 14F, 2F, 14F, on ? top_on : top_off)
				.addFace(DOWN, 2F, 14F, 2F, 14F, on ? bottom_on : bottom_off)
				.setHasBrightness(true)
				.rotate(facing, DOWN)
				.bakeJava()
		);
	}

	@Override
	public scala.collection.immutable.Map<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms() {
		return JavaConverters.mapAsScalaMapConverter(TRANSFORMS).asScala().toMap(Predef.$conforms()); // Lies
	}

	@Override
	public Map<ItemCameraTransforms.TransformType, TRSRTransformation> getTransformsJava() {
		return TRANSFORMS;
	}
}
