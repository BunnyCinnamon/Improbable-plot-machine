/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.energy;

import arekkuusu.solar.api.capability.energy.data.ComplexLumenStackWrapper;
import arekkuusu.solar.api.capability.energy.data.IComplexLumen;
import arekkuusu.solar.api.capability.energy.data.ILumen;
import arekkuusu.solar.api.capability.energy.data.SimpleLumenStackWrapper;
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
 * <p>
 * Provides an {@link ILumen} capability for an {@link ItemStack}
 */
public class LumenStackProvider implements ICapabilityProvider {

	@CapabilityInject(ILumen.class)
	public static Capability<ILumen> LUMEN_CAPABILITY = null;
	@CapabilityInject(IComplexLumen.class)
	public static Capability<IComplexLumen> COMPLEX_LUMEN_CAPABILITY = null;
	private final ILumen handler;

	private LumenStackProvider(ILumen handler) {
		this.handler = handler;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == LUMEN_CAPABILITY || (capability == COMPLEX_LUMEN_CAPABILITY && handler instanceof IComplexLumen);
	}

	@Nullable
	@Override
	public <C> C getCapability(@Nonnull Capability<C> capability, @Nullable EnumFacing facing) {
		return capability == LUMEN_CAPABILITY
				? LUMEN_CAPABILITY.cast(handler)
				: capability == COMPLEX_LUMEN_CAPABILITY && handler instanceof IComplexLumen
				? COMPLEX_LUMEN_CAPABILITY.cast((IComplexLumen) handler)
				: null;
	}

	/**
	 * Constructor for a default {@link ComplexLumenStackWrapper} implementation
	 *
	 * @param stack The {@link ItemStack}
	 * @param max   Lumen capacity
	 */
	public static LumenStackProvider createComplex(ItemStack stack, int max) {
		return new LumenStackProvider(new ComplexLumenStackWrapper(stack, max));
	}

	/**
	 * Constructor for a default {@link SimpleLumenStackWrapper} implementation
	 *
	 * @param stack The {@link ItemStack}
	 * @param max   Lumen capacity
	 */
	public static LumenStackProvider createSimple(ItemStack stack, int max) {
		return new LumenStackProvider(new SimpleLumenStackWrapper(stack, max));
	}

	/**
	 * Constructor for a custom {@link ILumen} implementation
	 *
	 * @param handler lumen wrapper provider
	 */
	public static LumenStackProvider create(ILumen handler) {
		return new LumenStackProvider(handler);
	}
}
