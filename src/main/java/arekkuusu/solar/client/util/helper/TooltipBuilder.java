/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.helper;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by <Arekkuusu> on 12/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class TooltipBuilder {

	public static final TextFormatting[] DARK_GRAY_ITALIC = {TextFormatting.DARK_GRAY, TextFormatting.ITALIC};
	public static final TextFormatting[] GRAY_ITALIC = {TextFormatting.GRAY, TextFormatting.ITALIC};
	private static final String FORMAT = "tlp.%s.name";

	private TooltipBuilder() {
	}

	public static TooltipBuilder inline() {
		return new TooltipBuilder();
	}

	private StringBuilder builder = new StringBuilder();
	private List<String> strings = new ArrayList<>();

	public Condition<Object> condition(Condition<Object> condition) {
		return condition.setBuilder(this);
	}

	public Condition<TooltipBuilder> condition(KeyCondition keyCondition) {
		return keyCondition.apply(this);
	}

	public TooltipBuilder add(Object object, TextFormatting... formats) {
		for(TextFormatting format : formats) {
			builder.append(format);
		}
		builder.append(object);
		return this;
	}

	public TooltipBuilder addI18(String i18, TextFormatting... formats) {
		return add(I18n.format(String.format(FORMAT, i18)), formats);
	}

	public TooltipBuilder end() {
		strings.add(builder.toString());
		builder = new StringBuilder();
		return this;
	}

	public TooltipBuilder skip() {
		strings.add("");
		return this;
	}

	public void build(List<String> tooltip) {
		tooltip.addAll(strings);
	}

	public enum KeyCondition {
		NOTHING((a) -> true, builder -> builder),
		SHIFT_KEY_DOWN((a) -> GuiScreen.isShiftKeyDown()
				, builder -> builder.addI18("shift_for_info", TooltipBuilder.DARK_GRAY_ITALIC).end()),
		CONTROL_KEY_DOWN((a) -> GuiScreen.isCtrlKeyDown()
				, builder -> builder.addI18("ctrl_for_info", TooltipBuilder.DARK_GRAY_ITALIC).end());

		private final Function<TooltipBuilder, Boolean> canApply;
		private final Function<TooltipBuilder, TooltipBuilder> or;

		KeyCondition(Function<TooltipBuilder, Boolean> canApply, Function<TooltipBuilder, TooltipBuilder> or) {
			this.canApply = canApply;
			this.or = or;
		}

		public Condition<TooltipBuilder> apply(TooltipBuilder builder) {
			return new Condition<>(canApply, or).setBuilder(builder);
		}
	}

	public static class Condition<T> {
		private TooltipBuilder builder;
		private final Function<T, Boolean> canApply;
		private final Function<TooltipBuilder, TooltipBuilder> or;

		public Condition(Function<T, Boolean> canApply, Function<TooltipBuilder, TooltipBuilder> or) {
			this.canApply = canApply;
			this.or = or;
		}

		public Condition<T> setBuilder(TooltipBuilder builder) {
			this.builder = builder;
			return this;
		}

		public boolean canApply(T t) {
			return canApply.apply(t);
		}

		public TooltipBuilder apply(Function<TooltipBuilder, TooltipBuilder> apply) {
			if(canApply(null)) {
				apply.apply(builder);
				return builder;
			}
			return or.apply(builder);
		}

		public TooltipBuilder apply(T t, Function<TooltipBuilder, TooltipBuilder> apply) {
			if(canApply(t)) {
				apply.apply(builder);
				return builder;
			}
			return or.apply(builder);
		}
	}
}
