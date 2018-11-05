package arekkuusu.implom.common.handler.recipe;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ClockworkRemoveQuartzRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public ClockworkRemoveQuartzRecipe() {
		setRegistryName(LibMod.MOD_ID, "clockwork_remove_quartz");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean clockwork = false;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == ModItems.CLOCKWORK) {
				if(!NBTHelper.getNBTTag(stack, "quartz").isPresent())
					return false;
				else if(!clockwork) clockwork = true;
				else return false;
			} else if(!stack.isEmpty()) return false;
		}
		return clockwork;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack clockwork = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == ModItems.CLOCKWORK) {
				clockwork = stack.copy();
				break;
			}
		}
		if(!clockwork.isEmpty()) {
			return NBTHelper.getNBTTag(clockwork, "quartz").map(ItemStack::new).orElse(ItemStack.EMPTY);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == ModItems.CLOCKWORK) {
				ItemStack clockwork = stack.copy();
				NBTHelper.setBoolean(clockwork, "unsealed", true);
				NBTHelper.removeTag(clockwork, "quartz");
				ret.set(i, clockwork);
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width > 0 || height > 0;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.QUARTZ);
	}
}
