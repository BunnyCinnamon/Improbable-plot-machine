/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.energy.data.ILumen;
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
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 4/9/2018.
 * It's distributed as part of Solar.
 */
public class TileLuminicMechanism extends TileBase implements ITickable {

	public static final int MAX_LUMEN = 16;
	private final ILumen handler;
	private int tick;

	public TileLuminicMechanism() {
		handler = new LuminicMechanismLumenHandler(this);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			if(handler.get() < handler.getMax() && tick++ % 20 == 0){
				BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(getPos());
				EnumFacing facing = getFacingLazy();
				for(int i = 0; i < 2; i++) {
					IBlockState state = world.getBlockState(pos.move(facing));
					if(state.getBlock().hasTileEntity(state)) {
						TileEntity tile = world.getTileEntity(pos);
						if(tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())) {
							IFluidHandler fluid = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
							FluidStack stack = fluid != null ? fluid.drain(500, false) : null;
							if(stack != null && stack.amount == 500) {
								fluid.drain(500, true);
								handler.fill(1);
							}
						}
					}
				}
			}
			spit();
		}
	}

	private void spit() {
		if(handler.get() >= 16) {
			EntityLumen lumen = EntityLumen.spawn(world, new Vector3.WrappedVec3i(getPos()).asImmutable().add(0.5D), handler.drain(16));
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 25F);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 25F);
			Vector3 vec = new Vector3.WrappedVec3i(getFacingLazy().getOpposite().getDirectionVec()).asImmutable().rotate(x.multiply(z)).multiply(0.1D);
			lumen.motionX = vec.x();
			lumen.motionY = vec.y();
			lumen.motionZ = vec.z();
		}
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY
				? ModCapability.LUMEN_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		handler.set(compound.getInteger("lumen"));
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setInteger("lumen", handler.get());
	}

	private static class LuminicMechanismLumenHandler implements ILumen {

		private final TileLuminicMechanism tile;
		private int lumen;

		private LuminicMechanismLumenHandler(TileLuminicMechanism tile) {
			this.tile = tile;
		}

		@Override
		public int get() {
			return lumen;
		}

		@Override
		public void set(int neutrons) {
			this.lumen = neutrons;
			tile.markDirty();
		}

		@Override
		public int drain(int amount) {
			if(amount > 0) {
				int contained = get();
				int drained = amount < getMax() ? amount : getMax();
				int remain = contained;
				int removed = remain < drained ? contained : drained;
				remain -= removed;
				set(remain);
				return removed;
			} else return 0;
		}

		@Override
		public int fill(int amount) {
			if(amount > 0) {
				int contained = get();
				if(contained >= getMax()) return amount;
				int sum = contained + amount;
				int remain = 0;
				if(sum > getMax()) {
					remain = sum - getMax();
					sum = getMax();
				}
				set(sum);
				return remain;
			} else return 0;
		}

		@Override
		public int getMax() {
			return MAX_LUMEN;
		}
	}
}
