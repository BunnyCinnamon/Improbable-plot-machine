/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.handler.data.QuantumTileWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public class TileQuantumMirror extends TileQuantumBase<QuantumTileWrapper> implements ITickable {

	public static final int SLOTS = 1;
	public int tick;

	@Override
	public QuantumTileWrapper createHandler() {
		return new QuantumTileWrapper<>(this, SLOTS);
	}

	@Override
	public void update() {
		if(world.isRemote && world.rand.nextInt(10) == 0) {
			Vector3 from = new Vector3(pos).add(0.5D, 0.5D, 0.5D);

			ParticleUtil.spawnQuorn(world, from, Vector3.getRandomVec(0.1F), 20, 0.1F, 0XFFFFFF);
		}
		++tick;
	}

	public void takeItem(EntityPlayer player, ItemStack stack) {
		if(hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler handler = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(handler == null) return;

			ItemStack contained = handler.extractItem(0, Integer.MAX_VALUE, false);
			if(stack.isEmpty()) {
				player.setHeldItem(EnumHand.MAIN_HAND, contained);
			} else {
				ItemHandlerHelper.giveItemToPlayer(player, contained);
			}
		}
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
}
