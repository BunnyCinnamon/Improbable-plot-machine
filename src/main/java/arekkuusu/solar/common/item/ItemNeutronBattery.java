/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.capability.energy.LumenHelper;
import arekkuusu.solar.api.capability.energy.LumenStackProvider;
import arekkuusu.solar.api.capability.energy.data.ComplexLumenStackWrapper;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.common.block.BlockNeutronBattery.BatteryCapacitor;
import net.minecraft.block.Block;
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
 * Created by <Arekkuusu> on 21/03/2018.
 * It's distributed as part of Solar.
 */
public class ItemNeutronBattery extends ItemBaseBlock implements IUUIDDescription {

	public ItemNeutronBattery(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		LumenHelper.getCapability(ComplexLumenStackWrapper.class, stack).ifPresent(complex -> {
			complex.getKey().ifPresent(uuid -> addInformation(uuid, tooltip));
		});
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return LumenStackProvider.create(new ComplexLumenStackWrapper(stack, -1) {
			@Override
			public int getMax() {
				BatteryCapacitor capacitor = new BatteryCapacitor();
				NBTHelper.getNBTTag(stack, "neutron_capacitor").ifPresent(capacitor::deserializeNBT);
				return capacitor.getCapacity();
			}
		});
	}
}
