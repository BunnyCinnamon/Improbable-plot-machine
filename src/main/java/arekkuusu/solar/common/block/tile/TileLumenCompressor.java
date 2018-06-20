/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.common.block.ModBlocks;
import net.katsstuff.mirror.client.particles.GlowTexture;
import net.katsstuff.mirror.data.Quat;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * Created by <Arekkuusu> on 5/3/2018.
 * It's distributed as part of Solar.
 */
public class TileLumenCompressor extends TileLumenBase implements ITickable {

	public static final int MAX_LUMEN = 64;

	@Override
	public void update() {
		if(world.isRemote && hasContainer()) {
			if(hasLumen()) {
				if(isContainerEmpty()) {
					fancyStart();
				} else fancyUpdate();
			}
		}
	}

	@Override
	void onLumenChange() {
		if(hasContainer()) {
			BlockPos pos = getPos().offset(getFacingLazy().getOpposite());
			IBlockState state = world.getBlockState(pos);
			if(hasLumen() && isContainerEmpty()) {
				world.setBlockState(pos, state.withProperty(State.ACTIVE, true));
			} else if(!hasLumen()) {
				world.setBlockState(pos, state.withProperty(State.ACTIVE, false));
			}
		}
		sync();
	}

	public boolean hasLumen() {
		return handler.get() > 0;
	}

	public boolean hasContainer() {
		BlockPos pos = getPos().offset(getFacingLazy().getOpposite());
		IBlockState state = world.getBlockState(pos);
		return state.getBlock() == ModBlocks.PHOTON_CONTAINER;
	}

	public boolean isContainerEmpty() {
		return !getStateValue(State.ACTIVE, pos.offset(getFacingLazy().getOpposite())).orElse(false);
	}

	private void fancyStart() {
		EnumFacing facing = getFacingLazy().getOpposite();
		Vector3 facingVec = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable();
		Vector3 posVec = facingVec.add(pos.getX(), pos.getY(), pos.getZ()).add(0.5D);
		for(int i = 0; i < 6 + world.rand.nextInt(4); i++) {
			float particleScale = 0.2F + 3F * world.rand.nextFloat();
			Vector3 speedVec = Vector3.rotateRandom().multiply(0.005D + 0.01D * world.rand.nextDouble());
			FXUtil.spawnLumen(world, posVec, speedVec, 35, particleScale, GlowTexture.STAR);
		}
	}

	private void fancyUpdate() {
		EnumFacing facing = getFacingLazy().getOpposite();
		Vector3 facingVec = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable();
		Vector3 posVec = facingVec.add(pos.getX(), pos.getY(), pos.getZ()).add(0.5D);
		float diff = (float) handler.get() / (float) handler.getMax();
		float particleScale = 0.5F + 4.5F * diff;
		float particleSpeed = 0.005F + 0.010F * diff;
		for(int i = 0; i < 2 + world.rand.nextInt(3); i++) {
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 12F);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 12F);
			Vector3 speedVec = Vector3.rotateRandom().multiply(particleSpeed * world.rand.nextDouble()).rotate(x.multiply(z));
			FXUtil.spawnLumen(world, posVec, speedVec, 160, particleScale, GlowTexture.GLOW);
		}
		FXUtil.spawnLumen(world, posVec, Vector3.Zero(), 160, particleScale, GlowTexture.GLOW);
	}

	@Override
	ILumen createHandler() {
		return new LumenHandler(this) {
			@Override
			public int fill(int amount) {
				return hasContainer() ? super.fill(amount) : amount;
			}

			@Override
			public int drain(int amount) {
				return 0;
			}
		};
	}

	@Override
	public int getCapacity() {
		return MAX_LUMEN;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
