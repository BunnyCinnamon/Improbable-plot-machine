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
	private boolean powered;
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

				Optional<BlockPos> positionOut = traceBlock(facing.getOpposite());
				positionOut.ifPresent(posOut -> {
					Optional<IItemHandler> inventoryOut = getInventory(posOut, facing);
					inventoryOut.ifPresent(handlerOut -> {
						Optional<BlockPos> positionIn = traceBlock(facing);
						positionIn.ifPresent(posIn -> {
							Optional<IItemHandler> inventoryIn = getInventory(posIn, facing.getOpposite());
							inventoryIn.ifPresent(this::transferIn);
						});

						if(!getStack().isEmpty()) {
							transferOut(handlerOut);
						}
					});
				});
			}
		} else {
			spawnParticles();
		}
		tick++;
	}

	private Optional<BlockPos> traceBlock(EnumFacing facing) { //Oh boy, I cant wait to use raytrace! ♪~ ᕕ(ᐛ)ᕗ
		for(int forward = 0; forward < 15; forward++) {
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
				setStack(handler.extractItem(slot, Integer.MAX_VALUE, false));
				break;
			}
		}
	}

	private void transferOut(IItemHandler handler) {
		ItemStack inserted = getStack().copy();

		for(int slot = 0; slot < handler.getSlots(); slot++) {
			ItemStack inSlot = handler.getStackInSlot(slot);

			if(inSlot.isEmpty() || (ItemHandlerHelper.canItemStacksStack(inSlot, inserted) && (inSlot.getCount() < inSlot.getMaxStackSize() && inSlot.getCount() < handler.getSlotLimit(slot)))) {
				inserted = handler.insertItem(slot, inserted, false);
				setStack(inserted);
				break;
			}
		}
	}

	private void spawnParticles() {
		if(tick % 160 == 0) {
			EnumFacing facing = getFacing();

			Vector3 from = new Vector3(getOffSet(facing));
			BlockPos target = pos.offset(facing.getOpposite(), 9);
			Vector3 to = new Vector3(target).add(0.5D, 0.5D, 0.5D);

			ParticleUtil.spawnNeutronBlast(world, from, 0.025D, to, 0xFF0303, 0.25F, true);
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
			ItemStack stack = getStack();
			if(!stack.isEmpty()) {
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.spawnEntity(item);
				setStack(ItemStack.EMPTY);
			}
		}
	}

	private ItemStack getStack() {
		return handler.getStackInSlot(0);
	}

	private void setStack(ItemStack stack) {
		handler.setStackInSlot(0, stack);
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(BlockDirectional.FACING, getFacing().getOpposite()));
		this.inverse = inverse;
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
	}

	private EnumFacing getFacing() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	private Vec3d getOffSet(EnumFacing facing) {
		return FACING_MAP.get(facing).addVector(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	void readNBT(NBTTagCompound cmp) {
		handler.deserializeNBT(cmp);
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
		NBTTagCompound tag = handler.serializeNBT();
		cmp.merge(tag);
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
