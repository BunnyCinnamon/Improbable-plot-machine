package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.energy.data.ComplexLumenTileWrapper;
import arekkuusu.solar.api.capability.energy.data.IComplexLumen;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileComplexLumenBase extends TileBase {

	protected IComplexLumen handler;

	public TileComplexLumenBase() {
		this.handler = createHandler();
	}

	public abstract int getCapacity();

	public IComplexLumen createHandler() {
		return new ComplexLumenTileWrapper<>(this, getCapacity());
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY || capability == ModCapability.COMPLEX_LUMEN_CAPABILITY;
	}

	@Nullable
	@Override
	public <C> C getCapability(@Nonnull Capability<C> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY
				? ModCapability.LUMEN_CAPABILITY.cast(handler) : capability == ModCapability.COMPLEX_LUMEN_CAPABILITY
				? ModCapability.COMPLEX_LUMEN_CAPABILITY.cast((IComplexLumen) handler) : null;
	}
}
