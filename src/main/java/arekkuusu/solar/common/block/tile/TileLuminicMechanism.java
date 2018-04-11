/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

/**
 * Created by <Arekkuusu> on 4/9/2018.
 * It's distributed as part of Solar.
 */
public class TileLuminicMechanism extends TileBase implements ITickable {

	private final ILumen handler;

	public TileLuminicMechanism() {
		handler = new LuminicMechanismLumenHandler(this);
	}

	@Override
	public void update() {

	}

	@Override
	void readNBT(NBTTagCompound compound) {
		handler.set(compound.getInteger("lumen"));
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setInteger("lumen", handler.get());
	}

	private static class LuminicMechanismLumenHandler implements ILumen {

		private final TileLuminicMechanism tile;
		private int lumen;

		private LuminicMechanismLumenHandler(TileLuminicMechanism tile) {
			this.tile = tile;
		}

		@Override
		public int get() {
			return lumen;
		}

		@Override
		public void set(int neutrons) {
			this.lumen = neutrons;
			tile.markDirty();
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
			return 32;
		}
	}
}
