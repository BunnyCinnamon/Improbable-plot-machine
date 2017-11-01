/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.client.util.helper.TooltipHelper;
import arekkuusu.solar.common.block.ModBlocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.SHIFT_KEY_DOWN;

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
		getKey(stack).ifPresent(uuid -> TooltipHelper.inline().condition(SHIFT_KEY_DOWN)
				.ifAgrees(builder -> getInfo(builder, uuid)).build(tooltip));
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(!world.isRemote && !NBTHelper.hasTag(stack, SolarApi.QUANTUM_DATA)) {
			setKey(stack, UUID.randomUUID());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0, ResourceLibrary.getModel("blinker_", ""));
	}
}
