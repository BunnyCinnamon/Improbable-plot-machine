/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.inventory;

import arekkuusu.solar.api.capability.inventory.data.EntangledStackWrapper;
import arekkuusu.solar.api.capability.inventory.data.IEntangledIItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 11/08/2017.
 * It's distributed as part of Solar.
 * <p>
 * Provides an {@link IItemHandler} capability for an {@link ItemStack} inside a {@link IEntangledIItemHandler} implementation
 */
public class EntangledStackProvider implements ICapabilityProvider {

	private final IEntangledIItemHandler handler;

	private EntangledStackProvider(IEntangledIItemHandler handler) {
		this.handler = handler;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
		return facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <C> C getCapability(@Nonnull Capability<C> capability, EnumFacing facing) {
		return facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler)
				: null;
	}

	/**
	 * Constructor for a default {@link EntangledStackWrapper} implementation
	 *
	 * @param stack {@link ItemStack}
	 * @param slots Slot amount
	 */
	public static EntangledStackProvider create(ItemStack stack, int slots) {
		return new EntangledStackProvider(new EntangledStackWrapper(stack, slots));
	}

	/**
	 * Constructor for a custom {@link IEntangledIItemHandler} implementation
	 *
	 * @param handler lumen wrapper provider
	 */
	public static EntangledStackProvider create(IEntangledIItemHandler handler) {
		return new EntangledStackProvider(handler);
	}
}
