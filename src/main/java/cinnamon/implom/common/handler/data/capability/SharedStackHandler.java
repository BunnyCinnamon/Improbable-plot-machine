package cinnamon.implom.common.handler.data.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class SharedStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {

    public final ItemStackHandler left;
    public final ItemStackHandler right;

    public SharedStackHandler(ItemStackHandler left, ItemStackHandler right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot < left.getSlots()) left.setStackInSlot(slot, stack);
        else right.setStackInSlot(slot - left.getSlots(), stack);
    }

    @Override
    public int getSlots() {
        return left.getSlots() + right.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot < left.getSlots() ? left.getStackInSlot(slot) : right.getStackInSlot(slot - left.getSlots());
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return slot < left.getSlots() ? left.insertItem(slot, stack, simulate) : right.insertItem(slot - left.getSlots(), stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot < left.getSlots() ? left.extractItem(slot, amount, simulate) : right.extractItem(slot - left.getSlots(), amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot < left.getSlots() ? left.getSlotLimit(slot) : right.getSlotLimit(slot - left.getSlots());
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return slot < left.getSlots() ? left.isItemValid(slot, stack) : right.isItemValid(slot - left.getSlots(), stack);
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        //Nothing
    }
}
