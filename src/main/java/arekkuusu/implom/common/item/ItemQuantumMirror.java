/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.api.capability.nbt.IInventoryNBTDataCapability;
import arekkuusu.implom.api.helper.InventoryHelper;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.data.capability.nbt.InventoryNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.provider.InventoryNBTProvider;
import arekkuusu.implom.common.network.PacketHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/*
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class ItemQuantumMirror extends ItemBaseBlock implements IUUIDDescription {

	public ItemQuantumMirror() {
		super(ModBlocks.QUANTUM_MIRROR);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
		InventoryHelper.getCapability(stack).map(data -> (IInventoryNBTDataCapability) data).ifPresent(instance -> {
			if(instance.getKey() != null) addInformation(instance.getKey(), tooltip);
		});
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new InventoryNBTProvider(new InventoryNBTDataCapability() {
			@Override
			public void onChange(ItemStack old) {
				if(old.getItem() != getStackInSlot(0).getItem()) {
					if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
						PacketHelper.sendQuantumMirrorPacket(this);
					}
				}
			}
		});
	}
}
