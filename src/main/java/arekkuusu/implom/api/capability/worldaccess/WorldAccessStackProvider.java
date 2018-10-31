/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.worldaccess;

import arekkuusu.implom.api.capability.worldaccess.data.IWorldAccess;
import arekkuusu.implom.api.capability.worldaccess.data.WorldAccessStackWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 30/10/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class WorldAccessStackProvider implements ICapabilityProvider {

	@CapabilityInject(IWorldAccess.class)
	public static Capability<IWorldAccess> RELATIVE_WORLD_ACCESS_CAPABILITY = null;
	private final IWorldAccess handler;

	public WorldAccessStackProvider(IWorldAccess handler) {
		this.handler = handler;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == RELATIVE_WORLD_ACCESS_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == RELATIVE_WORLD_ACCESS_CAPABILITY
				? RELATIVE_WORLD_ACCESS_CAPABILITY.cast(handler)
				: null;
	}

	public static WorldAccessStackProvider create(ItemStack stack) {
		return new WorldAccessStackProvider(new WorldAccessStackWrapper(stack));
	}

	public static WorldAccessStackProvider create(IWorldAccess handler) {
		return new WorldAccessStackProvider(handler);
	}
}
