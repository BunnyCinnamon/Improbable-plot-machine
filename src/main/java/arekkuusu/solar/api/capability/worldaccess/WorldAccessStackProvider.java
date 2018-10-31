package arekkuusu.solar.api.capability.worldaccess;

import arekkuusu.solar.api.capability.worldaccess.data.IWorldAccess;
import arekkuusu.solar.api.capability.worldaccess.data.WorldAccessStackWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
