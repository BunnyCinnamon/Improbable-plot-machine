/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 5/3/2018.
 * It's distributed as part of Solar.
 */
public abstract class TileLumenBase extends TileBase {

	protected final ILumen handler = new LumenHandler();
	protected int capacity;

	public TileLumenBase(int capacity) {
		this.capacity = capacity;
	}

	void onLumenChange() {
		//FOR RENT
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
		handler.set(compound.getInteger("lumen"));
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setInteger("lumen", handler.get());
	}

	private class LumenHandler implements ILumen {

		private int lumen;

		@Override
		public int get() {
			return lumen;
		}

		@Override
		public void set(int neutrons) {
			this.lumen = neutrons;
			markDirty();
			onLumenChange();
		}

		@Override
		public int drain(int amount) {
			if(amount > 0) {
				int contained = get();
				int drained = amount < getMax() ? amount : getMax();
				int remain = contained;
				int removed = remain < drained ? contained : drained;
				remain -= removed;
				set(remain);
				return removed;
			} else return 0;
		}

		@Override
		public int fill(int amount) {
			if(amount > 0) {
				int contained = get();
				if(contained >= getMax()) return amount;
				int sum = contained + amount;
				int remain = 0;
				if(sum > getMax()) {
					remain = sum - getMax();
					sum = getMax();
				}
				set(sum);
				return remain;
			} else return 0;
		}

		@Override
		public int getMax() {
			return capacity;
		}
	}
}
