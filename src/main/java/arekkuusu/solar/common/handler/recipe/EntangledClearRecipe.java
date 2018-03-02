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
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Created by <Arekkuusu> on 17/08/2017.
 * It's distributed as part of Solar.
 */
public class EntangledClearRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	EntangledClearRecipe() {
		setRegistryName(LibMod.MOD_ID, "entangled_clear");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack cleared = ItemStack.EMPTY;
		for(int j = 0; j < inv.getSizeInventory(); j++) {
			ItemStack inSlot = inv.getStackInSlot(j);
			if(cleared.isEmpty() && !inSlot.isEmpty()) {
				cleared = inSlot;
			} else if(!cleared.isEmpty() && !inSlot.isEmpty()) return false;
		}

		return !cleared.isEmpty() && cleared.getItem() instanceof IEntangledStack;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack cleared = ItemStack.EMPTY;
		for(int j = 0; j < inv.getSizeInventory(); j++) {
			ItemStack inSlot = inv.getStackInSlot(j);
			if(cleared.isEmpty() && !inSlot.isEmpty()) {
				cleared = inSlot;
			} else if(!cleared.isEmpty() && !inSlot.isEmpty()) return ItemStack.EMPTY;
		}

		if(!cleared.isEmpty() && cleared.getItem() instanceof IEntangledStack) {
			return new ItemStack(cleared.getItem(), cleared.getCount());
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}
