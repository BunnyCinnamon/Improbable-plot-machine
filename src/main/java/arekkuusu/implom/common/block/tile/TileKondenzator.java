/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.LumenHelper;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.BlockKondenzator;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.data.ImbuingData;
import arekkuusu.implom.common.handler.data.capability.LumenCapability;
import arekkuusu.implom.common.handler.data.capability.provider.CapabilityProvider;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileKondenzator extends TileBase implements ITickable {

	public final LumenCapability lumenCapability = new LumenCapability(BlockKondenzator.ImbuingConstants.IMBUING_CAPACITY) {
		@Override
		public void set(int lumen) {
			super.set(lumen);
			markDirty();
			sync();
		}
	};
	public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
			.put(Capabilities.LUMEN, lumenCapability)
			.build();

	public boolean add(ItemStack stack) {
		if(!stack.isEmpty() && stack.hasCapability(Capabilities.LUMEN, null)) {
			if(!world.isRemote) {
				LumenHelper.getCapability(stack).ifPresent(lumen ->
						LumenHelper.transfer(lumen, lumenCapability, 10, false)
				);
			} else {
				EnumFacing facing = getFacingLazy().getOpposite();
				Vector3 offset = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable();
				Vector3 pos = new Vector3.WrappedVec3i(getPos()).asImmutable().add(0.5D).offset(offset, -0.25D);
				for(int i = 0; i < 10 + world.rand.nextInt(20); i++) {
					Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 95);
					Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 95);
					double speed = world.rand.nextDouble() * 0.015D;
					Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
							.asImmutable()
							.rotate(x.multiply(z))
							.multiply(speed);
					IPM.getProxy().spawnSpeck(world, pos, speedVec, 75, 1.75F, 0x49FFFF, Light.GLOW, ResourceLibrary.GLOW_PARTICLE);
				}
			}
			return true;
		} else return false;
	}

	@Override
	public void update() {
		if(!world.isRemote && lumenCapability.get() > 0) {
			if(world.getTotalWorldTime() % BlockKondenzator.ImbuingConstants.IMBUING_INCREASE_INTERVAL == 0 && isFacingGlass()) {
				BlockPos pos = getTargetPos();
				ImbuingData.increment(getWorld(), pos, getPos());
				if(ImbuingData.getProgress(getWorld(), pos).timer >= BlockKondenzator.ImbuingConstants.IMBUING_TIME) {
					world.setBlockState(pos, ModBlocks.IMBUED_QUARTZ.getDefaultState());
				}
				reduceLumen();
			}
			if(world.getTotalWorldTime() % BlockKondenzator.ImbuingConstants.IMBUING_DECREASE_INTERVAL == 0 && !isFacingGlass()) {
				reduceLumen();
			}
		}
		if(world.isRemote && lumenCapability.get() > 0 && isFacingGlass()) {
			ImbuingData.Progress progress = ImbuingData.getProgress(getWorld(), getTargetPos());
			int amount = (int) ((float) progress.timer * (0.15F * (1F - (float) progress.getMultiplier() / 6F)));
			for(int i = 0; i < amount + world.rand.nextInt(4); i++) {
				Vector3 posVec = new Vector3.WrappedVec3i(getTargetPos()).asImmutable().add(Math.random(), Math.random(), Math.random());
				IPM.getProxy().spawnSpeck(world, posVec, Vector3.rotateRandom().multiply(0.01D), 45, world.rand.nextFloat(), 0x49FFFF, Light.GLOW, ResourceLibrary.GLOW_PARTICLE);
			}
		}
	}

	private void reduceLumen() {
		if(lumenCapability.get() > 0) lumenCapability.set(lumenCapability.get() - 1);
	}

	private boolean isFacingGlass() {
		IBlockState state = world.getBlockState(pos.offset(getFacingLazy()));
		return state.getMaterial() == Material.GLASS && state.getBlock() != ModBlocks.IMBUED_QUARTZ;
	}

	public BlockPos getTargetPos() {
		return pos.offset(getFacingLazy());
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing)
				? provider.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	/* NBT */
	private static final String NBT_PROVIDER = "provider";
	private static final String NBT_IMBUING_PROGRESS = "imbuing_progress";

	@Override
	void writeSync(NBTTagCompound compound) {
		compound.setTag(NBT_PROVIDER, provider.serializeNBT());
		compound.setInteger(NBT_IMBUING_PROGRESS, ImbuingData.getProgress(getWorld(), getTargetPos()).timer);
	}

	@Override
	void readSync(NBTTagCompound compound) {
		provider.deserializeNBT(compound.getCompoundTag(NBT_PROVIDER));
		ImbuingData.setProgress(getWorld(), getTargetPos(), compound.getInteger(NBT_IMBUING_PROGRESS));
	}
}
