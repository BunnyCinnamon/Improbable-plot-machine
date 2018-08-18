/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.entanglement.quantum.QuantumDataHandler;
import arekkuusu.solar.api.entanglement.quantum.data.QimranutData;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
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
public class ItemQimranut extends ItemBaseBlock implements IEntangledStack, IEntangledDescription<ItemQimranut> {

	public ItemQimranut() {
		super(ModBlocks.QIMRANUT);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		addTooltipInfo(this, stack, tooltip);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void pickSource(PlayerInteractEvent.LeftClickBlock event) {
		ItemStack stack = event.getItemStack();
		if(stack.getItem() == this) {
			if(!event.getWorld().isRemote) {
				if(!getKey(stack).isPresent()) {
					setKey(stack, UUID.randomUUID());
				}
				getKey(stack).ifPresent(uuid -> {
					QimranutData data = QuantumDataHandler.getOrCreate(uuid, QimranutData::new);
					data.setFacing(event.getFace());
					data.setPos(event.getPos());
					data.setWorld(event.getWorld());
				});
			}
			event.setCanceled(true);
		}
	}
}
