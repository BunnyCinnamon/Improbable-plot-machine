/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.api.capability.INBTDataTransferable;
import arekkuusu.implom.api.capability.WorldAccessHelper;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.data.capability.nbt.WorldAccessNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.provider.WorldAccessProvider;
import arekkuusu.implom.common.network.PacketHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 21/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class ItemMutator extends ItemBaseBlock implements IUUIDDescription {

	public ItemMutator() {
		super(ModBlocks.MUTATOR);
		MinecraftForge.EVENT_BUS.register(this);
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
		return new WorldAccessProvider(new WorldAccessNBTDataCapability() {
			@Override
			public void onChange() {
				if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					PacketHelper.sendMutatorPacket(this);
				}
			}
		});
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void pickSource(PlayerInteractEvent.LeftClickBlock event) {
		ItemStack stack = event.getItemStack();
		if(stack.getItem() == this) {
			if(!event.getWorld().isRemote) {
				WorldAccessHelper.getCapability(stack).ifPresent(instance -> {
					instance.set(event.getWorld(), event.getPos(), event.getFace());
				});
			}
			event.setCanceled(true);
		}
	}
}
