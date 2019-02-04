/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.api.capability.INBTDataTransferable;
import arekkuusu.implom.api.helper.WorldAccessHelper;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.data.capability.nbt.WorldAccessNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.provider.WorldAccessProvider;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 24/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
@EventBusSubscriber(modid = LibMod.MOD_ID, value = Side.SERVER)
public class ItemQimranut extends ItemBaseBlock implements IUUIDDescription {

	public ItemQimranut() {
		super(ModBlocks.QIMRANUT);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		WorldAccessHelper.getCapability(stack).ifPresent(instance -> {
			if(instance.getKey() != null) addInformation(instance.getKey(), tooltip, INBTDataTransferable.DefaultGroup.WORLD_ACCESS);
		});
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new WorldAccessProvider(new WorldAccessNBTDataCapability());
	}
}
