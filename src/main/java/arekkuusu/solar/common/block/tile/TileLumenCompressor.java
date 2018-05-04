/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.client.effect.FXUtil;
import net.katsstuff.mirror.client.particles.GlowTexture;
import net.katsstuff.mirror.data.Quat;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * Created by <Arekkuusu> on 5/3/2018.
 * It's distributed as part of Solar.
 */
public class TileLumenCompressor extends TileLumenBase implements ITickable {

	public static final int MAX_LUMEN = 64;
	private boolean isEmpty = true;

	public TileLumenCompressor() {
		super(MAX_LUMEN);
	}

	@Override
	public void update() {
		if(world.isRemote) {
			if(handler.get() > 0) {
				if(isEmpty) {
					fancyStart();
					isEmpty = false;
				} else fancyUpdate();
			}
		}
	}

	private void fancyStart() {
		EnumFacing facing = getFacingLazy().getOpposite();
		Vector3 posVec = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable()
				.add(pos.getX(), pos.getY() + 0.15D, pos.getZ()).add(0.5D);
		for(int i = 0; i < 6 + world.rand.nextInt(4); i++) {
			float particleScale = 0.2F + 3F * world.rand.nextFloat();
			Vector3 speedVec = Vector3.rotateRandom().multiply(0.005D + 0.01D * world.rand.nextDouble());
			FXUtil.spawnLumen(world, posVec, speedVec, 35, particleScale, GlowTexture.STAR);
		}
	}

	private void fancyUpdate() {
		EnumFacing facing = getFacingLazy().getOpposite();
		Vector3 posVec = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable()
				.add(pos.getX(), pos.getY() + 0.15D, pos.getZ()).add(0.5D);
		float particleScale = 0.5F + 5F * ((float) handler.get() / (float) handler.getMax());
		for(int i = 0; i < 2 + world.rand.nextInt(3); i++) {
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 12F);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 12F);
			Vector3 speedVec = Vector3.rotateRandom().multiply(0.015D * world.rand.nextDouble()).rotate(x.multiply(z));
			FXUtil.spawnLumen(world, posVec, speedVec, 160, particleScale, GlowTexture.GLOW);
		}
		FXUtil.spawnLumen(world, posVec, Vector3.Zero(), 160, particleScale, GlowTexture.GLOW);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	void onLumenChange() {
		if(world != null && !world.isRemote) {
			updatePosition(world, pos);
		}
	}
}
