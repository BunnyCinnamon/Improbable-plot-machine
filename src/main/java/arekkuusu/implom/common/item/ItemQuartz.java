package arekkuusu.implom.common.item;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;
import java.util.stream.Stream;

public class ItemQuartz extends ItemBase {

	public ItemQuartz() {
		super(LibNames.QUARTZ);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(ItemStack stack) {
		return NBTHelper.getEnum(Quartz.class, stack, "quartz").map(Enum::ordinal).orElse(0);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(this.isInCreativeTab(tab)) {
			Stream.of(Quartz.values()).forEach(q -> items.add(NBTHelper.setEnum(new ItemStack(this), q, Constants.NBT_QUARTZ)));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHelper.registerModel(this, Quartz.class);
	}

	public static class Constants {
		public static final String NBT_QUARTZ = "quartz";
	}

	public enum Quartz implements IStringSerializable {
		/*WHITE_SMALL(Size.SMALL, EnumDyeColor.WHITE),
		WHITE_MEDIUM(Size.MEDIUM, EnumDyeColor.WHITE),
		WHITE_LARGE(Size.LARGE, EnumDyeColor.WHITE),*/
		BLUE_SMALL(Size.SMALL, EnumDyeColor.BLUE),
		BLUE_MEDIUM(Size.MEDIUM, EnumDyeColor.BLUE),
		BLUE_LARGE(Size.LARGE, EnumDyeColor.BLUE),
		/*GREEN_SMALL(Size.SMALL, EnumDyeColor.GREEN),
		GREEN_MEDIUM(Size.MEDIUM, EnumDyeColor.GREEN),
		GREEN_LARGE(Size.LARGE, EnumDyeColor.GREEN),
		YELLOW_SMALL(Size.SMALL, EnumDyeColor.YELLOW),
		YELLOW_MEDIUM(Size.MEDIUM, EnumDyeColor.YELLOW),
		YELLOW_LARGE(Size.LARGE, EnumDyeColor.YELLOW),
		PINK_SMALL(Size.SMALL, EnumDyeColor.PINK),
		PINK_MEDIUM(Size.MEDIUM, EnumDyeColor.PINK),
		PINK_LARGE(Size.LARGE, EnumDyeColor.PINK)*/;

		public final Size size;
		public final EnumDyeColor color;

		Quartz(Size size, EnumDyeColor color) {
			this.size = size;
			this.color = color;
		}

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}

		public enum Size implements IStringSerializable {
			SMALL,
			MEDIUM,
			LARGE;

			@Override
			public String getName() {
				return name().toLowerCase(Locale.ROOT);
			}
		}
	}
}
