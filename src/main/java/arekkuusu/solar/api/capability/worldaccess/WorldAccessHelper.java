package arekkuusu.solar.api.capability.worldaccess;

import arekkuusu.solar.api.capability.worldaccess.data.IWorldAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.Optional;

public final class WorldAccessHelper {

	public static Optional<IWorldAccess> getCapability(ItemStack stack) {
		return getCapability(stack, null);
	}

	public static Optional<IWorldAccess> getCapability(ItemStack stack, EnumFacing facing) {
		return stack.hasCapability(WorldAccessStackProvider.RELATIVE_WORLD_ACCESS_CAPABILITY, facing)
				? Optional.ofNullable(stack.getCapability(WorldAccessStackProvider.RELATIVE_WORLD_ACCESS_CAPABILITY, facing))
				: Optional.empty();
	}

	public static Optional<IWorldAccess> getCapability(TileEntity tile) {
		return getCapability(tile, null);
	}

	public static Optional<IWorldAccess> getCapability(TileEntity tile, EnumFacing facing) {
		return tile.hasCapability(WorldAccessStackProvider.RELATIVE_WORLD_ACCESS_CAPABILITY, facing)
				? Optional.ofNullable(tile.getCapability(WorldAccessStackProvider.RELATIVE_WORLD_ACCESS_CAPABILITY, facing))
				: Optional.empty();
	}
}
