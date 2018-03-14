/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.inventory.data.EntangledTileWrapper;
import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.common.network.PacketHelper;
import net.katsstuff.mirror.client.particles.GlowTexture;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public class TileQuantumMirror extends TileEntangledBase<EntangledTileWrapper> implements ITickable {

	public static final int SLOTS = 1;

	@Override
	public EntangledTileWrapper createHandler() {
		return new QuantumWrapper(this);
	}

	@Override
	public void update() {
		if(world.isRemote && world.rand.nextInt(10) == 0) {
			Vector3 from = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			FXUtil.spawnMute(world, from, Vector3.rotateRandom().multiply(0.1F), 20, 0.1F, 0XFFFFFF, GlowTexture.STAR);
		}
	}

	public void takeItem(EntityPlayer player) {
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
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

	public static class QuantumWrapper extends EntangledTileWrapper<TileQuantumMirror> {

		QuantumWrapper(TileQuantumMirror mirror) {
			super(mirror, SLOTS);
		}

		@Override
		protected void onChange(int slot) {
			tile.getKey().ifPresent(uuid -> {
				if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					PacketHelper.sendQuantumChange(uuid, getStackInSlot(slot), slot);
				}
			});
		}
	}
}
