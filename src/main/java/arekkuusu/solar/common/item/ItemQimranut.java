/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.capability.worldaccess.WorldAccessHelper;
import arekkuusu.solar.api.capability.worldaccess.WorldAccessStackProvider;
import arekkuusu.solar.api.capability.worldaccess.data.IWorldAccess;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 24/12/2017.
 * It's distributed as part of Solar.
 */
@EventBusSubscriber(modid = LibMod.MOD_ID, value = Side.SERVER)
public class ItemQimranut extends ItemBaseBlock implements IUUIDDescription {

	public ItemQimranut() {
		super(ModBlocks.QIMRANUT);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		WorldAccessHelper.getCapability(stack).flatMap(IWorldAccess::getKey).ifPresent(uuid -> {
			addInformation(uuid, tooltip);
		});
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return WorldAccessStackProvider.create(stack);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void pickSource(PlayerInteractEvent.LeftClickBlock event) {
		ItemStack stack = event.getItemStack();
		if(stack.getItem() == this) {
			if(!event.getWorld().isRemote) {
				WorldAccessHelper.getCapability(stack).ifPresent(handler -> {
					if(!handler.getKey().isPresent()) handler.setKey(UUID.randomUUID());
					handler.setFacing(event.getFace());
					handler.setPos(event.getPos());
					handler.setWorld(event.getWorld());
				});
			}
			event.setCanceled(true);
		}
	}
}
