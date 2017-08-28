/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
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
public class TooltipHelper {

	public static final TextFormatting[] DARK_GRAY_ITALIC = {TextFormatting.DARK_GRAY, TextFormatting.ITALIC};
	public static final TextFormatting[] GRAY_ITALIC = {TextFormatting.GRAY, TextFormatting.ITALIC};
	private static final String FORMAT = "tlp.%s.name";

	public static Builder inline() {
		return new Builder();
	}

	public static class Builder {

		private StringBuilder builder = new StringBuilder();
		private List<String> strings = new ArrayList<>();

		public Condition condition(Condition condition) {
			return condition.apply(this);
		}

		public Builder add(Object object, TextFormatting... formats) {
			for(TextFormatting format : formats) {
				builder.append(format);
			}
			builder.append(object);
			return this;
		}

		public Builder addI18(String i18, TextFormatting... formats) {
			return add(I18n.format(String.format(FORMAT, i18)), formats);
		}

		public Builder end() {
			strings.add(builder.toString());
			builder = new StringBuilder();
			return this;
		}

		public Builder skip() {
			strings.add("");
			return this;
		}

		public void build(List<String> tooltip) {
			tooltip.addAll(strings);
		}
	}

	public enum Condition {
		NOTHING(() -> true, builder -> {}),
		SHIFT_KEY_DOWN(GuiScreen::isShiftKeyDown
				, builder -> builder.addI18("shift_for_info", TooltipHelper.DARK_GRAY_ITALIC).end()),
		CONTROL_KEY_DOWN(GuiScreen::isCtrlKeyDown
				, builder -> builder.addI18("ctrl_for_info", TooltipHelper.DARK_GRAY_ITALIC).end());

		private final BooleanSupplier supplier;
		private final Consumer<Builder> consumer;
		private Builder builder;

		Condition(BooleanSupplier supplier, Consumer<Builder> consumer) {
			this.supplier = supplier;
			this.consumer = consumer;
		}

		public Condition apply(Builder builder) {
			this.builder = builder;
			return this;
		}

		public boolean agrees() {
			return supplier.getAsBoolean();
		}

		public Builder ifAgrees(Consumer<Builder> consumer) {
			Builder builder = this.builder;
			this.builder = null;
			if(agrees()) consumer.accept(builder);
			else this.consumer.accept(builder);
			return builder;
		}
	}
}
