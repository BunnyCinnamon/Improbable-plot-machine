/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.common.IPM;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Map;
import java.util.Optional;

/*
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TileGravityHopper extends TileBase implements ITickable {

	private static final Map<EnumFacing, Vector3> FF = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.apply(0.5D, 0.75D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.apply(0.5D, 0.25D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.apply(0.5D, 0.5D, 0.25D))
			.put(EnumFacing.SOUTH, Vector3.apply(0.5D, 0.5D, 0.75D))
			.put(EnumFacing.EAST, Vector3.apply(0.75D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.apply(0.25D, 0.5D, 0.5D))
			.build();
	private boolean powered;
	private boolean inverse;
	private int cooldown;

	@Override
	public void update() {
		if(!world.isRemote) {
			if(cooldown <= 0) {
				EnumFacing facing = getFacing();
				traceBlock(facing).ifPresent(out -> {
					traceBlock(facing.getOpposite()).ifPresent(in -> {
						getInventory(out, facing.getOpposite()).ifPresent(invOut -> {
							getInventory(in, facing).ifPresent(invIn -> {
								ItemStack stack = transferOut(invOut, true);
								if(!stack.isEmpty() && transferIn(invIn, stack, true)) {
									if(transferIn(invIn, transferOut(invOut, false), false)) cooldown = 5;
								}
							});
						});
					});
				});
			} else cooldown--;
		} else {
			spawnParticles();
		}
	}

	private Optional<BlockPos> traceBlock(EnumFacing facing) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(getPos());
		for(int forward = 0; forward < 3; forward++) {
			pos.move(facing);
			if(world.isValid(pos) && world.isBlockLoaded(pos)) {
				IBlockState state = world.getBlockState(pos);
				if(state.getBlock().hasTileEntity(state)) {
					return getTile(TileEntity.class, world, pos).filter(tile ->
							tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())
							|| tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
					).map(ignored -> pos.toImmutable());
				}
			}
		}
		return Optional.empty();
	}

	private Optional<IItemHandler> getInventory(BlockPos target, EnumFacing facing) {
		if(world.isValid(target) && world.isBlockLoaded(target, false)) {
			return getTile(TileEntity.class, world, target).map(tile -> {
				IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
				if(handler == null) handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				return handler;
			});
		}
		return Optional.empty();
	}

	private ItemStack transferOut(IItemHandler inv, boolean test) {
		for(int slot = 0; slot < inv.getSlots(); slot++) {
			ItemStack in = inv.getStackInSlot(slot);
			if(!in.isEmpty()) {
				return inv.extractItem(slot, Integer.MAX_VALUE, test);
			}
		}
		return ItemStack.EMPTY;
	}

	private boolean transferIn(IItemHandler inv, ItemStack inserted, boolean test) {
		for(int slot = 0; slot < inv.getSlots(); slot++) {
			ItemStack inSlot = inv.getStackInSlot(slot);
			if(inSlot.isEmpty() || (ItemHandlerHelper.canItemStacksStack(inSlot, inserted)
					&& (inSlot.getCount() + inserted.getCount() <= inSlot.getMaxStackSize()
					&& inSlot.getCount() + inserted.getCount() <= inv.getSlotLimit(slot)))) {
				return inv.insertItem(slot, inserted, test) != inserted;
			}
		}
		return false;
	}

	private void spawnParticles() {
		if(world.getTotalWorldTime() % 180 == 0) {
			EnumFacing facing = getFacing();
			Vector3 back = getOffSet(facing);
			Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
					.asImmutable()
					.multiply(0.005D);
			IPM.getProxy().spawnNeutronBlast(world, back, speedVec, 40, 0.25F, 0xFF0303, false);
		} else if(world.getTotalWorldTime() % 4 == 0 && world.rand.nextBoolean()) {
			EnumFacing facing = getFacing().getOpposite();
			Vector3 back = getOffSet(facing);
			double speed = world.rand.nextDouble() * 0.015D;
			Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
					.asImmutable()
					.multiply(speed);
			IPM.getProxy().spawnMute(world, back, speedVec, 30, 2F, 0x49FFFF, Light.GLOW);
		}
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

	private Vector3 getOffSet(EnumFacing facing) {
		return FF.get(facing).add(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		powered = compound.getBoolean("powered");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setBoolean("powered", powered);
	}
}
