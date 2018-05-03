/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.recipe;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.entanglement.inventory.EntangledIItemHandler;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
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
		ItemStack toClear = ItemStack.EMPTY;
		for(int j = 0; j < inv.getSizeInventory(); j++) {
			ItemStack inSlot = inv.getStackInSlot(j);
			if(toClear.isEmpty() && !inSlot.isEmpty()) toClear = inSlot;
			else if(!inSlot.isEmpty()) return false;
		}
		return !toClear.isEmpty() && toClear.getItem() instanceof IEntangledStack;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		for(int j = 0; j < inv.getSizeInventory(); j++) {
			ItemStack inSlot = inv.getStackInSlot(j);
			if(!inSlot.isEmpty()) {
				ItemStack toClear = inSlot.copy();
				NBTHelper.removeTag(toClear, EntangledIItemHandler.NBT_TAG);
				toClear.setCount(1);
				return toClear;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
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
