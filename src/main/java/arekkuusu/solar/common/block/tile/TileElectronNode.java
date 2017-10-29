/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Created by <Arekkuusu> on 27/10/2017.
 * It's distributed as part of Solar.
 */
public class TileElectronNode extends TileBase {

	@Override
	public void onLoad() {
		if(!world.isRemote) {
			BlockPos vec = new BlockPos(16, 16, 16);

			BlockPos from = pos.add(vec);
			BlockPos to = pos.subtract(vec);
			BlockPos.getAllInBox(from, to).forEach(p -> {
				getTile(TileHyperConductor.class, world, p).ifPresent(conductor -> {
					double distance = conductor.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
					if(distance <= 16D) {
						conductor.addElectron(world.getBlockState(pos), pos);
					}
				});
			});
		}
	}

	@Override
	void readNBT(NBTTagCompound compound) {

	}

	@Override
	void writeNBT(NBTTagCompound compound) {

	}
}
