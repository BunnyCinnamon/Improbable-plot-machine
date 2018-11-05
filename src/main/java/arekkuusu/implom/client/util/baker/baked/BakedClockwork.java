package arekkuusu.implom.client.util.baker.baked;

import arekkuusu.implom.client.util.baker.model.ModelClockwork;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class BakedClockwork extends BakedItemModel {

	public final ModelClockwork parent;
	public final VertexFormat format;

	public BakedClockwork(ModelClockwork parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, ItemOverrideList overrides, VertexFormat format) {
		super(quads, particle, transforms, overrides);
		this.parent = parent;
		this.format = format;
	}

	public ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms() {
		return transforms;
	}
}
