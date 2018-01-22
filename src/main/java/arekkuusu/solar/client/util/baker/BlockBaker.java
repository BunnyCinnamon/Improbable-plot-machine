/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker;

import arekkuusu.solar.client.util.ResourceLibrary;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by <Arekkuusu> on 16/09/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public enum BlockBaker {
	VACUUM_TOP("vacuum_top"),
	VACUUM_PIECE("vacuum_piece"),
	VACUUM_BOTTOM("vacuum_bottom"),
	TRANSLOCATOR_BASE("translocator_base"),
	TRANSLOCATOR_CENTER("translocator_center"),
	TRANSLOCATOR_PIECE_RING("translocator_piece_ring");

	ResourceLocation location;

	List<BakedQuad> quads;
	IModel model;
	IBakedModel baked;

	BlockBaker(String name) {
		location = ResourceLibrary.getLocation(null, ResourceLibrary.ModelLocation.OTHER, name, "");
	}

	public void bake() throws Exception {
		model = ModelLoaderRegistry.getModel(location);
		baked = model.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, ModelLoader.defaultTextureGetter());
		quads = baked.getQuads(null, null, 0);
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public IModel getModel() {
		return model;
	}

	public IBakedModel getBaked() {
		return baked;
	}

	public List<BakedQuad> getQuads() {
		return quads;
	}

	public static void render(BlockBaker model) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for(BakedQuad bakedquad : model.quads) {
			LightUtil.renderQuadColor(buffer, bakedquad, -1);
		}
		tessellator.draw();
	}
}
