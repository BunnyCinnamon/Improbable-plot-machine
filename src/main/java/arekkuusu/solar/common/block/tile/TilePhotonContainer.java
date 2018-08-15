/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.energy.IPholarized;
import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.block.BlockPhotonContainer;
import arekkuusu.solar.common.block.BlockPhotonContainer.ContainerState;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;

/**
 * Created by <Arekkuusu> on 6/20/2018.
 * It's distributed as part of Solar.
 */
public class TilePhotonContainer extends TileLumenBase implements IPholarized, ITickable {

	public static final int MAX_LUMEN = 64;
	public boolean nou;

	@Override
	public void update() {
		if(world.isRemote) {
			if(hasLumen() && !isLumenOffLazy()) {
				fancyUpdate();
			}
		}
	}

	@Override
	ILumen createHandler() {
		return new LumenHandler(this) {
			@Override
			public int fill(int amount, boolean fill) {
				if(isActiveLazy()) {
					return super.fill(amount, fill);
				} else return amount;
			}

			@Override
			public int drain(int amount, boolean drain) {
				if(isActiveLazy()) {
					return super.drain(amount, drain);
				} else return 0;
			}
		};
	}

	@Override
	public boolean canDrain() {
		return false;
	}

	@Override
	public boolean canFill() {
		return false;
	}

	@Override
	void onLumenChange() {
		if(!world.isRemote) {
			IBlockState state = world.getBlockState(getPos());
			if(hasLumen() && isLumenOffLazy()) {
				world.setBlockState(pos, state.withProperty(BlockPhotonContainer.PROPERTY, ContainerState.LUMEN_ON));
			} else if(!hasLumen()) {
				world.setBlockState(pos, state.withProperty(BlockPhotonContainer.PROPERTY, ContainerState.LUMEN_OFF));
			}
			sync();
		}
	}

	public boolean hasLumen() {
		return handler.get() > 0;
	}

	@Override
	public int getCapacity() {
		return MAX_LUMEN;
	}

	public boolean isActiveLazy() {
		ContainerState state = getStateValue(BlockPhotonContainer.PROPERTY, pos).orElse(ContainerState.INACTIVE);
		return state != ContainerState.INACTIVE;
	}

	public boolean isLumenOffLazy() {
		ContainerState state = getStateValue(BlockPhotonContainer.PROPERTY, pos).orElse(ContainerState.INACTIVE);
		return state == ContainerState.LUMEN_OFF;
	}

	private void fancyUpdate() {
		Vector3 posVec = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5D);
		float diff = (float) handler.get() / (float) handler.getMax();
		float particleScale = 0.5F + 4.5F * diff;
		float particleSpeed = 0.005F + 0.010F * diff;
		for(int i = 0; i < 2 + world.rand.nextInt(3); i++) {
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 12F);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 12F);
			Vector3 speedVec = Vector3.rotateRandom().multiply(particleSpeed * world.rand.nextDouble()).rotate(x.multiply(z));
			Solar.PROXY.spawnLuminescence(world, posVec, speedVec, 160, particleScale, GlowTexture.GLOW);
		}
		Solar.PROXY.spawnLuminescence(world, posVec, Vector3.Zero(), 160, particleScale, GlowTexture.GLOW);
	}
}
