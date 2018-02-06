/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.gen;

import arekkuusu.solar.api.util.RandomCollection;
import arekkuusu.solar.api.util.Vector3;
import arekkuusu.solar.common.block.BlockLargePot;
import arekkuusu.solar.common.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import static arekkuusu.solar.common.handler.ConfigHandler.GEN_CONFIG;
import static arekkuusu.solar.common.handler.gen.ModGen.Structure;

/**
 * Created by <Arekkuusu> on 01/11/2017.
 * It's distributed as part of Solar.
 */
public class AshenCubeStructure extends BaseGen {

	private final RandomCollection<Structure> nuggets = new RandomCollection<Structure>(random)
			.add(GEN_CONFIG.ASHEN_CUBE_STRUCTURE.WEIGHTS.big, Structure.ASHEN_NUGGET_BIG)
			.add(GEN_CONFIG.ASHEN_CUBE_STRUCTURE.WEIGHTS.small, Structure.ASHEN_NUGGET_SMALL)
			.add(GEN_CONFIG.ASHEN_CUBE_STRUCTURE.WEIGHTS.spawn, Structure.ASHEN_NUGGET_SPAWN);

	@Override
	void gen(World world, int x, int z, IChunkGenerator generator, IChunkProvider provider) {
		random.setSeed(world.getSeed());
		long good = random.nextLong();
		long succ = random.nextLong();

		good *= x >> 1;
		succ *= z >> 1;
		random.setSeed(good ^ succ ^ world.getSeed());
		//Generate
		if(GEN_CONFIG.ASHEN_CUBE_STRUCTURE.rarity > 0D && GEN_CONFIG.ASHEN_CUBE_STRUCTURE.rarity / 100D > random.nextDouble()) {
			BlockPos center = new BlockPos(x, 15 + random.nextInt(25), z);
			if(!world.canSeeSky(center) || !GEN_CONFIG.ASHEN_CUBE_STRUCTURE.underground) {
				genCubes(world, center);
			}
		}
	}

	private void genCubes(World world, BlockPos pos) {
		//Gen Cube
		BlockPos origin = pos.add(5, 0, 5);
		Template template = Structure.ASHEN_CUBE.load(world);
		boolean loot = GEN_CONFIG.ASHEN_CUBE_STRUCTURE.loot / 100D > random.nextDouble();
		PlacementSettings integrity = new PlacementSettings();
		integrity.setIntegrity(loot ? 1F : 0.35F + 0.45F * random.nextFloat());
		template.addBlocksToWorld(world, origin, integrity);
		integrity.setIntegrity(!loot && random.nextFloat() > 0.45F ? 1F : random.nextFloat());
		Structure.ASHEN_CUBE_.generate(world, origin, integrity);
		//Add loot
		for (int i = 0; i < 6 + random.nextInt(6); i++) {
			loot = GEN_CONFIG.MONOLITH_CONFIG.MONOLITH_STRUCTURE.loot / 100D > random.nextDouble();
			if (loot) {
				BlockPos inside = origin.add(1 + random.nextInt(4), 1, 1 + random.nextInt(4));
				IBlockState pot = ModBlocks.LARGE_POT.getDefaultState().withProperty(BlockLargePot.POT_VARIANT, random.nextInt(3));
				world.setBlockState(inside, pot);
			}
		}
		//Gen Cubes
		AxisAlignedBB cubeBB = new AxisAlignedBB(origin, origin.add(template.getSize()));
		for(int i = 0; i < GEN_CONFIG.ASHEN_CUBE_STRUCTURE.size; i++) {
			Template cube = nuggets.next().load(world);
			Rotation rotation = Rotation.values()[random.nextInt(4)];
			Vector3 vec = Vector3.create(cube.getSize()).rotate(rotation);
			BlockPos offset = randomVector().add(pos).toBlockPos();
			if(offset.getY() < 1  || (world.canSeeSky(offset) && GEN_CONFIG.ASHEN_CUBE_STRUCTURE.underground)) continue;
			AxisAlignedBB nuggetBB = new AxisAlignedBB(offset, vec.add(offset).toBlockPos());
			if(!nuggetBB.intersects(cubeBB)) {
				PlacementSettings settings = new PlacementSettings();
				settings.setIntegrity(random.nextFloat() > 0.85F ? 0.9F : 0.35F + 0.45F * random.nextFloat());
				settings.setRotation(rotation);
				settings.setRandom(random);
				cube.addBlocksToWorld(world, offset, settings);
			}
		}
	}

	private Vector3 randomVector() {
		double x = 7D + (4D * (random.nextDouble() * 2D - 1D));
		double y = GEN_CONFIG.ASHEN_CUBE_STRUCTURE.spread * (random.nextDouble() * 2D - 1D);
		double z = 7D + (4D * (random.nextDouble() * 2D - 1D));
		return Vector3.create(x, y, z);
	}
}
