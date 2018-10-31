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
import arekkuusu.implom.common.block.ModBlocks;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import static arekkuusu.implom.common.handler.ConfigHandler.GEN_CONFIG;
import static arekkuusu.implom.common.handler.gen.ModGen.Structure;

/**
 * Created by <Arekkuusu> on 01/11/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class AshenCubeStructure extends BaseGen {

	private final RandomCollection<Structure> nuggets = new RandomCollection<Structure>(random)
			.add(GEN_CONFIG.ashen_cube.weights.big, Structure.ASHEN_NUGGET_BIG)
			.add(GEN_CONFIG.ashen_cube.weights.small, Structure.ASHEN_NUGGET_SMALL)
			.add(GEN_CONFIG.ashen_cube.weights.spawn, Structure.ASHEN_NUGGET_SPAWN);

	@Override
	void gen(World world, int x, int z, IChunkGenerator generator, IChunkProvider provider) {
		random.setSeed(world.getSeed());
		long good = random.nextLong();
		long succ = random.nextLong();

		good *= x >> 1;
		succ *= z >> 1;
		random.setSeed(good ^ succ ^ world.getSeed());
		//Generate
		if(GEN_CONFIG.ashen_cube.rarity > 0D && GEN_CONFIG.ashen_cube.rarity / 100D > random.nextDouble()) {
			BlockPos center = new BlockPos(x, 15 + random.nextInt(25), z);
			if(!world.canSeeSky(center) || !GEN_CONFIG.ashen_cube.underground) {
				genCubes(world, center);
			}
		}
	}

	private void genCubes(World world, BlockPos pos) {
		//Gen Cube
		BlockPos origin = pos.add(5, 0, 5);
		Template template = Structure.ASHEN_CUBE.load(world);
		boolean loot = GEN_CONFIG.ashen_cube.loot / 100D > random.nextDouble();
		PlacementSettings integrity = new PlacementSettings();
		integrity.setIntegrity(loot ? 1F : 0.35F + 0.45F * random.nextFloat());
		template.addBlocksToWorld(world, origin, integrity);
		integrity.setIntegrity(!loot && random.nextFloat() > 0.45F ? 1F : random.nextFloat());
		Structure.ASHEN_CUBE_.generate(world, origin, integrity);
		//Add loot
		for(int i = 0; i < 6 + random.nextInt(6); i++) {
			loot = GEN_CONFIG.monolith.structure.loot / 100D > random.nextDouble();
			if(loot) {
				BlockPos inside = origin.add(1 + random.nextInt(4), 1, 1 + random.nextInt(4));
				IBlockState pot = ModBlocks.LARGE_POT.getDefaultState().withProperty(BlockLargePot.POT_VARIANT, random.nextInt(3));
				world.setBlockState(inside, pot);
			}
		}
		//Gen Cubes
		AxisAlignedBB cubeBB = new AxisAlignedBB(origin, origin.add(template.getSize()));
		for(int i = 0; i < GEN_CONFIG.ashen_cube.size; i++) {
			Template cube = nuggets.next().load(world);
			Rotation rotation = Rotation.values()[random.nextInt(4)];
			Vector3 vec = rotate(new Vector3.WrappedVec3i(cube.getSize()).asImmutable(), rotation);
			BlockPos offset = pos.add(randomVector().toBlockPos());
			if(offset.getY() < 1 || (world.canSeeSky(offset) && GEN_CONFIG.ashen_cube.underground)) continue;
			AxisAlignedBB nuggetBB = new AxisAlignedBB(offset, vec.add(new Vector3.WrappedVec3i(offset)).toBlockPos());
			if(!nuggetBB.intersects(cubeBB)) {
				PlacementSettings settings = new PlacementSettings();
				settings.setIntegrity(random.nextFloat() > 0.85F ? 0.9F : 0.35F + 0.45F * random.nextFloat());
				settings.setRotation(rotation);
				settings.setRandom(random);
				cube.addBlocksToWorld(world, offset, settings);
			}
		}
	}

	public Vector3 rotate(Vector3 vec, Rotation rotation) {
		switch (rotation) {
			case NONE:
			default:
				return vec;
			case CLOCKWISE_90:
				return Vector3.apply(-vec.z(), vec.y(), vec.x());
			case CLOCKWISE_180:
				return Vector3.apply(-vec.x(), vec.y(), -vec.z());
			case COUNTERCLOCKWISE_90:
				return Vector3.apply(vec.z(), vec.y(), -vec.x());
		}
	}

	private Vector3 randomVector() {
		double x = 7D + (4D * (random.nextDouble() * 2D - 1D));
		double y = GEN_CONFIG.ashen_cube.spread * (random.nextDouble() * 2D - 1D);
		double z = 7D + (4D * (random.nextDouble() * 2D - 1D));
		return Vector3.apply(x, y, z);
	}
}
