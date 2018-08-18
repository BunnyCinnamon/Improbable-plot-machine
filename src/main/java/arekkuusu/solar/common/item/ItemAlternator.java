/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.common.block.ModBlocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 23/01/2018.
 * It's distributed as part of Solar.
 */
public class ItemAlternator extends ItemBaseBlock implements IEntangledStack, IEntangledDescription<ItemAlternator> {

	public ItemAlternator() {
		super(ModBlocks.ALTERNATOR);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		addTooltipInfo(this, stack, tooltip);
	}
}
