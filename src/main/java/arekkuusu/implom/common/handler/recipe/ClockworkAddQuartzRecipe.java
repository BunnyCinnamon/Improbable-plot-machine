package arekkuusu.implom.common.handler.recipe;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.common.item.ItemQuartz;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ClockworkAddQuartzRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public ClockworkAddQuartzRecipe() {
		setRegistryName(LibMod.MOD_ID, "clockwork_add_quartz");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean clockwork = false, quartz = false;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == ModItems.CLOCKWORK) {
				if(!NBTHelper.getBoolean(stack, "unsealed") || NBTHelper.getNBTTag(stack, "quartz").isPresent())
					return false;
				else if(!clockwork) clockwork = true;
				else return false;
			} else if(stack.getItem() == ModItems.QUARTZ) {
				if(!NBTHelper.getEnum(ItemQuartz.Quartz.class, stack, "quartz_kind")
						.filter(tag -> tag.size == ItemQuartz.Quartz.Size.SMALL)
						.isPresent())
					return false;
				else if(!quartz) quartz = true;
				else return false;
			} else if(!stack.isEmpty()) return false;
		}
		return clockwork && quartz;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack quartz = ItemStack.EMPTY, clockwork = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == ModItems.CLOCKWORK) {
				clockwork = stack.copy();
			} else if(stack.getItem() == ModItems.QUARTZ) {
				quartz = stack.copy();
			}
		}
		if(!quartz.isEmpty() && !clockwork.isEmpty()) {
			NBTHelper.setBoolean(clockwork, "unsealed", false);
			NBTHelper.setNBT(clockwork, "quartz", quartz.serializeNBT());
			return clockwork;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width > 1 || height > 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.CLOCKWORK);
	}
}
