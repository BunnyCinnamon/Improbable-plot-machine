package arekkuusu.solar.common.item;

import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.client.helper.ResourceHelperStatic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

import java.util.Locale;
import java.util.stream.Stream;

public class ItemQuartz extends ItemBase {

	public ItemQuartz() {
		super(LibNames.QUARTZ);
		addPropertyOverride(ResourceHelperStatic.getSimple(LibMod.MOD_ID, "size"), (s, w, e) ->
				NBTHelper.getEnum(Quartz.class, s, "quartz_kind").map(q -> (float) q.size.ordinal() * 0.1F).orElse(0F)
		);
		addPropertyOverride(ResourceHelperStatic.getSimple(LibMod.MOD_ID, "color"), (s, w, e) ->
				NBTHelper.getEnum(Quartz.class, s, "quartz_kind").map(q -> {
					switch (q.color) {
						case WHITE: return 0.0F;
						case BLUE: return 0.1F;
						case GREEN: return 0.2F;
						case YELLOW: return 0.3F;
						case PINK: return 0.4F;
						default: return 0F;
					}
				}).orElse(0F)
		);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(this.isInCreativeTab(tab)) {
			Stream.of(Quartz.values()).forEach(q -> items.add(NBTHelper.setEnum(new ItemStack(this), q, "quartz_kind")));
		}
	}

	public enum Quartz implements IStringSerializable {
		WHITE_SMALL(Size.SMALL, EnumDyeColor.WHITE),
		WHITE_MEDIUM(Size.MEDIUM, EnumDyeColor.WHITE),
		WHITE_LARGE(Size.LARGE, EnumDyeColor.WHITE),
		BLUE_SMALL(Size.SMALL, EnumDyeColor.BLUE),
		BLUE_MEDIUM(Size.MEDIUM, EnumDyeColor.BLUE),
		BLUE_LARGE(Size.LARGE, EnumDyeColor.BLUE),
		GREEN_SMALL(Size.SMALL, EnumDyeColor.GREEN),
		GREEN_MEDIUM(Size.MEDIUM, EnumDyeColor.GREEN),
		GREEN_LARGE(Size.LARGE, EnumDyeColor.GREEN),
		YELLOW_SMALL(Size.SMALL, EnumDyeColor.YELLOW),
		YELLOW_MEDIUM(Size.MEDIUM, EnumDyeColor.YELLOW),
		YELLOW_LARGE(Size.LARGE, EnumDyeColor.YELLOW),
		PINK_SMALL(Size.SMALL, EnumDyeColor.PINK),
		PINK_MEDIUM(Size.MEDIUM, EnumDyeColor.PINK),
		PINK_LARGE(Size.LARGE, EnumDyeColor.PINK);

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
