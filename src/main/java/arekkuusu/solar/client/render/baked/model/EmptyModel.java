/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.render.baked.model;

import arekkuusu.solar.client.render.baked.EmptyBakedModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class EmptyModel implements IModel {

	private static final String RAW_MODEL = "{    'elements': [        {   'from': [0, 0, 0],            'to': [16, 16, 16],            'faces': {                'down': {'uv': [0, 0, 16, 16], 'texture': '' }            }        }    ]}".replaceAll("'", "\"");
	private static final ModelBlock MODEL_BLOCK = ModelBlock.deserialize(RAW_MODEL);
	private static final IModelState MODEL_STATE = part -> Optional.empty();

	static {
		MODEL_BLOCK.name = "EmptyModel";
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return Collections.emptyList();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, java.util.function.Function<ResourceLocation, TextureAtlasSprite> getter) {
		return new EmptyBakedModel(MODEL_BLOCK.getAllTransforms());

	}

	@Override
	public IModelState getDefaultState() {
		return MODEL_STATE;
	}
}
