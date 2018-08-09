/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker;

import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.lib.LibMod;
import net.katsstuff.mirror.client.helper.ModelLocation;
import net.katsstuff.mirror.client.helper.ResourceHelperStatic;
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
	TRANSLOCATOR_PIECE_RING("translocator_piece_ring"),
	CONDUCTOR_PIECE_0("conductor_piece_0"),
	CONDUCTOR_PIECE_1("conductor_piece_1"),
	CONDUCTOR_PIECE_2("conductor_piece_2"),
	CONDUCTOR_PIECE_3("conductor_piece_3"),
	CONDUCTOR_PIECE_4("conductor_piece_4"),
	QIMRANUT("qimranut"),
	QIMRANUT_("qimranut_"),
	QIMRANUT_RING("qimranut_ring"),
	QELAION("qelaion"),
	NEUTRON_BATTERY_BASE("neutron_battery_base"),
	NEUTRON_BATTERY_BLUE("neutron_battery_blue"),
	NEUTRON_BATTERY_GREEN("neutron_battery_green"),
	NEUTRON_BATTERY_PINK("neutron_battery_pink"),
	PHOLARIZER_BASE("pholarizer_base"),
	PHOLARIZER_RING("pholarizer_ring"),
	PHOLARIZER_PILLAR("pholarizer_pillar"),
	PHOLARIZER_POSITIVE("pholarizer_positive"),
	PHOLARIZER_NEGATIVE("pholarizer_negative"),
	FISSION_INDUCER_TOP("fission_inducer_top"),
	FISSION_INDUCER_CENTER("fission_inducer_center"),
	FISSION_INDUCER_INSIDE("fission_inducer_inside"),
	FISSION_INDUCER_BOTTOM("fission_inducer_bottom"),
	ELECTRON("electron"),
	DIFFERENTIATOR_BASE("differentiator_base"),
	DIFFERENTIATOR_CORE("differentiator_core"),
	DIFFERENTIATOR_RING_BOTTOM("differentiator_ring_bottom"),
	DIFFERENTIATOR_RING_MIDDLE("differentiator_ring_middle"),
	DIFFERENTIATOR_RING_TOP("differentiator_ring_top"),
	DIFFERENTIATOR_INTERCEPTOR_BASE("differentiator_interceptor_base"),
	DIFFERENTIATOR_INTERCEPTOR_RING("differentiator_interceptor_ring"),
	DIFFERENTIATOR_INTERCEPTOR_RING_("differentiator_interceptor_ring_"),
	DIFFERENTIATOR_INTERCEPTOR_GLASS("differentiator_interceptor_glass"),
	DIFFERENTIATOR_INTERCEPTOR_BEACON("differentiator_interceptor_beacon"),
	KONDENZATOR_BASE("kondenzator_base"),
	KONDENZATOR_CENTER("kondenzator_center");

	private ResourceLocation location;
	private List<BakedQuad> quads;
	private IModel model;
	private IBakedModel baked;

	BlockBaker(String name) {
		ModelLocation modelLocation = new ModelLocation("other");
		location = ResourceHelperStatic.getLocation(LibMod.MOD_ID, null, modelLocation, name, "");
	}

	public static void bakeAll() {
		for(BlockBaker bake : BlockBaker.values()) {
			try {
				if(bake.model == null) {
					bake.model = ModelLoaderRegistry.getModel(bake.location);
					bake.bake();
				}
			} catch(Exception e) {
				Solar.LOG.fatal("[Model Bakery] Failed to bake json model: " + bake.getLocation().toString());
				e.printStackTrace();
			}
		}
	}

	void bake() {
		if(model != null) {
			baked = model.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, ModelLoader.defaultTextureGetter());
			quads = baked.getQuads(null, null, 0);
		}
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

	public void render() {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
		for(BakedQuad bakedquad : quads) {
			LightUtil.renderQuadColor(buffer, bakedquad, -1);
		}
		tessellator.draw();
	}
}
