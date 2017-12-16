/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;

import static arekkuusu.solar.common.handler.ConfigHandler.GEN_CONFIG;

/**
 * Created by <Arekkuusu> on 09/12/2017.
 * It's distributed as part of Solar.
 */
public class MonolithStructure extends BaseGen {

	@Override
	void gen(World world, int x, int z, IChunkGenerator generator, IChunkProvider provider) {
		random.setSeed(world.getSeed());
		long good = random.nextLong();
		long succ = random.nextLong();

		good *= x >> 2;
		succ *= z >> 2;
		random.setSeed(good * succ ^ world.getSeed());
		//Generate
		if(GEN_CONFIG.MONOLITH_CONFIG.MONOLITH_STRUCTURE.rarity > 0D
				&& GEN_CONFIG.MONOLITH_CONFIG.MONOLITH_STRUCTURE.rarity / 100D > random.nextDouble()) {
			BlockPos origin = new BlockPos(x, 0, z);

			BlockPos top = world.getTopSolidOrLiquidBlock(origin.add(8, 0, 8));
			BlockPos cube = origin.add(0, Math.max(top.getY() - (2 + random.nextInt(4)), 0), 0);
			ModGen.Structure.MONOLITH_CUBE_HOLLOW.generate(world, cube, new PlacementSettings());
			if(GEN_CONFIG.MONOLITH_CONFIG.MONOLITH_STRUCTURE.generateObelisks) {

			}
		}
	}
}
