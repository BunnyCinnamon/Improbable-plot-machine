/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.inventory.IEntangledIItemStack;
import arekkuusu.solar.api.entanglement.inventory.data.EntangledStackProvider;
import arekkuusu.solar.api.entanglement.inventory.data.EntangledStackWrapper;
import arekkuusu.solar.common.block.BlockQuantumMirror;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.network.PacketHelper;
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
 * It's distributed as part of Solar.
 */
public class ItemQuantumMirror extends ItemBaseBlock implements IEntangledIItemStack, IHandlerEntangledDescription<ItemQuantumMirror> {

	public ItemQuantumMirror() {
		super(ModBlocks.QUANTUM_MIRROR);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
		addTooltipInfo(this, stack, tooltip);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return EntangledStackProvider.create(new EntangledStackWrapper<ItemQuantumMirror>(this, stack, BlockQuantumMirror.SLOTS) {
			@Override
			protected void onChange(int slot) {
				getKey().ifPresent(uuid -> {
					if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
						PacketHelper.sendQuantumChange(uuid, getStackInSlot(slot), slot);
					}
				});
			}
		});
	}
}
