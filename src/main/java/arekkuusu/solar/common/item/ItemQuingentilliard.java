/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.quantum.IQuantumStack;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.client.util.helper.TooltipBuilder;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.block.tile.TileQuingentilliard;
import arekkuusu.solar.common.handler.data.QuantumStackProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 19/12/2017.
 * It's distributed as part of Solar.
 */
public class ItemQuingentilliard extends ItemBaseBlock implements IQuantumStack {

	public ItemQuingentilliard() {
		super(ModBlocks.QUINGENTILLIARD);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		TooltipBuilder.inline()
				.condition(() -> NBTHelper.hasTag(stack, "lookup"))
				.ifPresent(sub -> sub
						.addI18("quingentilliard_filter", TooltipBuilder.DARK_GRAY_ITALIC)
						.add(": ", TooltipBuilder.DARK_GRAY_ITALIC)
						.add(new ItemStack(NBTHelper.<NBTTagCompound>getNBT(stack, "lookup").orElse(new NBTTagCompound())).getDisplayName(), TooltipBuilder.GRAY_ITALIC)
						.end()
						.skip()
				).build(tooltip);
		addTooltipInfo(stack, tooltip);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new QuantumStackProvider<>(this, stack, TileQuingentilliard.SLOTS);
	}
}
