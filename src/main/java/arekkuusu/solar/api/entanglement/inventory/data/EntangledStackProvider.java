/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.inventory.data;

import arekkuusu.solar.api.entanglement.inventory.IEntangledIItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 11/08/2017.
 * It's distributed as part of Solar.
 * <p>
 * Provides an {@link IItemHandler} capability for an {@link ItemStack} inside a {@link IEntangledIItem} implementation
 */
public class EntangledStackProvider implements ICapabilityProvider {

	private final IEntangledIItem handler;

	private EntangledStackProvider(IEntangledIItem handler) {
		this.handler = handler;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <C> C getCapability(Capability<C> capability, @Nullable EnumFacing facing) {
		return facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler)
				: null;
	}

	/**
	 * Constructor for a default {@link EntangledStackWrapper<T>} implementation
	 *
	 * @param holder {@link Item} class
	 * @param stack  {@link ItemStack}
	 * @param slots  Slot amount
	 */
	public static <T extends Item & IEntangledIItemStack> EntangledStackProvider create(T holder, ItemStack stack, int slots) {
		return new EntangledStackProvider(new EntangledStackWrapper<>(holder, stack, slots));
	}

	/**
	 * Constructor for a custom {@link IEntangledIItem} implementation
	 *
	 * @param handler lumen wrapper provider
	 */
	public static EntangledStackProvider create(IEntangledIItem handler) {
		return new EntangledStackProvider(handler);
	}
}
