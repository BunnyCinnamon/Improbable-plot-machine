/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.api.capability.relativity.RelativityHelper;
import arekkuusu.implom.api.capability.relativity.RelativityStackProvider;
import arekkuusu.implom.api.capability.relativity.data.IRelative;
import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.common.block.ModBlocks;
import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
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
 * Created by <Arekkuusu> on 24/02/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class ItemQelaion extends ItemBaseBlock implements IUUIDDescription {

	public ItemQelaion() {
		super(ModBlocks.QELAION);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		RelativityHelper.getCapability(stack).flatMap(IRelative::getKey).ifPresent(uuid -> Tooltip.inline()
				.condition(KeyCondition$.MODULE$.shiftKeyDown())
				.ifTrueJ(builder -> getInfo(builder, uuid)
						.condition(() -> NBTHelper.hasUniqueID(stack, "nodes"))
						.ifTrueJ(sub -> {
							String key = NBTHelper.getUniqueID(stack, "nodes").toString();
							return sub.newline()
									.addI18n("tlp.tag_nodes", Tooltip.DarkGrayItalic())
									.add(": ").newline()
									.add(" > ").add(key.substring(0, 18)).newline()
									.add(" > ").add(key.substring(18)).newline();
						}).apply()
				).apply().build(tooltip)
		);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return RelativityStackProvider.createRelative(stack);
	}
}
