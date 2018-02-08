/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.client.util.helper.TooltipBuilder;
import arekkuusu.solar.common.block.ModBlocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static arekkuusu.solar.client.util.helper.TooltipBuilder.KeyCondition.SHIFT_KEY_DOWN;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public class ItemBlinker extends ItemBaseBlock implements IEntangledStack {

	ItemBlinker() {
		super(ModBlocks.BLINKER);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		getKey(stack).ifPresent(uuid -> TooltipBuilder.inline().condition(SHIFT_KEY_DOWN)
				.ifPresent(builder -> getInfo(builder, uuid)).build(tooltip));
	}
}
