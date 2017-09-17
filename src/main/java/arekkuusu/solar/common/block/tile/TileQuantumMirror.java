/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

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
			double x = pos.getX() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double y = pos.getY() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double z = pos.getZ() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double speed = 0.05D;

			ParticleUtil.spawnQuorn(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, speed, x, y, z, 0.1F, 0XFFFFFF);
		}
		++tick;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
}
