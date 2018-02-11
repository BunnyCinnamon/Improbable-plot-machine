/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.recipe;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 16/08/2017.
 * It's distributed as part of Solar.
 */
public class EntangledCloningRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	EntangledCloningRecipe() {
		setRegistryName(LibMod.MOD_ID, "entangled_cloning");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int slots = inv.getHeight() * inv.getWidth();
		if(slots < 9 || slots % 2 != 0) return false;
		int center = (int) ((float)slots / 2F);
		ItemStack checked = inv.getStackInSlot(center);
		if(checked.isEmpty() || !checked.hasTagCompound()) return false;
		int amount = 0;

		for(int j = 0; j < slots; j++) {
			ItemStack stack = inv.getStackInSlot(j);
			if(!stack.isEmpty() && (!hasTag(stack) || stack.getItem() != checked.getItem())) return false;
			if(!stack.isEmpty() && j != center) {
				amount++;
			}
		}

		return amount > 0;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		int slots = inv.getHeight() * inv.getWidth();
		int center = (int) ((float)slots / 2F);
		ItemStack checked = inv.getStackInSlot(center);
		if(checked.isEmpty() || !checked.hasTagCompound()) return ItemStack.EMPTY;
		int amount = 0;

		for(int j = 0; j < slots; j++) {
			ItemStack stack = inv.getStackInSlot(j);
			if(!stack.isEmpty() && (!hasTag(stack) || stack.getItem() != checked.getItem())) return ItemStack.EMPTY;
			if(!stack.isEmpty() && j != center) {
				amount++;
			}
		}

		if(amount > 0) {
			ItemStack entanglement = new ItemStack(checked.getItem(), amount);
			getKey(checked).ifPresent(uuid -> setKey(entanglement, uuid));
			return entanglement;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		int slots = inv.getHeight() * inv.getWidth();
		NonNullList<ItemStack> nonNullList = NonNullList.<ItemStack>withSize(slots, ItemStack.EMPTY);
		int center = (int) ((float) slots / 2);

		ItemStack stack = inv.getStackInSlot(center);
		nonNullList.set(center, stack.copy());

		return nonNullList;
	}

	private boolean hasTag(ItemStack stack) {
		return stack.getItem() instanceof IEntangledStack && getKey(stack).isPresent();
	}

	private Optional<UUID> getKey(ItemStack stack) {
		return ((IEntangledStack) stack.getItem()).getKey(stack);
	}

	private void setKey(ItemStack stack, UUID uuid) {
		((IEntangledStack) stack.getItem()).setKey(stack, uuid);
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}
