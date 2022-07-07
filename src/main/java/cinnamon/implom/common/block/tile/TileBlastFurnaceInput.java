package cinnamon.implom.common.block.tile;

import cinnamon.implom.api.helper.WorldHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileBlastFurnaceInput extends TileMultiBlockImouto {

    public LazyOptional<IItemHandler> cached = LazyOptional.empty();
    public NonNullConsumer<IItemHandler> consumer = this::transfer;
    public ItemStack filter = ItemStack.EMPTY;
    public boolean scan = false;
    public boolean load = false;

    public TileBlastFurnaceInput(BlockPos arg2, BlockState arg3) {
        super(ModTiles.BLAST_FURNACE_INPUT.get(), arg2, arg3);
    }

    public void transfer(IItemHandler handler) {
        List<ItemEntity> list = getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getBlockPos().relative(getFacingLazy())));
        for (ItemEntity entityItem : list) {
            ItemStack entityStack = entityItem.getItem();
            if (entityStack.isEmpty() || (!filter.isEmpty() && filter.getTags().anyMatch(entityStack::is))) continue;

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
                this.cached = WorldHelper.getTile(TileBlastFurnaceController.class, getLevel(), getOniichan())
                        .map(t -> t.getMultiBlockCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                        .orElse(LazyOptional.empty());
                if (this.cached.isPresent()) this.scan = false;
                this.cached.addListener(l -> this.scan = true);
            } else this.scan = true;
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && hasValidOniichan()
                ? WorldHelper.getTile(TileBlastFurnaceController.class, getLevel(), getOniichan()).map(tile -> tile.getMultiBlockCapability(cap, null)).orElse(LazyOptional.empty())
                : super.getCapability(cap, side);
    }

    public Direction getFacingLazy() {
        return WorldHelper.getStateValue(getLevel(), getBlockPos(), HorizontalDirectionalBlock.FACING).orElse(Direction.NORTH);
    }

    public static class Ticking implements BlockEntityTicker<TileBlastFurnaceInput> {

        @Override
        public void tick(Level arg, BlockPos arg2, BlockState arg3, TileBlastFurnaceInput instance) {
            if (instance.isClientWorld()) return;
            if (!instance.load) {
                instance.setupConnections();
                instance.load = true;
            }
            if (instance.hasValidOniichan()) {
                if (instance.scan) instance.setupConnections();
                instance.cached.ifPresent(instance.consumer);
            }
        }
    }
}
