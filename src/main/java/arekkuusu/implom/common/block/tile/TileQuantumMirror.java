/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.InventoryHelper;
import arekkuusu.implom.api.capability.nbt.IInventoryNBTDataCapability;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.handler.data.capability.provider.QuantumMirrorCapabilityProvider;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TileQuantumMirror extends TileBase implements ITickable, INBTDataTransferableImpl {

	public final QuantumMirrorCapabilityProvider provider = new QuantumMirrorCapabilityProvider(this);

	@Override
	public void update() {
		if(world.isRemote && world.rand.nextInt(10) == 0) {
			Vector3 from = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			IPM.getProxy().spawnSpeck(world, from, Vector3.rotateRandom().multiply(0.1F), 20, 0.1F, 0XFFFFFF, Light.GLOW, GlowTexture.STAR.getTexture());
		}
	}

	public void takeItem(EntityPlayer player) {
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack contained = provider.inventoryNBTDataCapability.getStackInSlot(0);
		provider.inventoryNBTDataCapability.setStackInSlot(0, ItemStack.EMPTY);
		if(stack.isEmpty()) {
			player.setHeldItem(EnumHand.MAIN_HAND, contained);
		}
		else {
			ItemHandlerHelper.giveItemToPlayer(player, contained);
		}
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

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setTag(NBT_PROVIDER, provider.serializeNBT());
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		provider.deserializeNBT(compound.getCompoundTag(NBT_PROVIDER));
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
	public void setKey(@Nullable UUID uuid) {
		provider.inventoryNBTDataCapability.setKey(uuid);
	}

	@Nullable
	@Override
	public UUID getKey() {
		return provider.inventoryNBTDataCapability.getKey();
	}

	@Override
	public void fromItemStack(ItemStack stack) {
		InventoryHelper.getCapability(stack).map(i -> (IInventoryNBTDataCapability) i).map(IInventoryNBTDataCapability::getKey).ifPresent(this::setKey);
	}

	@Override
	public void toItemStack(ItemStack stack) {
		InventoryHelper.getCapability(stack).map(i -> (IInventoryNBTDataCapability) i).ifPresent(instance -> instance.setKey(this.getKey()));
	}
}
