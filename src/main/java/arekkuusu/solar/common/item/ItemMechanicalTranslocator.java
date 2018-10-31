/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.capability.relativity.RelativityHelper;
import arekkuusu.solar.api.capability.relativity.RelativityStackProvider;
import arekkuusu.solar.api.capability.relativity.data.IRelative;
import arekkuusu.solar.common.block.ModBlocks;
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
 * Created by <Arekkuusu> on 17/01/2018.
 * It's distributed as part of Solar.
 */
public class ItemMechanicalTranslocator extends ItemBaseBlock implements IUUIDDescription {

	public ItemMechanicalTranslocator() {
		super(ModBlocks.MECHANICAL_TRANSLOCATOR);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		RelativityHelper.getCapability(stack).flatMap(IRelative::getKey).ifPresent(uuid -> {
			addInformation(uuid, tooltip);
		});
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return RelativityStackProvider.createRelative(stack);
	}
}
