/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 03/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public abstract class BrightBakedModel implements IBakedModel {

	private final VertexFormat format;

	public BrightBakedModel(VertexFormat format) {
		this.format = new VertexFormat(format).addElement(DefaultVertexFormats.TEX_2S);
	}

	private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, TextureAtlasSprite sprite, float u, float v, boolean hasBrightness) {
		for(int e = 0; e < format.getElementCount(); e++) {
			switch(format.getElement(e).getUsage()) {
				case POSITION:
					builder.put(e, (float) x, (float) y, (float) z, 1.0f);
					break;
				case COLOR:
					builder.put(e, 1F, 1F, 1F, 1F);
					break;
				case UV:
					if(format.getElement(e).getIndex() == 1) {
						if(hasBrightness) {
							builder.put(e, 1F, 1F);
						} else {
							builder.put(e);
						}
					} else {
						u = sprite.getInterpolatedU(u);
						v = sprite.getInterpolatedV(v);
						builder.put(e, u, v, 0F, 1F);
					}
					break;
				case NORMAL:
					if(hasBrightness) {
						builder.put(e, 0F, 1F, 0F);
					} else {
						builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z);
					}
					break;
				default:
					builder.put(e);
					break;
			}
		}
	}

	BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, boolean hasBrightness) {
		Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		builder.setTexture(sprite);
		putVertex(builder, normal, v1.x, v1.y, v1.z, sprite, 16, 16, hasBrightness);
		putVertex(builder, normal, v2.x, v2.y, v2.z, sprite, 16, 0, hasBrightness);
		putVertex(builder, normal, v3.x, v3.y, v3.z, sprite, 0, 0, hasBrightness);
		putVertex(builder, normal, v4.x, v4.y, v4.z, sprite, 0, 16, hasBrightness);
		return builder.build();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
}
