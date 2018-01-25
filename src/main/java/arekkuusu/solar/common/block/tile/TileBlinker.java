/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.relativity.IRelativePower;
import arekkuusu.solar.api.entanglement.relativity.RelativityHandler;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.util.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.Map;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public class TileBlinker extends TileRelativeBase implements IRelativePower, ITickable {

	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.create(0.5D, 0.2D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.create(0.5D, 0.8D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.create(0.5D, 0.5D, 0.8D))
			.put(EnumFacing.SOUTH, Vector3.create(0.5D, 0.5D, 0.2D))
			.put(EnumFacing.EAST, Vector3.create(0.2D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.create(0.8D, 0.5D, 0.5D))
			.build();
	private int tick;

	@Override
	public void update() {
		if(world.isRemote && tick++ % 4 == 0 && world.rand.nextBoolean()) {
			EnumFacing facing = getFacing();
			Vector3 back = getOffSet(facing.getOpposite());
			double speed = world.rand.nextDouble() * -0.01D;
			Vector3 vec = Vector3.create(facing).multiply(speed);
			ParticleUtil.spawnLightParticle(world, back, vec, isPoweredLazy() ? 0x49FFFF : 0xFFFFFF, 60, 2.5F);
		}
	}

	@Override
	public void onPowerUpdate() {
		if(!world.isRemote) {
			IBlockState state = world.getBlockState(pos);
			world.scheduleUpdate(getPos(), state.getBlock(), 0);
		}
	}

	public int getRedstonePower() {
		int power = 0;
		for(EnumFacing facing : EnumFacing.values()) {
			int detected = world.getRedstonePower(pos.offset(facing), facing);
			if(detected > power) power = detected;
		}
		return power;
	}

	private boolean isPoweredLazy() {
		return getStateValue(State.ACTIVE, pos).orElse(false);
	}

	private EnumFacing getFacing() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	private Vector3 getOffSet(EnumFacing facing) {
		return FACING_MAP.get(facing).copy().add(pos);
	}

	@Override
	public void add() {
		if(!world.isRemote) {
			RelativityHandler.addRelative(this, () -> {
				if(world.isBlockPowered(getPos())) {
					RelativityHandler.setPower(this, getRedstonePower());
				} else {
					onPowerUpdate();
				}
			});
		}
	}

	@Override
	public void remove() {
		if(!world.isRemote) {
			RelativityHandler.removeRelative(this, () -> {
				if(world.isBlockPowered(getPos())) {
					RelativityHandler.setPower(this, 0);
				}
			});
		}
	}
}
