package arekkuusu.implom.common.item;

import arekkuusu.implom.api.helper.NBTHelper;
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
		addPropertyOverride(ResourceHelperStatic.getSimple(LibMod.MOD_ID, "state"), (s, w, e) ->
				NBTHelper.getBoolean(s, "state") ? 1 : 0
		);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		NBTHelper.setBoolean(stack, "state", !NBTHelper.getBoolean(stack, "state"));
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
