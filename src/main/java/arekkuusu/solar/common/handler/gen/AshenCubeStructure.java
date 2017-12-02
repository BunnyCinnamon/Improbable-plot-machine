/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.gen;

import arekkuusu.solar.api.helper.RandomCollection;
import arekkuusu.solar.api.helper.Vector3;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

import static arekkuusu.solar.common.handler.ConfigHandler.GEN_CONFIG;
import static arekkuusu.solar.common.handler.gen.ModGen.Structure;

/**
 * Created by <Arekkuusu> on 01/11/2017.
 * It's distributed as part of Solar.
 */
public class AshenCubeStructure implements IWorldGenerator {

	private final Random random = new Random();
	private final RandomCollection<Structure> structures = new RandomCollection<Structure>(random)
			.add(GEN_CONFIG.ASHEN_CUBE_STRUCTURE.WEIGHTS.big, Structure.ASHEN_NUGGET_BIG)
			.add(GEN_CONFIG.ASHEN_CUBE_STRUCTURE.WEIGHTS.small, Structure.ASHEN_NUGGET_SMALL)
			.add(GEN_CONFIG.ASHEN_CUBE_STRUCTURE.WEIGHTS.spawn, Structure.ASHEN_NUGGET_SPAWN);

	@Override
	public void generate(Random r, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == 0 && world.getWorldInfo().isMapFeaturesEnabled()) {
			random.setSeed(world.getSeed());
			long good = random.nextLong();
			long succ = random.nextLong();

			good *= (chunkX * 16) >> 1;
			succ *= (chunkZ * 16) >> 1;
			random.setSeed(good ^ succ ^ world.getSeed());
			if(GEN_CONFIG.ASHEN_CUBE_STRUCTURE.rarity > 0D && GEN_CONFIG.ASHEN_CUBE_STRUCTURE.rarity / 100D > random.nextDouble()) {
				BlockPos center = new BlockPos(chunkX * 16, 15 + random.nextInt(25), chunkZ * 16);
				if(!world.canSeeSky(center) || !GEN_CONFIG.ASHEN_CUBE_STRUCTURE.underground) {
					genCubes(world, center);
				}
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
		if(loot) {
			BlockPos center = origin.add(
					template.getSize().getX() / 2,
					template.getSize().getY() / 2,
					template.getSize().getZ() / 2
			);
			Rotation rotation = Rotation.values()[random.nextInt(4)];
			world.setBlockState(center, Blocks.CHEST.getDefaultState().withRotation(rotation));
			TileEntity tile = world.getTileEntity(center);
			if(tile instanceof TileEntityChest) {
				((TileEntityChest) tile).setLootTable(ModGen.ASHEN_CUBE_LOOT, world.getSeed());
			}
		}
		//Gen Cubes
		AxisAlignedBB cubeBB = new AxisAlignedBB(origin, origin.add(template.getSize()));
		for(int i = 0; i < GEN_CONFIG.ASHEN_CUBE_STRUCTURE.size; i++) {
			Template cube = structures.next().load(world);
			Rotation rotation = Rotation.values()[random.nextInt(4)];
			Vector3 vec = new Vector3(cube.getSize()).rotate(rotation);
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
		return new Vector3(x, y, z);
	}
}
