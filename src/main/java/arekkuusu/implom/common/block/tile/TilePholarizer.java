package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.common.block.BlockPholarizer;
import arekkuusu.implom.common.block.BlockPholarizer.Polarization;
import net.minecraft.block.BlockDirectional;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

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
				.ifPresent(wrapper -> {

				});
	}

	private void unsucc() {
		final EnumFacing facing = getFacingLazy();
		getTile(TileEntity.class, world, pos.offset(facing))
				.ifPresent(wrapper -> {

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
