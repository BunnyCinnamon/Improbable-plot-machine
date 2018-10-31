package arekkuusu.solar.api.capability.relativity;

import arekkuusu.solar.api.capability.relativity.data.IRelative;
import arekkuusu.solar.api.capability.relativity.data.IRelativeRedstone;
import arekkuusu.solar.api.capability.relativity.data.RelativeRedstoneStackWrapper;
import arekkuusu.solar.api.capability.relativity.data.RelativeStackWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RelativityStackProvider implements ICapabilityProvider {

	@CapabilityInject(IRelative.class)
	public static Capability<IRelative> RELATIVE_CAPABILITY = null;
	@CapabilityInject(IRelativeRedstone.class)
	public static Capability<IRelativeRedstone> RELATIVE_REDSTONE_CAPABILITY = null;
	private final IRelative handler;

	public RelativityStackProvider(IRelative handler) {
		this.handler = handler;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == RELATIVE_CAPABILITY || (capability == RELATIVE_REDSTONE_CAPABILITY && handler instanceof IRelativeRedstone);
	}

	@Nullable
	@Override
	public <C> C getCapability(@Nonnull Capability<C> capability, @Nullable EnumFacing facing) {
		return capability == RELATIVE_CAPABILITY
				? RELATIVE_CAPABILITY.cast(handler)
				: capability == RELATIVE_REDSTONE_CAPABILITY && handler instanceof IRelativeRedstone
				? RELATIVE_REDSTONE_CAPABILITY.cast((IRelativeRedstone) handler)
				: null;
	}

	/**
	 * Constructor for a default {@link RelativeStackWrapper} implementation
	 *
	 * @param stack {@link ItemStack}
	 */
	public static RelativityStackProvider createRelative(ItemStack stack) {
		return new RelativityStackProvider(new RelativeStackWrapper(stack));
	}

	/**
	 * Constructor for a default {@link RelativeStackWrapper} implementation
	 *
	 * @param stack {@link ItemStack}
	 */
	public static RelativityStackProvider createRedstone(ItemStack stack) {
		return new RelativityStackProvider(new RelativeRedstoneStackWrapper(stack));
	}

	/**
	 * Constructor for a custom {@link IRelative} implementation
	 *
	 * @param handler lumen wrapper provider
	 */
	public static RelativityStackProvider create(IRelative handler) {
		return new RelativityStackProvider(handler);
	}
}
