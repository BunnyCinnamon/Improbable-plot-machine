/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * Created by <Arekkuusu> on 25/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class BlockSchrodingerGlyph extends BlockBase {

	public BlockSchrodingerGlyph() {
		super(LibNames.SCHRODINGER_GLYPH, Material.ROCK);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(4F);
		setResistance(2000F);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		world.scheduleUpdate(pos, this, tickRate(world));
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}
}
