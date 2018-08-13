package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.energy.IPholarized;
import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import arekkuusu.solar.common.block.BlockPholarizer;
import arekkuusu.solar.common.block.BlockPholarizer.Polarization;
import arekkuusu.solar.common.entity.EntityLumen;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.katsstuff.mirror.data.Quat;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("ConstantConditions") //Shut up
public class TilePholarizer extends TileBase implements ITickable {

	private int cooldown;

	@Override
	public void update() {
		if(!world.isRemote) {
			if(getPolarizationLazy().isPositive()) succ();
			else if(cooldown-- <= 0) {
				cooldown = 20;
				spit();
			}
		}
	}

	private void succ() {
		EnumFacing facing = getFacingLazy();
		BlockPos pos = getPos().offset(facing);
		IBlockState state = world.getBlockState(pos);
		facing = facing.getOpposite();
		TileEntity tile;
		if(state.getBlock().hasTileEntity(state) && (tile = world.getTileEntity(pos)) instanceof IPholarized
				&& tile.hasCapability(ModCapability.NEUTRON_CAPABILITY, facing) && ((IPholarized) tile).canFill()) {
			ILumen wrapper = tile.getCapability(ModCapability.NEUTRON_CAPABILITY, facing);
			if(wrapper.get() < wrapper.getMax()) {
				world.getEntitiesWithinAABB(EntityLumen.class, new AxisAlignedBB(pos.up()).grow(10D)).stream().filter(e ->
						!e.isDead && e.hasCapability(ModCapability.NEUTRON_CAPABILITY, null)
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
						!e.isDead && e.hasCapability(ModCapability.NEUTRON_CAPABILITY, null)
				).forEach(e -> {
					ILumen lumen = e.getCapability(ModCapability.NEUTRON_CAPABILITY, null);
					int drained = lumen.drain(1, false);
					int filled = wrapper.fill(drained, false);
					if(drained > 0 && filled == 0) {
						wrapper.fill(lumen.drain(1, true), true);
					}
					if(lumen.get() <= 0) e.setDead();
				});
			}
		}
	}

	private void spit() {
		EnumFacing facing = getFacingLazy();
		BlockPos pos = getPos().offset(facing);
		IBlockState state = world.getBlockState(pos);
		facing = facing.getOpposite();
		TileEntity tile;
		if(state.getBlock().hasTileEntity(state) && (tile = world.getTileEntity(pos)) instanceof IPholarized
				&& tile.hasCapability(ModCapability.NEUTRON_CAPABILITY, facing) && ((IPholarized) tile).canDrain()) {
			ILumen wrapper = tile.getCapability(ModCapability.NEUTRON_CAPABILITY, facing);
			if(wrapper.get() >= 16) {
				int drain = wrapper.drain(64, true);
				if(drain > 0) {
					EntityLumen lumen = EntityLumen.spawn(world, new Vector3.WrappedVec3i(getPos()).asImmutable().add(0.5D), drain);
					Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 25F);
					Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 25F);
					Vector3 vec = new Vector3.WrappedVec3i(getFacingLazy().getOpposite().getDirectionVec()).asImmutable().rotate(x.multiply(z)).multiply(0.1D);
					lumen.setMotion(vec);
				}
			}
		}
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

	@Override
	void readNBT(NBTTagCompound compound) {

	}

	@Override
	void writeNBT(NBTTagCompound compound) {

	}
}
