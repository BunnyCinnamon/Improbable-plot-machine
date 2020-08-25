package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.helper.WorldHelper;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class TileBlastFurnaceInput extends TileMultiBlockImouto implements ITickableTileEntity {

    public LazyOptional<IItemHandler> cached = LazyOptional.empty();
    public NonNullConsumer<IItemHandler> consumer = this::transfer;
    public boolean scan = false;
    public boolean load = false;

    public TileBlastFurnaceInput() {
        super(ModTiles.BLAST_FURNACE_INPUT.get());
    }

    @Override
    public void tick() {
        if (isClientWorld()) return;
        if (!load) {
            this.setupConnections();
            load = true;
        }
        if (hasValidOniichan()) {
            if (scan) setupConnections();
            cached.ifPresent(consumer);
        }
    }

    public void transfer(IItemHandler handler) {
        List<ItemEntity> list = getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(getPos().offset(getFacingLazy())));
        for (ItemEntity entityItem : list) {
            ItemStack entityStack = entityItem.getItem();
            if (entityStack.isEmpty()) continue;

            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack insertRemaining = handler.insertItem(i, entityStack, true);
                int inserted = entityStack.getCount() - insertRemaining.getCount();
                if (inserted > 0) {
                    ItemStack insert = ItemHandlerHelper.copyStackWithSize(entityStack, inserted);
                    handler.insertItem(i, insert, false);
                    entityItem.getItem().shrink(inserted);
                    break;
                }
            }
        }
    }

    public void setupConnections() {
        if (!this.cached.isPresent()) {
            if (this.hasOniichanByHerSide) {
                this.cached = WorldHelper.getTile(TileBlastFurnaceController.class, getWorld(), getOniichan())
                        .map(t -> t.getMultiBlockCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP))
                        .orElse(LazyOptional.empty());
                if (this.cached.isPresent()) this.scan = false;
                this.cached.addListener(l -> this.scan = true);
            } else this.scan = true;
        }
    }

    public Direction getFacingLazy() {
        return WorldHelper.getStateValue(getWorld(), getPos(), HorizontalBlock.HORIZONTAL_FACING).orElse(Direction.NORTH);
    }
}
