/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.handler.recipe;

import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Created by <Arekkuusu> on 16/08/2017.
 * It's distributed as part of Solar.
 */
public abstract class EntangledCloningRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	EntangledCloningRecipe() {
		setRegistryName(LibMod.MOD_ID, "entangled_cloning");
	}

	/*@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack from = ItemStack.EMPTY;
		ItemStack to = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack inSlot = inv.getStackInSlot(i);
			if(!inSlot.isEmpty()) {
				if(from.isEmpty()) from = inSlot;
				else if(to.isEmpty()) to = inSlot;
				else return false;
			}
		}
		return isEntangled(from) && from.getCount() == 1 && isEntangled(to);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack from = ItemStack.EMPTY;
		ItemStack to = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack inSlot = inv.getStackInSlot(i);
			if(!inSlot.isEmpty()) {
				if(from.isEmpty()) from = inSlot;
				else if(to.isEmpty()) to = inSlot;
				else return ItemStack.EMPTY;
			}
		}
		if(isEntangled(from) && from.getCount() == 1 && (isEntangled(to) && hasNoTag(to))) {
			ItemStack copied = to.copy();
			copyTag(from, copied);
			copied.setCount(1);
			return copied;
		} else return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack inSlot = inv.getStackInSlot(i);
			if(!inSlot.isEmpty()) {
				list.set(i, inSlot.copy());
				break;
			}
		}
		return list;
	}

	private boolean isEntangled(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() instanceof IQuantumStack;
	}

	private boolean hasNoTag(ItemStack stack) {
		return !getKey(stack).isPresent();
	}

	private void copyTag(ItemStack from, ItemStack to) {
		getKey(from).ifPresent(uuid -> setKey(to, uuid));
	}

	private Optional<UUID> getKey(ItemStack stack) {
		return ((IQuantumStack) stack.getItem()).getKey(stack);
	}

	private void setKey(ItemStack stack, UUID uuid) {
		((IQuantumStack) stack.getItem()).setKey(stack, uuid);
	}

	@Override
	public boolean canFit(int width, int height) {
		return width > 1 || height > 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}*/
}
