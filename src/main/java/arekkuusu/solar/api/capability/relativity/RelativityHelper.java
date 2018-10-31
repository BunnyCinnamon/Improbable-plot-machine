package arekkuusu.solar.api.capability.relativity;

import arekkuusu.solar.api.capability.relativity.data.IRelative;
import arekkuusu.solar.api.capability.relativity.data.IRelativeRedstone;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.Optional;

public final class RelativityHelper {

	public static Optional<IRelative> getCapability(ItemStack stack) {
		return getCapability(stack, null);
	}

	public static Optional<IRelative> getCapability(ItemStack stack, EnumFacing facing) {
		return stack.hasCapability(RelativityStackProvider.RELATIVE_CAPABILITY, facing)
				? Optional.ofNullable(stack.getCapability(RelativityStackProvider.RELATIVE_CAPABILITY, facing))
				: Optional.empty();
	}

	public static Optional<IRelative> getCapability(TileEntity tile) {
		return getCapability(tile, null);
	}

	public static Optional<IRelative> getCapability(TileEntity tile, EnumFacing facing) {
		return tile.hasCapability(RelativityStackProvider.RELATIVE_CAPABILITY, facing)
				? Optional.ofNullable(tile.getCapability(RelativityStackProvider.RELATIVE_CAPABILITY, facing))
				: Optional.empty();
	}

	public static Optional<IRelativeRedstone> getRedstoneCapability(ItemStack stack) {
		return getRedstoneCapability(stack, null);
	}

	public static Optional<IRelativeRedstone> getRedstoneCapability(ItemStack stack, EnumFacing facing) {
		return stack.hasCapability(RelativityStackProvider.RELATIVE_REDSTONE_CAPABILITY, facing)
				? Optional.ofNullable(stack.getCapability(RelativityStackProvider.RELATIVE_REDSTONE_CAPABILITY, facing))
				: Optional.empty();
	}

	public static Optional<IRelativeRedstone> getRedstoneCapability(TileEntity tile) {
		return getRedstoneCapability(tile, null);
	}

	public static Optional<IRelativeRedstone> getRedstoneCapability(TileEntity tile, EnumFacing facing) {
		return tile.hasCapability(RelativityStackProvider.RELATIVE_REDSTONE_CAPABILITY, facing)
				? Optional.ofNullable(tile.getCapability(RelativityStackProvider.RELATIVE_REDSTONE_CAPABILITY, facing))
				: Optional.empty();
	}
}
