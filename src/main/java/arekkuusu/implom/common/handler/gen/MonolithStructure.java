/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler.gen;

import arekkuusu.implom.common.block.BlockLargePot;
import arekkuusu.implom.common.block.BlockMonolithicGlyph;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.gen.ModGen.Structure;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;

import java.util.Arrays;

import static arekkuusu.implom.common.handler.ConfigHandler.GEN_CONFIG;

/**
 * Created by <Arekkuusu> on 09/12/2017.
 * It's distributed as part of Improbable plot machine.
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
		if (GEN_CONFIG.monolith.structure.rarity > 0D
				&& GEN_CONFIG.monolith.structure.rarity / 100D > random.nextDouble()) {
			BlockPos origin = new BlockPos(x, 0, z);
			//Gen Monolith
			BlockPos surface = world.getTopSolidOrLiquidBlock(origin.add(8, 0, 8));
			BlockPos pos = origin.add(5, Math.max(surface.getY() - (5 + random.nextInt(3)), 8), 4);
			Structure.MONOLITH_CUBE.generate(world, pos.down(7), new PlacementSettings());
			//Randomize glyphs
			BlockPos.MutableBlockPos start = new BlockPos.MutableBlockPos(pos.getX(), pos.getY() - 4, pos.getZ() + 6);
			Arrays.stream(EnumFacing.HORIZONTALS).forEach(facing -> {
				for (int i = 0; i < 6; i++) {
					IBlockState glyph = ModBlocks.MONOLITHIC_GLYPH.getDefaultState()
							.withProperty(BlockMonolithicGlyph.GLYPH, random.nextInt(16));
					world.setBlockState(start, glyph);
					start.move(facing.getOpposite(), 1);
				}
			});
			//Add loot
			BlockPos loot = pos.down(7).add(1, 1, 1);
			for (int i = 0; i < 6 + random.nextInt(6); i++) {
				if (GEN_CONFIG.monolith.structure.loot / 100D > random.nextDouble()) {
					BlockPos inside = loot.add(random.nextInt(4), 0, random.nextInt(4));
					IBlockState pot = ModBlocks.LARGE_POT.getDefaultState()
							.withProperty(BlockLargePot.POT_VARIANT, random.nextInt(3));
					world.setBlockState(inside, pot);
				}
			}
			//Gen ruin
			if(GEN_CONFIG.monolith.structure.ruined > 0) {
				int size = (GEN_CONFIG.monolith.structure.ruined / 100) * 32;
				size += random.nextInt((int) (((double) GEN_CONFIG.monolith.structure.ruined / 100D) * 64D));
				for(int i = 0; i < size; i++) {
					BlockPos top = world.getTopSolidOrLiquidBlock(randomVector().add(x, 1, z).toBlockPos());
					int below = random.nextInt(3);
					if(top.getY() > below) {
						top = top.add(0, -below, 0);
					}
					world.setBlockState(top, ModBlocks.ASHEN.getDefaultState());
				}
			}
		}
	}

	private Vector3 randomVector() {
		double x = 8D + (6D * (random.nextDouble() * 2D - 1D));
		double z = 8D + (6D * (random.nextDouble() * 2D - 1D));
		return Vector3.apply(x, 0, z);
	}
}
