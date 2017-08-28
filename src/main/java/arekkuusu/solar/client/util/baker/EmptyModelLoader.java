/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.util.baker;

import arekkuusu.solar.client.render.baked.model.EmptyModel;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class EmptyModelLoader implements ICustomModelLoader {

	@Override
	public boolean accepts(ResourceLocation location) {
		return EmptyRenderRegistry.isRegistered(trim(location));
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return new EmptyModel();
	}

	private ResourceLocation trim(ResourceLocation location) {
		return new ResourceLocation(location.getResourceDomain(), location.getResourcePath());
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		//NO-OP
	}
}
