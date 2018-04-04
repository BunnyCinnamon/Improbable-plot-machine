/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.energy.data;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 21/03/2018.
 * It's distributed as part of Solar.
 *
 * Provides an {@link ILumen} capability for an {@link ItemStack} inside a {@link LumenStackWrapper} implementation
 */
public class LumenStackProvider<T extends Item & IEntangledStack> implements ICapabilityProvider {

	@CapabilityInject(ILumen.class)
	public static Capability<ILumen> NEUTRON_CAPABILITY = null;
	private final LumenWrapper handler;

	/**
	 * Constructor for a custom {@link LumenWrapper} implementation
	 *
	 * @param handler lumen wrapper provider
	 */
	public LumenStackProvider(LumenWrapper handler) {
		this.handler = handler;
	}

	/**
	 * Constructor for a default {@link LumenStackWrapper<T>} implementation
	 *
	 * @param holder The {@link Item} class
	 * @param stack  The {@link ItemStack}
	 * @param max    Lumen capacity
	 */
	public LumenStackProvider(T holder, ItemStack stack, int max) {
		this.handler = new LumenStackWrapper<>(holder, stack, max);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return facing == null  && capability == NEUTRON_CAPABILITY;
	}

	@Nullable
	@Override
	public <C> C getCapability(@Nonnull Capability<C> capability, @Nullable EnumFacing facing) {
		return facing == null && capability == NEUTRON_CAPABILITY
				?  NEUTRON_CAPABILITY.cast(handler)
				: null;
	}
}
