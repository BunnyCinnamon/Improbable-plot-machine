/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.api.capability.energy.LumenHelper;
import arekkuusu.implom.api.capability.energy.LumenStackProvider;
import arekkuusu.implom.api.capability.energy.data.ComplexLumenStackWrapper;
import arekkuusu.implom.common.block.BlockNeutronBattery.BatteryCapacitor;
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
 * It's distributed as part of Improbable plot machine.
 */
public class ItemNeutronBattery extends ItemBaseBlock implements IUUIDDescription {

	public ItemNeutronBattery(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		LumenHelper.getComplexCapability(stack).ifPresent(complex -> {
			complex.getKey().ifPresent(uuid -> addInformation(uuid, tooltip));
		});
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return LumenStackProvider.create(new ComplexLumenStackWrapper(stack, -1) {

			@Override
			public void setMax(int max) {
				BatteryCapacitor capacitor = getCapacitor().setCapacity(max);
				NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
				root.setTag(BatteryCapacitor.NBT_TAG, capacitor.serializeNBT());
			}

			@Override
			public int getMax() {
				return getCapacitor().getCapacity();
			}

			BatteryCapacitor getCapacitor() {
				BatteryCapacitor capacitor = new BatteryCapacitor("", 0, 0);
				NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
				if(root.hasKey(BatteryCapacitor.NBT_TAG)) {
					capacitor.deserializeNBT(root.getCompoundTag(BatteryCapacitor.NBT_TAG));
				}
				return capacitor;
			}
		});
	}
}
