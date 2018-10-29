package arekkuusu.solar.api.capability.energy;

import arekkuusu.solar.api.capability.energy.data.ILumen;
import arekkuusu.solar.api.capability.quantum.IQuantum;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.Optional;

public final class LumenHelper {

	public static Optional<ILumen> getCapability(ItemStack stack) {
		return stack.hasCapability(LumenStackProvider.LUMEN_CAPABILITY, null)
				? Optional.ofNullable(stack.getCapability(LumenStackProvider.LUMEN_CAPABILITY, null))
				: Optional.empty();
	}

	public static Optional<ILumen> getCapability(TileEntity tile) {
		return getCapability(tile, null);
	}

	public static Optional<ILumen> getCapability(TileEntity tile, EnumFacing facing) {
		return tile.hasCapability(LumenStackProvider.LUMEN_CAPABILITY, facing)
				? Optional.ofNullable(tile.getCapability(LumenStackProvider.LUMEN_CAPABILITY, facing))
				: Optional.empty();
	}

	public static <T extends ILumen & IQuantum> Optional<T> getComplexCapability(ItemStack stack) {
		//noinspection unchecked
		return getCapability(stack).filter(handler -> handler instanceof IQuantum).map(handler -> (T) handler);
	}

	public static <T extends ILumen & IQuantum> Optional<T> getComplexCapability(TileEntity tile) {
		return getComplexCapability(tile, null);
	}

	public static <T extends ILumen & IQuantum> Optional<T> getComplexCapability(TileEntity tile, EnumFacing facing) {
		//noinspection unchecked
		return getCapability(tile, facing).filter(handler -> handler instanceof IQuantum).map(handler -> (T) handler);
	}

	public static void transfer(ILumen from, ILumen to, int amount, boolean exact) {
		if(from.get() > 0 && to.get() < to.getMax()) {
			int drained = from.drain(amount, false);
			int remain = to.fill(drained, false);
			if(drained > 0 && remain != amount && ((drained == amount && remain == 0) || !exact)) {
				from.drain(drained, true);
				to.fill(drained - remain, true);
			}
		}
	}
}
