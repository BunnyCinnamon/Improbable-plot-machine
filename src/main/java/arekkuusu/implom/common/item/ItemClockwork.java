package arekkuusu.implom.common.item;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelClockwork;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.handler.data.capability.InventoryClockworkCapability;
import arekkuusu.implom.common.handler.data.capability.provider.CapabilityProvider;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class ItemClockwork extends ItemBase {

	public ItemClockwork() {
		super(LibNames.CLOCKWORK);
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		NBTHelper.setBoolean(NBTHelper.fixNBT(stack), Constants.NBT_UNSEALED, !NBTHelper.getBoolean(NBTHelper.fixNBT(stack), Constants.NBT_UNSEALED));
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new CapabilityProvider.Builder(stack).put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new InventoryClockworkCapability()).build();
	}

	@Override
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelClockwork());
		ModelHelper.registerModel(this, 0);
	}

	public static class Constants {
		public static final String NBT_UNSEALED = "unsealed";
	}
}
