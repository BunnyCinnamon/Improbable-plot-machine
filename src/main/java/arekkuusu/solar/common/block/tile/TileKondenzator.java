/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.energy.data.ILumen;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.block.BlockKondenzator;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Solar.
 */
public class TileKondenzator extends TileSimpleLumenBase implements ITickable {

	@Override
	public void update() {
		if(!world.isRemote && handler.get() > 0) {
			if(world.getTotalWorldTime() % 20 == 0 && isFacingGlass()) {
				BlockPos pos = getProgressPos();
				if(BlockKondenzator.setProgress(this, BlockKondenzator.getProgress(pos).getTimer() + 1).getTimer() >= 100) {
					world.setBlockState(pos, ModBlocks.IMBUED_QUARTZ.getDefaultState());
					BlockKondenzator.setProgress(this, 0);
				}
				handler.set(handler.get() - 1);
			}
			if(world.getTotalWorldTime() % 60 == 0 && !isFacingGlass()) {
				handler.set(handler.get() - 1);
			}
		}
		if(world.isRemote && handler.get() > 0) {
			if(world.getTotalWorldTime() % 5 == 0) {
				EnumFacing facing = getFacingLazy().getOpposite();
				Vector3 offset = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable();
				Vector3 pos = new Vector3.WrappedVec3i(getPos()).asImmutable().add(0.5D).offset(offset, -0.25D);
				for(int i = 0; i < (handler.get() / handler.getMax()) * 4 + world.rand.nextInt(5); i++) {
					Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 45);
					Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 45);
					double speed = world.rand.nextDouble() * 0.015D;
					Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
							.asImmutable()
							.rotate(x.multiply(z))
							.multiply(speed);
					Solar.PROXY.spawnMute(world, pos, speedVec, 75, 1.75F, 0x49FFFF, Light.GLOW);
				}
			}
			if(isFacingGlass()) {
				BlockKondenzator.Progress progress = BlockKondenzator.getProgress(getProgressPos());
				int amount = (int) ((float) progress.getTimer() * (0.15F * (1F - (float) progress.getMultiplier() / 6F)));
				for(int i = 0; i < amount + world.rand.nextInt(4); i++) {
					Vector3 posVec = new Vector3.WrappedVec3i(getProgressPos()).asImmutable().add(Math.random(), Math.random(), Math.random());
					Solar.PROXY.spawnSpeck(world, posVec, Vector3.rotateRandom().multiply(0.01D), 45, world.rand.nextFloat(), 0x49FFFF, GlowTexture.GLOW);
				}
			}
		}
	}

	private boolean isFacingGlass() {
		IBlockState state = world.getBlockState(pos.offset(getFacingLazy()));
		return state.getMaterial() == Material.GLASS && state.getBlock() != ModBlocks.IMBUED_QUARTZ;
	}

	public boolean add(ItemStack stack) {
		if(!stack.isEmpty() && stack.hasCapability(ModCapability.NEUTRON_CAPABILITY, null)) {
			if(!world.isRemote) {
				ILumen lumen = stack.getCapability(ModCapability.NEUTRON_CAPABILITY, null);
				assert lumen != null;
				int missing = BlockKondenzator.MAX_LUMEN - handler.get();
				if(missing > 0 && lumen.drain(missing, false) > 0) {
					handler.fill(lumen.drain(missing, true), true);
				}
			}
			return true;
		} else return false;
	}

	public BlockPos getProgressPos() {
		return pos.offset(getFacingLazy());
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	void writeSync(NBTTagCompound compound) {
		compound.setInteger(ILumen.NBT_TAG, handler.get());
		compound.setInteger("mutation_progress", BlockKondenzator.getProgress(getProgressPos()).getTimer());
	}

	@Override
	void readSync(NBTTagCompound compound) {
		handler.set(compound.getInteger(ILumen.NBT_TAG));
		BlockKondenzator.setProgress(this, compound.getInteger("mutation_progress"));
	}

	@Override
	void onLumenChange() {
		if(world != null && !world.isRemote) sync();
		if(handler.get() < 0) BlockKondenzator.setProgress(this, 0);
	}

	@Override
	public int getCapacity() {
		return BlockKondenzator.MAX_LUMEN;
	}

	@SideOnly(Side.CLIENT)
	public int getLumen() {
		return handler.get();
	}
}
