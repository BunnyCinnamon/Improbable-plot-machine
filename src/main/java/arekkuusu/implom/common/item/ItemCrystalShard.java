/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.api.capability.ILumenCapability;
import arekkuusu.implom.api.helper.LumenHelper;
import arekkuusu.implom.common.handler.data.capability.LumenShardCapability;
import arekkuusu.implom.common.handler.data.capability.provider.LumenProvider;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 14/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class ItemCrystalShard extends ItemBase {

	public ItemCrystalShard() {
		super(LibNames.CRYSTAL_SHARD);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(this.isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			LumenHelper.getCapability(stack).ifPresent(data -> data.set(10));
			items.add(stack);
		}
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new LumenProvider(new LumenShardCapability(stack));
	}
}
