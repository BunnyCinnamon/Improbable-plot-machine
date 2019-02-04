/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.nbt.IInventoryNBTDataCapability;
import arekkuusu.implom.api.helper.InventoryHelper;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.handler.data.capability.nbt.InventoryNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.provider.InventoryNBTProvider;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.network.PacketHelper;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TileQuantumMirror extends TileBase implements ITickable, INBTDataTransferableImpl {

	public final InventoryNBTProvider wrapper = new InventoryNBTProvider(new InventoryNBTDataCapability() {

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return stack.getItem() != ModItems.QUANTUM_MIRROR;
		}

		@Override
		public void onChange(ItemStack old) {
			if(old.getItem() != getStackInSlot(0).getItem()) {
				if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					PacketHelper.sendQuantumMirrorPacket(wrapper.instance);
				}
			}
		}

		@Override
		public void setKey(@Nullable UUID uuid) {
			super.setKey(uuid);
			markDirty();
			sync();
		}
	});

	@Override
	public void update() {
		if(world.isRemote && world.rand.nextInt(10) == 0) {
			Vector3 from = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			IPM.getProxy().spawnSpeck(world, from, Vector3.rotateRandom().multiply(0.1F), 20, 0.1F, 0XFFFFFF, GlowTexture.STAR);
		}
	}

	public void takeItem(EntityPlayer player) {
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack contained = wrapper.instance.getStackInSlot(0);
		wrapper.instance.setStackInSlot(0, ItemStack.EMPTY);
		if(stack.isEmpty()) {
			player.setHeldItem(EnumHand.MAIN_HAND, contained);
		} else {
			ItemHandlerHelper.giveItemToPlayer(player, contained);
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return wrapper.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return wrapper.hasCapability(capability, facing)
				? wrapper.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setTag("itemstack", wrapper.serializeNBT());
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		wrapper.deserializeNBT(compound.getCompoundTag("itemstack"));
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}

	@Override
	public String group() {
		return DefaultGroup.INVENTORY;
	}

	@Override
	public void setKey(UUID uuid) {
		wrapper.instance.setKey(uuid);
	}

	@Nullable
	@Override
	public UUID getKey() {
		return wrapper.instance.getKey();
	}

	@Override
	public void fromItemStack(ItemStack stack) {
		InventoryHelper.getCapability(stack).map(i -> (IInventoryNBTDataCapability) i).map(IInventoryNBTDataCapability::getKey).ifPresent(wrapper.instance::setKey);
	}

	@Override
	public void toItemStack(ItemStack stack) {
		InventoryHelper.getCapability(stack).map(i -> (IInventoryNBTDataCapability) i).ifPresent(instance -> instance.setKey(wrapper.instance.getKey()));
	}
}
