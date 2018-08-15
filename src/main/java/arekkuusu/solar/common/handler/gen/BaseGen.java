/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.handler.gen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * Created by <Arekkuusu> on 09/12/2017.
 * It's distributed as part of Solar.
 */
public abstract class BaseGen implements IWorldGenerator {

	protected final Random random = new Random();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator generator, IChunkProvider provider) {
		if(world.provider.getDimension() == 0 && world.getWorldInfo().isMapFeaturesEnabled()) {
			gen(world, chunkX * 16, chunkZ * 16, generator, provider);
		}
	}

	abstract void gen(World world, int x, int z, IChunkGenerator generator, IChunkProvider provider);
}
