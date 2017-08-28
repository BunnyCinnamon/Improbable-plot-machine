/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.handler.recipe;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.quantum.IQuantumItem;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 16/08/2017.
 * It's distributed as part of Solar.
 */
public class QuantumCloningRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	QuantumCloningRecipe() {
		setRegistryName(LibMod.MOD_ID, "quantum_cloning");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int slots = inv.getHeight() * inv.getWidth();
		int center = (int) ((float)slots / 2F);
		ItemStack checked = inv.getStackInSlot(center);
		int i = 0;

		for(int j = 0; j < slots; j++) {
			ItemStack stack = inv.getStackInSlot(j);
			if(!stack.isEmpty() && (!hasTag(stack) || checked.getItem() != stack.getItem())) return false;
			if(!stack.isEmpty() && j != center) i++;
		}

		return !checked.isEmpty() && checked.hasTagCompound() && i > 0;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		int slots = inv.getHeight() * inv.getWidth();
		int center = (int) ((float)slots / 2F);
		ItemStack checked = inv.getStackInSlot(center);
		int i = 0;

		for(int j = 0; j < slots; j++) {
			ItemStack stack = inv.getStackInSlot(j);
			if(!stack.isEmpty() && (!hasTag(stack) || checked.getItem() != stack.getItem())) return ItemStack.EMPTY;
			if(!stack.isEmpty() && j != center) i++;
		}

		if(!checked.isEmpty() && hasTag(checked) && i > 0) {
			ItemStack quingentilliard = new ItemStack(checked.getItem(), i);
			NBTTagCompound tag = quingentilliard.getOrCreateSubCompound(SolarApi.QUANTUM_DATA);
			getKey(checked).ifPresent(uuid -> tag.setUniqueId("key", uuid));
			return quingentilliard;
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
		return stack.getItem() instanceof IQuantumItem && getKey(stack).isPresent();
	}

	private Optional<UUID> getKey(ItemStack stack) {
		return ((IQuantumItem) stack.getItem()).getKey(stack);
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
