/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.entanglement.relativity.IRelativeTile;
import arekkuusu.solar.api.entanglement.relativity.RelativityHandler;
import arekkuusu.solar.client.effect.ParticleUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public class TileBlinker extends TileBase implements ITickable, IRelativeTile {

	private static final Map<EnumFacing, Vec3d> FACING_MAP = ImmutableMap.<EnumFacing, Vec3d>builder()
			.put(EnumFacing.UP, new Vec3d(0.5D, 0.1D, 0.5D))
			.put(EnumFacing.DOWN, new Vec3d(0.5D, 0.9D, 0.5D))
			.put(EnumFacing.NORTH, new Vec3d(0.5D, 0.5D, 0.9D))
			.put(EnumFacing.SOUTH, new Vec3d(0.5D, 0.5D, 0.1D))
			.put(EnumFacing.EAST, new Vec3d(0.1D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, new Vec3d(0.9D, 0.5D, 0.5D))
			.build();
	private static final Map<UUID, Integer> POWER_MAP = new HashMap<>();
	private UUID key;
	private int tick;

	public static boolean isPowered(TileBlinker tile) {
		return getPower(tile) > 0;
	}

	public static int getPower(TileBlinker tile) {
		return POWER_MAP.getOrDefault(tile.getKey().orElse(null), 0);
	}

	public static void setPower(TileBlinker tile, int newPower) {
		tile.getKey().ifPresent(uuid -> POWER_MAP.compute(uuid, (key, prevPower) -> {
			if(prevPower == null || prevPower != newPower) updateAll(key);
			return newPower > 0 ? newPower : null;
		}));
	}

	private static void updateAll(UUID uuid) {
		SolarApi.getRelativityMap().get(uuid)
				.stream().filter(tile -> tile.isLoaded() && tile instanceof TileBlinker )
				.map(tile -> (TileBlinker) tile ).forEach(TileBlinker::updateState);
	}

	private void updateState() {
		IBlockState state = getWorld().getBlockState(getPos());
		getWorld().scheduleUpdate(getPos(), state.getBlock(), 0);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			add();
		} else if(tick++ % 4 == 0 && world.rand.nextBoolean()) {
			EnumFacing facing = getFacing();
			Vec3d back = getOffSet(facing.getOpposite());

			double speed = world.rand.nextDouble() * -0.01D;
			Vec3d vec = new Vec3d(facing.getFrontOffsetX() * speed, facing.getFrontOffsetY() * speed, facing.getFrontOffsetZ() * speed);

			ParticleUtil.spawnLightParticle(world, back.x, back.y, back.z, vec.x, vec.y, vec.z, 0xFFFFFF, 30, 2F);
		}
	}

	private EnumFacing getFacing() {
		IBlockState here = world.getBlockState(pos);
		return here.getValue(BlockDirectional.FACING);
	}

	private Vec3d getOffSet(EnumFacing facing) {
		return FACING_MAP.get(facing).addVector(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void add() {
		RelativityHandler.addRelative(this, tile -> {
			if(tile.world.isBlockPowered(tile.getPos())) {
				TileBlinker.setPower(tile, getRedstonePower());
			} else {
				tile.updateState();
			}
		});
	}

	@Override
	public void remove() {
		RelativityHandler.removeRelative(this, tile -> {
			if(tile.world.isBlockPowered(tile.getPos())) {
				TileBlinker.setPower(tile, 0);
			}
		});
	}

	public int getRedstonePower() {
		int power = 0;
		for(EnumFacing facing : EnumFacing.values()) {
			int detected = world.getRedstonePower(pos.offset(facing), facing);
			if(detected > power) power = detected;
		}
		return power;
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		if(key == null) remove();
		this.key = key;
		updateState();
	}

	@Override
	void readNBT(NBTTagCompound cmp) {
		if(cmp.hasUniqueId("uuid_key")) {
			key = cmp.getUniqueId("uuid_key");
		}
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
		if(key != null) {
			cmp.setUniqueId("uuid_key", key);
		}
	}
}
