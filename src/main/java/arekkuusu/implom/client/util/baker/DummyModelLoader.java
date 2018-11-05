/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.baker;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class DummyModelLoader implements ICustomModelLoader {

	public static final DummyModelLoader INSTANCE = new DummyModelLoader();

	@Override
	public boolean accepts(ResourceLocation location) {
		return DummyModelRegistry.isRegistered(trim(location));
	}

	@Override
	public IModel loadModel(ResourceLocation location) {
		return DummyModelRegistry.getModel(trim(location));
	}

	private ResourceLocation trim(ResourceLocation location) {
		return new ResourceLocation(location.getResourceDomain(), location.getResourcePath());
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager) {
		try {
			ModelLoaderRegistry.getMissingModel(); //RIP
			BlockBaker.bakeAll();
		} catch (Exception ignored) {
			//How is this... fired at startup?
		}
	}
}
