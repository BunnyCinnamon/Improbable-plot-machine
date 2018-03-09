/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.katsstuff.mirror.client.baked.Baked;
import net.katsstuff.mirror.client.baked.BakedBrightness;
import net.katsstuff.mirror.client.baked.BakedPerspective;
import net.katsstuff.mirror.client.baked.QuadBuilder;
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
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class BakedGravityHopper extends BakedBrightness {

	public static final Map<ItemCameraTransforms.TransformType, TRSRTransformation> TRANSFORMS = ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
			.put(ItemCameraTransforms.TransformType.GUI, BakedPerspective.mkTransform(0F, 0F, 0F, 30F, 45F, 0F, 0.75F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, 0F, 0F, 0F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, 0F, 0F, 0F, 225F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.GROUND, BakedPerspective.mkTransform(0F, 3.5F, 0F, 0F, 0F, 0F, 0.6F))
			.put(ItemCameraTransforms.TransformType.FIXED, BakedPerspective.mkTransform(0F, 1F, 0F, 0F, 0F, 0F, 0.6F))
			.build();
	private TextureAtlasSprite[] overlay = new TextureAtlasSprite[3];
	private TextureAtlasSprite base;

	@Override
	public ResourceLocation[] getTextures() {
		return ResourceLibrary.GRAVITY_HOPPER_OVERLAY;
	}

	@Override
	public ResourceLocation getParticle() {
		return ResourceLibrary.GRAVITY_HOPPER;
	}

	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		for(int i = 0; i < 3; i++) {
			this.overlay[i] = sprites.apply(ResourceLibrary.GRAVITY_HOPPER_OVERLAY[i]);
		}
		this.base = sprites.apply(ResourceLibrary.GRAVITY_HOPPER);
		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format) {
		List<BakedQuad> quads = Lists.newArrayList();
		if(state == null) {
			//Base
			addCube(format, quads, EnumFacing.UP, base, base, base, false);
			//Overlay
			addCube(format, quads, EnumFacing.UP, overlay[0], overlay[1], overlay[2], false);
		} else {
			EnumFacing face = state.getValue(BlockDirectional.FACING);
			switch(MinecraftForgeClient.getRenderLayer()) {
				case SOLID:
					//Base
					addCube(format, quads, face, base, base, base, false);
					break;
				case CUTOUT_MIPPED:
					//Overlay
					addCube(format, quads, face, overlay[0], overlay[1], overlay[2], true);
					break;
			}
		}
		return quads;
	}

	private void addCube(VertexFormat format, List<BakedQuad> quads, EnumFacing facing, TextureAtlasSprite up, TextureAtlasSprite down, TextureAtlasSprite side, boolean shine) {
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(5, 5, 5)
				.setTo(11, 11, 11)
				.addAll(5F, 11F, 5F, 11F, side)
				.addFace(UP, 5F, 11F, 5F, 11F, up)
				.addFace(DOWN, 5F, 11F, 5F, 11F, down)
				.rotate(facing, DOWN)
				.setHasBrightness(shine)
				.bakeJava()
		);
	}

	@Override
	public Map<ItemCameraTransforms.TransformType, TRSRTransformation> getTransformsJava() {
		return TRANSFORMS;
	}
}
