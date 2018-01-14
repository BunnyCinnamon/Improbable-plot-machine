/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker;

import arekkuusu.solar.client.util.baker.model.DummyModel;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class DummyModelLoader implements ICustomModelLoader {

	private final Map<ResourceLocation, IModel> created = Maps.newHashMap();

	@Override
	public boolean accepts(ResourceLocation location) {
		return DummyBakedRegistry.isRegistered(trim(location));
	}

	@Override
	public IModel loadModel(ResourceLocation location) throws Exception {
		return created.computeIfAbsent(trim(location), DummyModel::new);
	}

	private ResourceLocation trim(ResourceLocation location) {
		return new ResourceLocation(location.getResourceDomain(), location.getResourcePath());
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		//NO-OP
	}
}
