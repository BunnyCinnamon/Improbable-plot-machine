/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.common.block.ModBlocks;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 19/12/2017.
 * It's distributed as part of Solar.
 */
public class ItemVacuumConveyor extends ItemBaseBlock {

	public ItemVacuumConveyor() {
		super(ModBlocks.VACUUM_CONVEYOR);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		Tooltip.inline()
				.condition(() -> NBTHelper.hasTag(stack, "lookup"))
				.ifTrueJ(sub -> sub
						.addI18n("tlp.item_filter", Tooltip.DarkGrayItalic()).add(": ")
						.add(new ItemStack(stack.getOrCreateSubCompound("lookup")).getDisplayName(), Tooltip.GrayItalic())
						.newline()
						.newline()
				).apply().build(tooltip);
	}
}
