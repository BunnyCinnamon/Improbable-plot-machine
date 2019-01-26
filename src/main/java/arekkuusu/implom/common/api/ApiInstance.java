package arekkuusu.implom.common.api;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.common.handler.data.WorldNBTData;
import net.minecraft.world.World;

public class ApiInstance extends IPMApi {

	public WorldNBTData data;

	@Override
	public void loadWorld(World world) {
		if(data == null) {
			data = WorldNBTData.get(world);
		}
	}

	@Override
	public void unloadWorld() {
		dataMap.clear();
		data = null;
	}

	@Override
	public void markWorldDirty() {
		data.markDirty();
	}
}
