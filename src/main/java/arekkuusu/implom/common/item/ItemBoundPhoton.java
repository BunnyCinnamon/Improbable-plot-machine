package arekkuusu.implom.common.item;

import arekkuusu.implom.api.capability.INBTDataTransferable;
import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemBoundPhoton extends ItemBase implements IUUIDDescription {

	public ItemBoundPhoton() {
		super(LibNames.BOUND_PHOTON);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		Tooltip.inline().condition(() -> NBTHelper.hasTag(NBTHelper.fixNBT(stack), Constants.NBT_BOUND)).ifTrueJ(builder -> builder
				.condition(KeyCondition$.MODULE$.shiftKeyDown())
				.ifTrueJ(sub -> {
					NBTTagCompound compound = stack.getOrCreateSubCompound(Constants.NBT_BOUND);
					for(String key : compound.getKeySet()) {
						NBTTagCompound tag = compound.getCompoundTag(key);
						sub = sub.addI18n("tlp." + key + ".group").space();
						for(String subKey : tag.getKeySet()) {
							if(tag.hasUniqueId(subKey.replace("Most", ""))) {
								sub = getInfo(sub, Objects.requireNonNull(tag.getUniqueId(subKey.replace("Most", ""))));
							}
						}
					}
					return sub;
				}).apply()
		).apply().build(tooltip);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void pickSource(PlayerInteractEvent.RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		if(stack.getItem() == this) {
			if(!event.getWorld().isRemote) {
				TileEntity tile = event.getWorld().getTileEntity(event.getPos());
				if(tile instanceof INBTDataTransferable) {
					NBTTagCompound compound = stack.getOrCreateSubCompound(Constants.NBT_BOUND);
					String key = ((INBTDataTransferable) tile).group();
					NBTTagCompound tag = compound.getCompoundTag(key);
					if(!compound.hasKey(key)) compound.setTag(key, tag);
					EnumActionResult result = ((INBTDataTransferable) tile).init(tag);
					if(result != EnumActionResult.FAIL) {
						TextComponentTranslation bound = new TextComponentTranslation("tlp." + key + ".group", TextFormatting.DARK_GRAY);
						TextComponentTranslation action = new TextComponentTranslation("status.bound", bound, TextFormatting.BLACK);
						event.getEntityPlayer().sendStatusMessage(action, true);
					}
				}
			}
			event.setCanceled(true);
		}
	}

	public static class Constants {
		public static final String NBT_BOUND = "bound_data";
	}
}
