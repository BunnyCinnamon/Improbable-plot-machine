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
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created by <Arekkuusu> on 23/12/2017.
 * It's distributed as part of Solar.
 */
public class TileQimranut extends TileSimpleLinkBase implements ITickable {

	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.create(0.5D, 0.4D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.create(0.5D, 0.6D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.create(0.5D, 0.5D, 0.6D))
			.put(EnumFacing.SOUTH, Vector3.create(0.5D, 0.5D, 0.4D))
			.put(EnumFacing.EAST, Vector3.create(0.4D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.create(0.6D, 0.5D, 0.5D))
			.build();

	@Override
	public void update() {
		if(world.isRemote) {
			EnumFacing facing = getFacingLazy();
			if(world.getTotalWorldTime() % 20 == 0 && world.rand.nextBoolean()) {
				Vector3 back = getOffSet(facing.getOpposite());
				double speed = -0.010D - world.rand.nextDouble() * 0.010D;
				Vector3 vec = Vector3.create(facing)
						.multiply(speed)
						.rotatePitchX((world.rand.nextFloat() * 2 - 1) * 0.25F)
						.rotatePitchZ((world.rand.nextFloat() * 2 - 1) * 0.25F);
				ParticleUtil.spawnNeutronBlast(world, back, vec, 0x000000, 60, 0.1F, true);
			} else if(world.getTotalWorldTime() % 2 == 0) {
				Vector3 back = getOffSet(facing.getOpposite());
				double speed = world.rand.nextDouble() * -0.03D;
				Vector3 vec = Vector3.create(facing).multiply(speed);
				ParticleUtil.spawnDarkParticle(world, back, vec, 0x000000, 100, 2.5F);
			}
		}
	}

	@Nullable
	public <T> T accessCapability(Capability<T> capability) {
		BlockPos offset = pos.offset(getFacingLazy());
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock().hasTileEntity(state)) {
			TileEntity tile = world.getTileEntity(offset);
			return tile != null && !(tile instanceof TileQimranut) ? tile.getCapability(capability, getFacingLazy()) : null;
		}
		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getInverse().filter(t -> t.isLoaded() && t instanceof TileQimranut)
				.map(t -> ((TileQimranut) t).accessCapability(capability) != null)
				.orElse(super.hasCapability(capability, facing)) : false;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getInverse().filter(t -> t.isLoaded() && t instanceof TileQimranut)
				.map(t -> ((TileQimranut) t).accessCapability(capability))
				.orElse(super.getCapability(capability, facing)) : null;
	}

	private EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	private Vector3 getOffSet(EnumFacing facing) {
		return FACING_MAP.get(facing).copy().add(pos);
	}
}
