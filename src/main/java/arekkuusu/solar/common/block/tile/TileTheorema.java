/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 22/09/2017.
 * It's distributed as part of Solar.
 */
public class TileTheorema extends TileBase {

	@SideOnly(Side.CLIENT)
	public boolean shouldRenderFace(EnumFacing facing) {
		return getBlockType().getDefaultState().shouldSideBeRendered(world, pos, facing);
	}

	@Override
	void readNBT(NBTTagCompound cmp) {

	}

	@Override
	void writeNBT(NBTTagCompound cmp) {

	}
}
