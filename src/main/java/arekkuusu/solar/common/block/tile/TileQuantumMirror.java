/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.quantum.data.QuantumTileWrapper;
import arekkuusu.solar.client.effect.FXUtil;
import net.katsstuff.mirror.client.particles.GlowTexture;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.util.ITickable;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public class TileQuantumMirror extends TileQuantumBase<QuantumTileWrapper> implements ITickable {

	public static final int SLOTS = 1;

	@Override
	public QuantumTileWrapper createHandler() {
		return new QuantumTileWrapper<>(this, SLOTS);
	}

	@Override
	public void update() {
		if(world.isRemote && world.rand.nextInt(10) == 0) {
			Vector3 from = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			FXUtil.spawnMute(world, from, Vector3.rotateRandom().multiply(0.1F), 20, 0.1F, 0XFFFFFF, GlowTexture.STAR);
		}
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
}
