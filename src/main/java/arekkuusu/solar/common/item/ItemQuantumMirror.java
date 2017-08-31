/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.quantum.IQuantumItem;
import arekkuusu.solar.client.util.helper.TooltipHelper;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.handler.data.QuantumProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.CONTROL_KEY_DOWN;
import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.SHIFT_KEY_DOWN;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public class ItemQuantumMirror extends ItemBaseBlock implements IQuantumItem {

	public ItemQuantumMirror() {
		super(ModBlocks.quantum_mirror);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		getKey(stack).ifPresent(uuid -> TooltipHelper.inline()
				.condition(SHIFT_KEY_DOWN)
				.ifAgrees(builder -> {
					builder.addI18("quantum", TooltipHelper.DARK_GRAY_ITALIC).end();
					SolarApi.getQuantumStacks(uuid).forEach(item -> builder
							.add("    - ", TextFormatting.DARK_GRAY)
							.add(item.getDisplayName(), TooltipHelper.GRAY_ITALIC)
							.add(" x " + item.getCount()).end()
					);
					builder.skip();

					builder.condition(CONTROL_KEY_DOWN).ifAgrees(sub -> {
						sub.addI18("uuid_key", TooltipHelper.DARK_GRAY_ITALIC).add(": ").end();
						String key = uuid.toString();
						sub.add(" > ").add(key.substring(0, 18)).end();
						sub.add(" > ").add(key.substring(18)).end();
					});
				}).build(tooltip));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new QuantumProvider(this, stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack original = player.getHeldItem(hand);
		if(!world.isRemote) {
			EnumHand otherHand = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

			handleItemTransfer(player, world, original, otherHand);
		}
		return ActionResult.newResult(EnumActionResult.FAIL, original);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		if(!world.isRemote && !NBTHelper.hasTag(stack, SolarApi.QUANTUM_DATA)) {
			setKey(stack, UUID.randomUUID());
		}
	}

	@Override
	public int getSlots() {
		return 1;
	}
}
