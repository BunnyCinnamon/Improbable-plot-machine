/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.gen;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.common.handler.gen.ModGen.Structure;
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
			//Gen Monolith
			BlockPos floor = world.getTopSolidOrLiquidBlock(origin.add(8, 0, 8));
			BlockPos cube = origin.add(5, Math.max(floor.getY() - (14 + random.nextInt(5)), 1), 4);
			Structure.MONOLITH_CUBE.generate(world, cube, new PlacementSettings());
			//Gen ruin
			if(GEN_CONFIG.MONOLITH_CONFIG.MONOLITH_STRUCTURE.size >= 1) {
				Structure.MONOLITH_RUIN.generate(world, origin.add(5, Math.max(floor.getY() - (1 + random.nextInt(3)), 1), 4), new PlacementSettings());
			}
			//Gen sub ruins
			for(int i = 0; i < GEN_CONFIG.MONOLITH_CONFIG.MONOLITH_STRUCTURE.size; i++) {
				BlockPos top = world.getTopSolidOrLiquidBlock(randomVector().add(x, 0, z).toBlockPos());
				int below = random.nextInt(3);
				if(top.getY() > below) {
					top = top.add(0, -below, 0);
				}
				PlacementSettings settings = new PlacementSettings();
				settings.setIntegrity(random.nextFloat());
				settings.setRandom(random);
				Structure.MONOLITH_RUIN_.generate(world, top, settings);
			}
		}
	}

	private Vector3 randomVector() {
		double x = 7D + (4D * (random.nextDouble() * 2D - 1D));
		double y = GEN_CONFIG.MONOLITH_CONFIG.MONOLITH_STRUCTURE.spread * (random.nextDouble() * 2D - 1D);
		double z = 7D + (4D * (random.nextDouble() * 2D - 1D));
		return Vector3.create(x, y, z);
	}
}
