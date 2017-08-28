/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.quantum.ISimpleQuantum;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.handler.data.QuantumHandler;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import arekkuusu.solar.common.network.PacketHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public class TileQuantumMirror extends TileBase implements ITickable, ISimpleQuantum {

	private final QuantumTileHandler handler;
	private UUID key;
	public int tick;

	public TileQuantumMirror() {
		handler = new QuantumTileHandler(this);
	}

	@Override
	public void update() {
		if(world.isRemote && world.rand.nextInt(10) == 0) {
			double x = pos.getX() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double y = pos.getY() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double z = pos.getZ() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double speed = 0.05D;

			ParticleUtil.spawnQuorn(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, speed, x, y, z, 0.1F, 0XFFFFFF);
		}
		++tick;
	}

	@SuppressWarnings("ConstantConditions")
	public void handleItemTransfer(EntityPlayer player, EnumHand hand) {
		if(hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler handler = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			ItemStack inserted = player.getHeldItem(hand);

			if(!inserted.isEmpty()) {
				for(int i = 0; i < handler.getSlots(); i++) {
					ItemStack test = handler.insertItem(i, inserted, true);
					if(test != inserted) {
						player.setHeldItem(hand, handler.insertItem(i, inserted, false));
						break;
					}
				}
			} else {
				for(int i = 0; i < handler.getSlots(); i++) {
					ItemStack test = handler.extractItem(i, handler.getSlotLimit(i), false);
					if(!test.isEmpty()) {
						player.setHeldItem(hand, test);
						break;
					}
				}
			}
		}
	}

	/*public void putItem(EntityPlayer player, EnumHand hand, ItemStack stack) {
		if(key == null || !handler.assertSafety(stack)) return;

		ItemStack contained = getQuantumItem(0).copy();

		if(ItemHandlerHelper.canItemStacksStack(contained, stack)) {
			if(contained.getCount() < contained.getMaxStackSize()) {
				int available = contained.getMaxStackSize() - contained.getCount();
				if(available > 0) {
					int difference = MathHelper.clamp(available, 0, stack.getCount());
					stack.shrink(difference);
					contained.grow(difference);
					setQuantumItem(contained, 0);
				}
			}
		} else if(contained.isEmpty()) {
			player.setHeldItem(hand, ItemStack.EMPTY);
			setQuantumItem(stack.copy(), 0);
		} else {
			player.setHeldItem(hand, contained);
			setQuantumItem(stack.copy(), 0);
		}
	}*/

	public void takeItem(EntityPlayer player, ItemStack stack) {
		if(key == null) return;
		ItemStack contained = getQuantumItem(0).copy();
		if(stack.isEmpty()) {
			player.setHeldItem(EnumHand.MAIN_HAND, contained);
		} else {
			ItemHandlerHelper.giveItemToPlayer(player, contained);
		}
		setQuantumItem(ItemStack.EMPTY, 0);
	}

	@Override
	public ItemStack getQuantumItem(int slot) {
		return key == null ? ItemStack.EMPTY : SolarApi.getQuantumItem(key, slot);
	}

	@Override
	public void setQuantumItem(ItemStack stack, int slot) {
		if(!world.isRemote) {
			SolarApi.setQuantumItem(key, stack, slot);
		}
		updateState();
	}

	@Nullable
	@Override
	public UUID getKey() {
		return key;
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
		PacketHandler.updatePosition(world, pos);
	}

	void updateState() {
		IBlockState state = world.getBlockState(pos);
		world.notifyNeighborsOfStateChange(pos, state.getBlock(), true);
		WorldQuantumData.get(world).markDirty();
	}

	@Override
	public void readNBT(NBTTagCompound compound) {
		if(compound.hasUniqueId("key")) {
			key = compound.getUniqueId("key");
		}
	}

	@Override
	public void writeNBT(NBTTagCompound compound) {
		if(key != null) {
			compound.setUniqueId("key", key);
		}
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

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	private static class QuantumTileHandler extends QuantumHandler {

		private final TileQuantumMirror tile;

		QuantumTileHandler(TileQuantumMirror tile) {
			super(1);
			this.tile = tile;
		}

		@Nullable
		@Override
		protected UUID getKey() {
			return tile.getKey();
		}

		@Override
		protected void onChange() {
			tile.updateState();
		}
	}
}
