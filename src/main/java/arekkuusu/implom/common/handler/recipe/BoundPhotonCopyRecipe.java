package arekkuusu.implom.common.handler.recipe;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class BoundPhotonCopyRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public BoundPhotonCopyRecipe() {
		setRegistryName(LibMod.MOD_ID, "bound_photon_copy");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int tags = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && (stack.getItem() != ModItems.BOUND_PHOTON || ++tags > 2)) {
				return false;
			}
		}
		return tags == 2;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack from = ItemStack.EMPTY;
		ItemStack to = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(from.isEmpty()) from = stack;
				else if(to.isEmpty()) to = stack;
			}
		}
		if(!from.isEmpty() && !to.isEmpty()) {
			to.setTagCompound(NBTHelper.fixNBT(from));
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.BOUND_PHOTON);
	}
}
