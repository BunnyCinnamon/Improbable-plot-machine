/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/*
 * Created by <Arekkuusu> on 5/3/2018.
 * It's distributed as part of Solar.
 */
public abstract class TileLumenBase extends TileBase {

	protected final ILumen handler;

	public TileLumenBase() {
		this.handler = createHandler();
	}

	abstract void onLumenChange();

	public abstract int getCapacity();

	ILumen createHandler() {
		return new LumenHandler(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.NEUTRON_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.NEUTRON_CAPABILITY
				? ModCapability.NEUTRON_CAPABILITY.cast(handler)
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

	static class LumenHandler implements ILumen {

		private TileLumenBase tile;
		private int lumen;

		LumenHandler(TileLumenBase tile)  {
			this.tile = tile;
		}

		@Override
		public int get() {
			return lumen;
		}

		@Override
		public void set(int neutrons) {
			if(neutrons <= getMax()) {
				this.lumen = neutrons;
				tile.markDirty();
				if(tile.world != null && !tile.world.isRemote) tile.onLumenChange();
			}
		}

		@Override
		public int drain(int amount, boolean drain) {
			if(amount > 0) {
				int contained = get();
				int drained = amount < getMax() ? amount : getMax();
				int remain = contained;
				int removed = remain < drained ? contained : drained;
				remain -= removed;
				if(drain) {
					set(remain);
				}
				return removed;
			} else return 0;
		}

		@Override
		public int fill(int amount, boolean fill) {
			if(amount > 0) {
				int contained = get();
				if(contained >= getMax()) return amount;
				int sum = contained + amount;
				int remain = 0;
				if(sum > getMax()) {
					remain = sum - getMax();
					sum = getMax();
				}
				if(fill) {
					set(sum);
				}
				return remain;
			} else return 0;
		}

		@Override
		public int getMax() {
			return tile.getCapacity();
		}
	}
}
