/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.neutron.data;

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
 */
public class NeutronStackProvider<T extends Item & IEntangledStack> implements ICapabilityProvider {

	@CapabilityInject(INeutron.class)
	public static Capability<INeutron> NEUTRON_CAPABILITY = null;
	private final NeutronStackWrapper<T> handler;

	public NeutronStackProvider(NeutronStackWrapper<T> handler) {
		this.handler = handler;
	}

	public NeutronStackProvider(T holder, ItemStack stack, int max) {
		this.handler = new NeutronStackWrapper<>(holder, stack, max);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return facing == null  && capability == NEUTRON_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return facing == null && capability == NEUTRON_CAPABILITY
				?  NEUTRON_CAPABILITY.cast(handler)
				: null;
	}
}
