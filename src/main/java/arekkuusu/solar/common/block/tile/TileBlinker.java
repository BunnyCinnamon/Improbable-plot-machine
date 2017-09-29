/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.state.Power;
import arekkuusu.solar.client.effect.ParticleUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public class TileBlinker extends TileRelativeBase<TileBlinker> implements ITickable {

	private static final Map<EnumFacing, Vec3d> FACING_MAP = ImmutableMap.<EnumFacing, Vec3d>builder()
			.put(EnumFacing.UP, new Vec3d(0.5D, 0.2D, 0.5D))
			.put(EnumFacing.DOWN, new Vec3d(0.5D, 0.8D, 0.5D))
			.put(EnumFacing.NORTH, new Vec3d(0.5D, 0.5D, 0.8D))
			.put(EnumFacing.SOUTH, new Vec3d(0.5D, 0.5D, 0.2D))
			.put(EnumFacing.EAST, new Vec3d(0.2D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, new Vec3d(0.8D, 0.5D, 0.5D))
			.build();
	private static final Map<UUID, Integer> POWER_MAP = Maps.newHashMap();
	private int tick;

	/**
	 * If the relative {@param tile} is powered by Redstone.
	 *
	 * @param tile The relative tile {@link TileBlinker}.
	 * @return If the redstone level is higher than 0
	 */
	public static boolean isPowered(TileBlinker tile) {
		return getPower(tile) > 0;
	}

	/**
	 * Get the Redstone power from the relative {@param tile}.
	 *
	 * @param tile The relative tile {@link TileBlinker}.
	 * @return The Redstone power from 0 to 15.
	 */
	public static int getPower(TileBlinker tile) {
		return POWER_MAP.getOrDefault(tile.getKey().orElse(null), 0);
	}

	/**
	 * Set the Redstone power to the relative {@param tile},
	 * this will update all other relative tiles, as long as
	 * they are loaded in the world.
	 *
	 * @param tile The relative tile {@link TileBlinker}.
	 * @param newPower The new redstone power.
	 */
	public static void setPower(TileBlinker tile, int newPower) {
		tile.getKey().ifPresent(uuid -> POWER_MAP.compute(uuid, (key, prevPower) -> {
			if(prevPower == null || prevPower != newPower) updateAll(key);
			return newPower > 0 ? newPower : null;
		}));
	}

	/**
	 * Updates all relative {@link TileBlinker} entangled
	 * by a shared {@param uuid} linking to a single Redstone
	 * power value.
	 *
	 * @param uuid The {@link UUID} entangling them together.
	 */
	private static void updateAll(UUID uuid) {
		SolarApi.getRelativityMap().computeIfPresent(uuid, (key, list) -> {
			list.stream()
					.filter(tile -> tile.isLoaded() && tile instanceof TileBlinker)
					.map(tile -> (TileBlinker) tile).forEach(TileBlinker::updateRelativity);
			return list;
		});
	}

	@Override
	public void update() {
		if(world.isRemote && tick++ % 4 == 0 && world.rand.nextBoolean()) {
			EnumFacing facing = getFacing();
			Vec3d back = getOffSet(facing.getOpposite());

			double speed = world.rand.nextDouble() * -0.01D;
			Vec3d vec = new Vec3d(facing.getFrontOffsetX() * speed, facing.getFrontOffsetY() * speed, facing.getFrontOffsetZ() * speed);

			ParticleUtil.spawnLightParticle(world, back.x, back.y, back.z, vec.x, vec.y, vec.z, isPoweredLazy() ? 0x49FFFF : 0xFFFFFF, 60, 2.5F);
		}
	}

	public void updateRelativity() {
		IBlockState state = world.getBlockState(pos);
		world.scheduleUpdate(getPos(), state.getBlock(), 0);
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
		return getState(Power.POWER).orElse(Power.OFF) == Power.ON;
	}

	private EnumFacing getFacing() {
		return getState(BlockDirectional.FACING).orElse(EnumFacing.UP);
	}

	private Vec3d getOffSet(EnumFacing facing) {
		return FACING_MAP.get(facing).addVector(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	void onUnload() {
		//Fuck, I left the oven on in the n'th dimension
	}

	@Override
	void onAdd() {
		if(world.isBlockPowered(getPos())) {
			TileBlinker.setPower(this, getRedstonePower());
		} else {
			updateRelativity();
		}
	}

	@Override
	void onRemove() {
		if(world.isBlockPowered(getPos())) {
			TileBlinker.setPower(this, 0);
		}
	}
}
