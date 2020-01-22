package arekkuusu.implom.common.block.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class TileBlastFurnaceInput extends TileMultiblockImouto implements ITickable {

	@Override
	public void update() {
		if(isClientWorld()) return;

		if(hasValidOniichan()) {
			IItemHandler handler = getTile(TileMultiblockOniichan.class, getWorld(), getOniichan())
					.filter(tile -> tile.hasSecretCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
					.map(tile -> tile.getSecretCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
					.orElse(null);
			if(handler != null) {
				List<EntityItem> list = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos().offset(getFacingLazy())));
				for(EntityItem entityItem : list) {
					ItemStack entityStack = entityItem.getItem();
					if(entityStack.isEmpty()) continue;

					for(int i = 0; i < handler.getSlots(); i++) {
						ItemStack insertRemaining = handler.insertItem(i, entityStack, true);
						int inserted = entityStack.getCount() - insertRemaining.getCount();
						if(inserted > 0) {
							ItemStack insert = ItemHandlerHelper.copyStackWithSize(entityStack, inserted);
							handler.insertItem(i, insert, false);
							entityItem.getItem().shrink(inserted);
							break;
						}
					}
				}
			}
		}
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockHorizontal.FACING, getPos()).orElse(EnumFacing.NORTH);
	}
}
