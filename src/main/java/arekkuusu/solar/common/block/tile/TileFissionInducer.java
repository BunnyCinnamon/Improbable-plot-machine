/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.block.fluid.ModFluids;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 4/5/2018.
 * It's distributed as part of Solar.
 */
public class TileFissionInducer extends TileBase implements ITickable {

	public static final int MAX_CAPACITY = 10000;
	private final FluidTank fluidHandler;
	private BlockPos from;
	private BlockPos to;
	private int tick;

	public TileFissionInducer() {
		fluidHandler = new FluidTank(new FluidStack(ModFluids.GOLD, 0), MAX_CAPACITY);
	}

	@Override
	public void onLoad() {
		from = pos.add(-1,-1,-1);
		to = pos.add(1,1,1);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			if(tick++ % 20 == 0 && canFill()) {
				BlockPos.getAllInBox(from, to).forEach(p -> {
					if(world.rand.nextDouble() < 0.01D) {
						IBlockState state = world.getBlockState(p);
						if(state.getBlock() == Blocks.GOLD_BLOCK) {
							world.setBlockState(p, ModBlocks.MOLTEN_GOLD.getDefaultState());
						} else if(state.getBlock() == ModBlocks.MOLTEN_GOLD) {
							FluidStack stack = new FluidStack(ModFluids.GOLD, Fluid.BUCKET_VOLUME);
							((IFluidBlock) ModFluids.GOLD.getBlock()).place(world, p, stack, true);
						} else if(state.getBlock() instanceof IFluidBlock) {
							IFluidBlock f = (IFluidBlock) state.getBlock();
							if(f.getFluid() == ModFluids.GOLD && f.canDrain(world, p)) {
								if(fluidHandler.fill(f.drain(world, p, true), true) == Fluid.BUCKET_VOLUME) {
									world.setBlockToAir(p);
								}
							}
						}
					}
				});
			}
			applyGravity();
		} else if(tick++ % 5 == 0) {
			BlockPos.getAllInBox(from, to).forEach(p -> {
				if(world.rand.nextFloat() < 0.4F) {
					IBlockState state = world.getBlockState(p);
					if(state.getBlock() == Blocks.GOLD_BLOCK || state.getBlock() == ModBlocks.MOLTEN_GOLD) {
						Vector3 pos = Vector3.randomVector().add(p.getX(), p.getY(), p.getZ());
						int color = world.rand.nextBoolean() ? 0xff5000 : 0xfff200;
						FXUtil.spawnLight(world, pos, Vector3.Zero(), 10, 0.2F, color, Light.GLOW);
					}
				}
			});
		}
	}

	private void applyGravity() {
		world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getPos().up()).grow(5)).forEach(e -> {
			double x = getPos().getX() + 0.5D - e.posX;
			double y = getPos().getY() + 0.5D - e.posY;
			double z = getPos().getZ() + 0.5D - e.posZ;
			double sqrt = Math.sqrt(x * x + y * y + z * z);
			double effect = sqrt / 5D;
			double strength = (1 - effect) * (1 - effect);
			double power = 0.005D;
			e.motionX += (x / sqrt) * strength * power;
			e.motionY += (y / sqrt) * strength * power;
			e.motionZ += (z / sqrt) * strength * power;
		});
	}

	public boolean canFill() {
		return fluidHandler.canFill() && fluidHandler.getFluidAmount() < fluidHandler.getCapacity();
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.DOWN);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		EnumFacing f = getFacingLazy();
		return (f == facing || f.getOpposite() == facing) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		EnumFacing f = getFacingLazy();
		return (f == facing || f.getOpposite() == facing) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler)
				: super.getCapability(capability, facing);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		fluidHandler.readFromNBT(compound);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		fluidHandler.writeToNBT(compound);
	}
}
