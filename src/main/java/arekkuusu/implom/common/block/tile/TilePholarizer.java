package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.energy.LumenHelper;
import arekkuusu.implom.api.capability.energy.data.ILumen;
import arekkuusu.implom.common.block.BlockPholarizer;
import arekkuusu.implom.common.block.BlockPholarizer.Polarization;
import arekkuusu.implom.common.entity.EntityLumen;
import arekkuusu.implom.common.handler.data.ModCapability;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

@SuppressWarnings("ConstantConditions")
public class TilePholarizer extends TileBase implements ITickable {

	private int cooldown;

	@Override
	public void update() {
		if(!world.isRemote) {
			if(getPolarizationLazy().isPositive()) succ();
			else if(cooldown-- <= 0) {
				cooldown = 20;
				unsucc();
			}
		}
	}

	private void succ() {
		final EnumFacing facing = getFacingLazy();
		getTile(TileEntity.class, world, pos.offset(facing))
				.filter(tile -> tile.hasCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
				.map(tile -> tile.getCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
				.ifPresent(wrapper -> {
					if(wrapper.get() < wrapper.getMax() && wrapper.canFill()) {
						world.getEntitiesWithinAABB(EntityLumen.class, new AxisAlignedBB(pos.up()).grow(10D)).stream().filter(e ->
								!e.isDead && e.hasCapability(ModCapability.LUMEN_CAPABILITY, null)
						).forEach(e -> {
							double x = getPos().getX() + 0.5D - e.posX;
							double y = getPos().getY() + 0.5D - e.posY;
							double z = getPos().getZ() + 0.5D - e.posZ;
							double sqrt = Math.sqrt(x * x + y * y + z * z);
							double effect = sqrt / 10D;
							double strength = (1 - effect) * (1 - effect);
							double power = 0.025D;
							e.motionX += (x / sqrt) * strength * power;
							e.motionY += (y / sqrt) * strength * power;
							e.motionZ += (z / sqrt) * strength * power;
						});
						world.getEntitiesWithinAABB(EntityLumen.class, new AxisAlignedBB(pos.up()).grow(1D)).stream().filter(e ->
								!e.isDead && e.hasCapability(ModCapability.LUMEN_CAPABILITY, null)
						).forEach(e -> {
							ILumen lumen = e.getCapability(ModCapability.LUMEN_CAPABILITY, null);
							LumenHelper.transfer(lumen, wrapper, 1, true);
							if(lumen.get() <= 0) e.setDead();
						});
					}
				});
	}

	private void unsucc() {
		final EnumFacing facing = getFacingLazy();
		getTile(TileEntity.class, world, pos.offset(facing))
				.filter(tile -> tile.hasCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
				.map(tile -> tile.getCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
				.ifPresent(wrapper -> {
					int drain = wrapper.drain(16, true);
					if(drain > 0) {
						EntityLumen lumen = EntityLumen.spawn(world, new Vector3.WrappedVec3i(getPos()).asImmutable().add(0.5D), drain);
						Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 75F);
						Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 75F);
						Vector3 vec = new Vector3.WrappedVec3i(getFacingLazy().getOpposite().getDirectionVec()).asImmutable().rotate(x.multiply(z)).multiply(0.1D);
						lumen.setMotion(vec);
					}
				});
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	public void setPolarizationLazy(Polarization polarization) {
		if(!world.isRemote && getStateValue(BlockPholarizer.POLARIZATION, pos).map(p -> p != polarization).orElse(false)) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockPholarizer.POLARIZATION, polarization));
		}
	}

	public Polarization getPolarizationLazy() {
		return getStateValue(BlockPholarizer.POLARIZATION, pos).orElse(Polarization.POSITIVE);
	}
}
