/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.energy.data.ILumen;
import arekkuusu.solar.api.capability.energy.data.SimpleLumenTileWrapper;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/*
 * Created by <Arekkuusu> on 5/3/2018.
 * It's distributed as part of Solar.
 */
public abstract class TileSimpleLumenBase extends TileBase {

	protected final ILumen handler;

	public TileSimpleLumenBase() {
		this.handler = createHandler();
	}

	abstract void onLumenChange();

	public abstract int getCapacity();

	ILumen createHandler() {
		return new SimpleLumenTileWrapper<TileSimpleLumenBase>(this, getCapacity()) {
			@Override
			public void set(int neutrons) {
				super.set(neutrons);
				onLumenChange();
			}
		};
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY
				? ModCapability.LUMEN_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		handler.set(compound.getInteger(ILumen.NBT_TAG));
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setInteger(ILumen.NBT_TAG, handler.get());
	}
}
