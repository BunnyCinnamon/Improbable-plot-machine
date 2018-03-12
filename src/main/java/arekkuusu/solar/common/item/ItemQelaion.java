/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.common.block.ModBlocks;
import net.katsstuff.mirror.client.helper.KeyCondition;
import net.katsstuff.mirror.client.helper.Tooltip;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Snack> on 24/02/2018.
 * It's distributed as part of Solar.
 */
public class ItemQelaion extends ItemBaseBlock implements IEntangledStack {

	public ItemQelaion() {
		super(ModBlocks.QELAION);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		getKey(stack).ifPresent(uuid -> Tooltip.inline().condition(KeyCondition.ShiftKeyDown$.MODULE$)
				.ifTrueJ(builder -> getInfo(builder, uuid)
						.condition(() -> NBTHelper.hasUniqueID(stack, "nodes"))
						.ifTrueJ(sub -> { String key = NBTHelper.getUniqueID(stack, "nodes").toString();
							return sub.addI18n("tlp.tag_nodes.name", Tooltip.DarkGrayItalic())
									.add(": ").newline()
									.add(" > ").add(key.substring(0, 18)).newline()
									.add(" > ").add(key.substring(18)).newline();
						}).apply()
				).apply().build(tooltip)
		);
	}
}
