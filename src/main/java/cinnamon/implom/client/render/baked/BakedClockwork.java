package cinnamon.implom.client.render.baked;

import cinnamon.implom.client.render.model.ModelClockwork;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.BakedItemModel;

public class BakedClockwork extends BakedItemModel {

	public final ModelClockwork parent;
	public final VertexFormat format;

	public BakedClockwork(ModelClockwork parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, ImmutableMap<ItemTransforms.TransformType, Transformation> transforms, ItemOverrides overrides, VertexFormat format, boolean untransformed, boolean isSideLit) {
		super(quads, particle, transforms, overrides, untransformed, isSideLit);
		this.parent = parent;
		this.format = format;
	}
}
