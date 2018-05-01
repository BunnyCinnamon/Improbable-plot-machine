/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.common.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by <Arekkuusu> on 4/30/2018.
 * It's distributed as part of Solar.
 */
public class TileQuartzConsumer extends TileBase {

	private boolean hasItem;

	public boolean consume(ItemStack stack) {
		if(!stack.isEmpty() && stack.getItem() == ModItems.CRYSTAL_QUARTZ) {
			if(!world.isRemote) {
				hasItem = true;
				markDirty();
				updatePosition(world, pos);
				stack.shrink(1);
			}
			return true;
		}
		return false;
	}

	public boolean getHasItem() {
		return hasItem;
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		hasItem = compound.getBoolean("hasItem");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setBoolean("hasItem", hasItem);
	}
}
