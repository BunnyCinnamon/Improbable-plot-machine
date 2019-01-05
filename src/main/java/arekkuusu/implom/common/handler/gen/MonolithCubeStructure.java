/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler.gen;

import arekkuusu.implom.api.util.RandomCollection;
import arekkuusu.implom.common.block.BlockLargePot;
import arekkuusu.implom.common.block.BlockMonolithicGlyph;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.gen.ModGen.Structure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.util.Arrays;

import static arekkuusu.implom.common.handler.ConfigHandler.GEN_CONFIG;

/**
 * Created by <Arekkuusu> on 09/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class MonolithCubeStructure extends BaseGen {

	private final RandomCollection<Structure> cubes = new RandomCollection<ModGen.Structure>(random)
			.add(GEN_CONFIG.monolith.cubeDecorator.weights.cube_small, Structure.MONOLITH_CUBE_SMALL)
			.add(GEN_CONFIG.monolith.cubeDecorator.weights.cube_huge, Structure.MONOLITH_CUBE_HUGE);

	@Override
	void gen(World world, int x, int z, IChunkGenerator generator, IChunkProvider provider) {
		if(GEN_CONFIG.monolith.cubeDecorator.rarity <= 0D) return;
		random.setSeed(world.getSeed());
		long good = random.nextLong();
		long succ = random.nextLong();

		good *= x >> 2;
		succ *= z >> 2;
		long seed = good * succ ^ world.getSeed();
		random.setSeed(seed);
		cubes.setSeed(seed);
		//Generate
		if(GEN_CONFIG.monolith.cubeDecorator.rarity / 100D > random.nextDouble()) {
			BlockPos origin = new BlockPos(x + 1, 0, z + 1);
			//Select Monolith
			Structure structure = cubes.next();
			Template template = structure.load(world);
			int height = template.getSize().getY();
			int width = template.getSize().getX();
			//Gen Monolith
			BlockPos mutableStart = new BlockPos(origin);
			if(width < 13) {
				int randX = random.nextInt(13 - width);
				int randZ = random.nextInt(13 - width);
				mutableStart = mutableStart.add(randX, 0, randZ);
			}
			BlockPos surface = world.getTopSolidOrLiquidBlock(mutableStart);
			int burriedBlocks = surface.getY() > height ? random.nextInt(height) : 0;
			BlockPos finalPos = mutableStart.add(0, surface.getY() - burriedBlocks, 0);
			template.addBlocksToWorld(world, finalPos, new PlacementSettings());
			//Randomize glyphs
			BlockPos[] glyphPosArray;
			switch (structure) {
				case MONOLITH_CUBE_SMALL:
					glyphPosArray = new BlockPos[1];
					glyphPosArray[0] = finalPos.add(0, 3, 0);
					break;
				case MONOLITH_CUBE_HUGE:
					glyphPosArray = new BlockPos[2];
					glyphPosArray[0] = finalPos.add(0, 8, 0);
					glyphPosArray[1] = finalPos.add(0, 5, 0);
					break;
				default:
					glyphPosArray = new BlockPos[1];
					glyphPosArray[0] = finalPos.add(0, height / 2, 0);
					break;
			}
			for(BlockPos glyphPos : glyphPosArray) {
				BlockPos.MutableBlockPos start = new BlockPos.MutableBlockPos(glyphPos.add(width - 1, 0, 0));
				Arrays.stream(EnumFacing.HORIZONTALS).forEach(facing -> {
					for(int i = 0; i < width - 1; i++) {
						IBlockState glyph = ModBlocks.MONOLITHIC_GLYPH.getDefaultState()
								.withProperty(BlockMonolithicGlyph.GLYPH, random.nextInt(16));
						world.setBlockState(start, glyph);
						start.move(facing, 1);
					}
				});
			}
			//Add loot
			if(GEN_CONFIG.monolith.cubeDecorator.loot > 0) {
				BlockPos lootPos = finalPos.add(1, 1, 1);
				for(int i = 0, amount = (int) (1 + random.nextInt(width) * 1.25); i < amount; i++) {
					if(GEN_CONFIG.monolith.cubeDecorator.loot / 100D > random.nextDouble()) {
						BlockPos finalLootPos = lootPos.add(random.nextInt(width - 2), 0, random.nextInt(width - 2));
						IBlockState pot = ModBlocks.LARGE_POT.getDefaultState()
								.withProperty(BlockLargePot.POT_VARIANT, random.nextInt(3));
						world.setBlockState(finalLootPos, pot);
					}
				}
			}
		}
	}
}
