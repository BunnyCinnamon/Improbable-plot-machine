/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.handler.data.QuantumTileWrapper;
import net.minecraft.util.ITickable;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public class TileQuantumMirror extends TileQuantumBase<QuantumTileWrapper> implements ITickable {

	public int tick;

	@Override
	public QuantumTileWrapper createHandler() {
		return new QuantumTileWrapper<>(this, 1);
	}

	@Override
	public void update() {
		if(world.isRemote && world.rand.nextInt(10) == 0) {
			Vector3 from = new Vector3(pos).add(0.5D, 0.5D, 0.5D);
			Vector3 to = Vector3.getRandomVec(1F).add(from);

			ParticleUtil.spawnQuorn(world, from, 0.05D, to, 0.1F, 0XFFFFFF);
		}
		++tick;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
}
