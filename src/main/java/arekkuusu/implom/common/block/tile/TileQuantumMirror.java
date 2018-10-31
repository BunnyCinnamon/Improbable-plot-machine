/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.inventory.data.EntangledTileWrapper;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.BlockQuantumMirror;
import arekkuusu.implom.common.network.PacketHelper;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TileQuantumMirror extends TileQuantumInventoryBase implements ITickable {

	private boolean dirty;

	@Override
	public EntangledTileWrapper createHandler() {
		return new EntangledTileWrapper<TileQuantumMirror>(this, getCapacity()) {
			@Override
			protected void onChange(int slot) {
				tile.dirty = true;
			}
		};
	}

	@Override
	public void update() {
		if(world.isRemote && world.rand.nextInt(10) == 0) {
			Vector3 from = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			IPM.getProxy().spawnSpeck(world, from, Vector3.rotateRandom().multiply(0.1F), 20, 0.1F, 0XFFFFFF, GlowTexture.STAR);
		}
		if(!world.isRemote && dirty) {
			handler.getKey().ifPresent(uuid -> {
				if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					PacketHelper.sendQuantumChanges(uuid);
				}
			});
			dirty = false;
		}
	}

	public void takeItem(EntityPlayer player) {
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack contained = handler.extractItem(0, Integer.MAX_VALUE, false);
		if(stack.isEmpty()) {
			player.setHeldItem(EnumHand.MAIN_HAND, contained);
		} else {
			ItemHandlerHelper.giveItemToPlayer(player, contained);
		}
	}

	@Override
	public int getCapacity() {
		return BlockQuantumMirror.SLOTS;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
}
