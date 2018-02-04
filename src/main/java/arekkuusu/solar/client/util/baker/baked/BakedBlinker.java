/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.util.ResourceLibrary;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
			.put(ItemCameraTransforms.TransformType.GUI, get(0F, 4F, 0F, 30F, 45F, 0F, 0.6F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(0F, 4F, 0F, 0F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(0F, 4F, 0F, 0F, 225F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.GROUND, get(0F, 3.8F, 0F, 0F, 0F, 0F, 0.45F))
			.put(ItemCameraTransforms.TransformType.FIXED, get(0F, 1F, 0F, 0F, -90F, 90F, 1F))
			.build();
	private final TextureAtlasSprite base;
	private final TextureAtlasSprite top_on;
	private final TextureAtlasSprite bottom_on;
	private final TextureAtlasSprite top_off;
	private final TextureAtlasSprite bottom_off;

	public BakedBlinker(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		base = getter.apply(ResourceLibrary.BLINKER_BASE);
		top_on = getter.apply(ResourceLibrary.BLINKER_TOP_ON);
		bottom_on = getter.apply(ResourceLibrary.BLINKER_BOTTOM_ON);
		top_off = getter.apply(ResourceLibrary.BLINKER_TOP_OFF);
		bottom_off = getter.apply(ResourceLibrary.BLINKER_BOTTOM_OFF);
	}

	@Override
	List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format) {
		List<BakedQuad> quads = Lists.newArrayList();
		if(state == null) {
			//Base
			addBaseQuads(quads, format, EnumFacing.DOWN);
			//Overlay
			addOverlayQuads(quads, format, EnumFacing.DOWN, false, true);
		} else {
			EnumFacing facing = state.getValue(BlockDirectional.FACING);
			switch(MinecraftForgeClient.getRenderLayer()) {
				case SOLID:
					//Base
					addBaseQuads(quads, format, facing);
					break;
				case CUTOUT_MIPPED:
					//Overlay
					addOverlayQuads(quads, format, facing, true, state.getValue(State.ACTIVE));
					break;
			}
		}
		return quads;
	}

	private void addBaseQuads(List<BakedQuad> quads, VertexFormat format, EnumFacing facing) {
		QuadBuilder base_quads = QuadBuilder.withFormat(format)
				.setFrom(2, 0, 2)
				.setTo(14, 1, 14)
				.addAll(2F, 14F, 2F, 2F, base)
				.addFace(UP, 2F, 14F, 3F, 14F, base)
				.addFace(DOWN, 2F, 14F, 2F, 14F, base)
				.rotate(facing, DOWN);
		quads.addAll(base_quads.bake());
	}

	private void addOverlayQuads(List<BakedQuad> quads, VertexFormat format, EnumFacing facing, boolean bright, boolean on) {
		QuadBuilder overlay_quads = QuadBuilder.withFormat(format)
				.setFrom(2, 0, 2)
				.setTo(14, 1, 14)
				.addAll(2F, 14F, 2F, 2F, on ? top_on : top_off)
				.addFace(UP, 2F, 14F, 2F, 14F, on ? top_on : top_off)
				.addFace(DOWN, 2F, 14F, 2F, 14F, on ? bottom_on : bottom_off)
				.setHasBrightness(bright)
				.rotate(facing, DOWN);
		quads.addAll(overlay_quads.bake());
	}

	@Override
	public Map<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms() {
		return TRANSFORMS;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
