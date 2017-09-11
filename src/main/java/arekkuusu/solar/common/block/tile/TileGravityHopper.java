/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.block.ModBlocks;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
public class TileGravityHopper extends TileBase implements ITickable {

	private static final Map<EnumFacing, Vec3d> FACING_MAP = ImmutableMap.<EnumFacing, Vec3d>builder()
			.put(EnumFacing.UP, new Vec3d(0.5D, 0.75D, 0.5D))
			.put(EnumFacing.DOWN, new Vec3d(0.5D, 0.25D, 0.5D))
			.put(EnumFacing.NORTH, new Vec3d(0.5D, 0.5D, 0.25D))
			.put(EnumFacing.SOUTH, new Vec3d(0.5D, 0.5D, 0.75D))
			.put(EnumFacing.EAST, new Vec3d(0.75D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, new Vec3d(0.25D, 0.5D, 0.5D))
			.build();
	private final GravityHopperItemHandler handler;
	private boolean isPowered;
	private boolean inverse;
	private int tick;

	public TileGravityHopper() {
		handler = new GravityHopperItemHandler(this);
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void update() {
		if(!world.isRemote) {
			if(tick % 2 == 0) {
				EnumFacing facing = getFacing();

				if(handler.getStack().isEmpty()) {
					Optional<BlockPos> in = traceBlock(facing);
					if(in.isPresent()) {
						Optional<IItemHandler> inventoryIn = getInventory(in.get(), facing.getOpposite());
						inventoryIn.ifPresent(this::transferIn);
					}
				}

				if(!handler.getStack().isEmpty()) {
					Optional<BlockPos> out = traceBlock(facing.getOpposite());
					if(out.isPresent()) {
						Optional<IItemHandler> inventoryOut = getInventory(out.get(), facing);
						inventoryOut.ifPresent(this::transferOut);
					}
				}
			}
		} else {
			spawnParticles();
		}
		tick++;
	}

	private Optional<BlockPos> traceBlock(EnumFacing facing) { //Oh boy, I cant wait to use raytrace! ♪~ ᕕ(ᐛ)ᕗ
		for(int forward = 0; forward < 10; forward++) {
			BlockPos target = pos.offset(facing, forward + 1);
			IBlockState state = world.getBlockState(target);
			if(state.getBlock().hasTileEntity(state)) {
				return Optional.of(target);
			}
		}
		return Optional.empty();
	}

	private Optional<IItemHandler> getInventory(BlockPos target, EnumFacing facing) {
		if(world.isBlockLoaded(target, false)) {
			TileEntity tile = world.getTileEntity(target);
			if(tile != null) {
				IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
				return handler != null ? Optional.of(handler)
						: Optional.ofNullable(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
			}
		}
		return Optional.empty();
	}

	private void transferIn(IItemHandler handler) {
		for(int slot = 0; slot < handler.getSlots(); slot++) {
			ItemStack inserted = handler.getStackInSlot(slot);
			if(!inserted.isEmpty() && !handler.extractItem(slot, Integer.MAX_VALUE, true).isEmpty()) {
				this.handler.setStack(handler.extractItem(slot, Integer.MAX_VALUE, false));
				break;
			}
		}
	}

	private void transferOut(IItemHandler handler) {
		ItemStack inserted = this.handler.getStack().copy();

		for(int slot = 0; slot < handler.getSlots(); slot++) {
			ItemStack inSlot = handler.getStackInSlot(slot);

			if(inSlot.isEmpty() || (ItemHandlerHelper.canItemStacksStack(inSlot, inserted) && (inSlot.getCount() < inSlot.getMaxStackSize() && inSlot.getCount() < handler.getSlotLimit(slot)))
					&& !handler.insertItem(slot, inserted, true).isEmpty()) {

				inserted = handler.insertItem(slot, inserted, false);
				this.handler.setStack(inserted);
				break;
			}
		}
	}

	private void spawnParticles() {
		if(tick % 120 == 0) {
			EnumFacing facing = getFacing();
			Vec3d front = getOffSet(facing);

			BlockPos target = pos.offset(facing.getOpposite(), 9);
			Vec3d to = new Vec3d(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D);
			double speed = 0.025D;

			ParticleUtil.spawnNeutronBlast(world, front.x, front.y, front.z, speed, to.x, to.y, to.z, 0xFF0303, 0.25F, true);
		} else if(tick % 4 == 0 && world.rand.nextBoolean()) {
			EnumFacing facing = getFacing();
			Vec3d back = getOffSet(facing.getOpposite());

			double speed = world.rand.nextDouble() * -0.015D;
			Vec3d vec = new Vec3d(facing.getFrontOffsetX() * speed, facing.getFrontOffsetY() * speed, facing.getFrontOffsetZ() * speed);

			ParticleUtil.spawnLightParticle(world, back.x, back.y, back.z, vec.x, vec.y, vec.z, 0x49FFFF, 30, 2F);
		}
	}

	public void remove() {
		if(!world.isRemote) {
			ItemStack stack = handler.extractItem(0, Integer.MAX_VALUE, false);
			if(!stack.isEmpty()) {
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.spawnEntity(item);
			}
		}
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		EnumFacing facing = getFacing().getOpposite();
		world.setBlockState(pos, ModBlocks.gravity_hopper.getDefaultState().withProperty(BlockDirectional.FACING, facing));
		this.inverse = inverse;
	}

	public boolean isPowered() {
		return isPowered;
	}

	public void setPowered(boolean powered) {
		isPowered = powered;
	}

	private EnumFacing getFacing() {
		IBlockState here = world.getBlockState(pos);
		return here.getValue(BlockDirectional.FACING);
	}

	private Vec3d getOffSet(EnumFacing facing) {
		return FACING_MAP.get(facing).addVector(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		handler.deserializeNBT(compound);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		NBTTagCompound tag = handler.serializeNBT();
		compound.merge(tag);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	private static class GravityHopperItemHandler extends ItemStackHandler {

		private final TileGravityHopper tile;

		GravityHopperItemHandler(TileGravityHopper tile) {
			super(1);
			this.tile = tile;
		}

		private ItemStack getStack() {
			return stacks.get(0);
		}

		private void setStack(ItemStack stack) {
			setStackInSlot(0, stack);
		}

		@Override
		public int getSlotLimit(int slot) {
			return Integer.MAX_VALUE;
		}

		@Override
		protected void onContentsChanged(int slot) {
			tile.markDirty();
		}
	}
}
