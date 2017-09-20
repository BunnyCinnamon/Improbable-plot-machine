/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

/**
 * Created by <Arekkuusu> on 19/09/2017.
 * It's distributed as part of Solar.
 */
public class TileQSquared extends TileBase implements ITickable {

	public int tick;

	@Override
	public void onLoad() {
		tick =  world.rand.nextInt(15);
	}

	@Override
	public void update() {
		tick++;
	}

	@Override
	void readNBT(NBTTagCompound cmp) {

	}

	@Override
	void writeNBT(NBTTagCompound cmp) {

	}
}
