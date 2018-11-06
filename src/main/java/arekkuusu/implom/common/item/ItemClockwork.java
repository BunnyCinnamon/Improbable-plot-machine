package arekkuusu.implom.common.item;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelClockwork;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.lib.LibMod;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.client.helper.ResourceHelperStatic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemClockwork extends ItemBase {

	public ItemClockwork() {
		super(LibNames.CLOCKWORK);
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		NBTHelper.setBoolean(stack, "unsealed", !NBTHelper.getBoolean(stack, "unsealed"));
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelClockwork());
		ModelHandler.registerModel(this, 0);
	}
}
