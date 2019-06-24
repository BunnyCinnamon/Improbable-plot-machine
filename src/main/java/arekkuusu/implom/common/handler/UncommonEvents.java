package arekkuusu.implom.common.handler;

import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.data.ImbuingData;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = LibMod.MOD_ID)
public class UncommonEvents {

	@SubscribeEvent
	public static void updateImbuingProgress(TickEvent.WorldTickEvent event) {
		for(Map.Entry<World, Map<BlockPos, ImbuingData.Progress>> worldEntry : ImbuingData.PROGRESS.entrySet()) {
			Iterator<Map.Entry<BlockPos, ImbuingData.Progress>> iterator = worldEntry.getValue().entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<BlockPos, ImbuingData.Progress> entry = iterator.next();
				ImbuingData.Progress progress = entry.getValue();
				BlockPos pos = entry.getKey();
				IBlockState state = worldEntry.getKey().getBlockState(pos);
				if(progress.timer > 0 && state.getMaterial() == Material.GLASS && state.getBlock() != ModBlocks.IMBUED_QUARTZ) {
					if(progress.lastUpdated++ >= 240) {
						progress.timer -= 20;
						if(progress.timer < 0) progress.timer = 0;
						progress.lastUpdated = 0;
					}
				} else iterator.remove();
			}
		}
	}
}
