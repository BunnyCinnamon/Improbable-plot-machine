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
		/*addPropertyOverride(ResourceHelperStatic.getSimple(LibMod.MOD_ID, "unsealed"), (s, w, e) ->
				NBTHelper.getBoolean(s, "unsealed") ? 1 : 0
		);
		addPropertyOverride(ResourceHelperStatic.getSimple(LibMod.MOD_ID, "quartz"), (s, w, e) -> NBTHelper.getNBTTag(s, "quartz")
				.flatMap(tag -> NBTHelper.getEnum(ItemQuartz.Quartz.class, new ItemStack(tag), "quartz_kind"))
				.map(q -> {
					switch (q.color) {
						case WHITE:
							return 0.1F;
						case BLUE:
							return 0.2F;
						case GREEN:
							return 0.3F;
						case YELLOW:
							return 0.4F;
						case PINK:
							return 0.5F;
						default:
							return 0F;
					}
				}).orElse(0F)
		);*/
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
