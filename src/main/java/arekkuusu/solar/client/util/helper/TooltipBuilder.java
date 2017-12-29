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
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

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

	public Condition condition(BooleanSupplier condition) {
		return new Condition(condition).setBuilder(this);
	}

	public Condition condition(KeyCondition keyCondition) {
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
		NOTHING(() -> true, builder -> {
		}),
		SHIFT_KEY_DOWN(GuiScreen::isShiftKeyDown
				, builder -> builder.addI18("shift_for_info", TooltipBuilder.DARK_GRAY_ITALIC).end()),
		CONTROL_KEY_DOWN(GuiScreen::isCtrlKeyDown
				, builder -> builder.addI18("ctrl_for_info", TooltipBuilder.DARK_GRAY_ITALIC).end());

		private final BooleanSupplier condition;
		private final Consumer<TooltipBuilder> or;

		KeyCondition(BooleanSupplier condition, Consumer<TooltipBuilder> or) {
			this.condition = condition;
			this.or = or;
		}

		public Condition apply(TooltipBuilder builder) {
			return new Condition(condition).setBuilder(builder).orElse(or);
		}
	}

	public static class Condition {
		private final BooleanSupplier condition;
		private Consumer<TooltipBuilder> present;
		private Consumer<TooltipBuilder> or;
		private TooltipBuilder builder;

		public Condition(BooleanSupplier condition) {
			this.condition = condition;
		}

		public Condition setBuilder(TooltipBuilder builder) {
			this.builder = builder;
			return this;
		}

		public Condition ifPresent(Consumer<TooltipBuilder> present) {
			this.present = present;
			return this;
		}

		public Condition orElse(Consumer<TooltipBuilder> or) {
			this.or = or;
			return this;
		}

		public boolean canApply() {
			return condition.getAsBoolean();
		}

		public TooltipBuilder apply() {
			if(canApply() && present != null) present.accept(builder);
			else if(or != null) or.accept(builder);
			return builder;
		}

		public void build(List<String> tooltip) {
			apply().build(tooltip);
		}
	}
}
