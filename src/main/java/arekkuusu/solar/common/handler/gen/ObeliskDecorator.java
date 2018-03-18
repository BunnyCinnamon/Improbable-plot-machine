/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.gen;

import arekkuusu.solar.api.util.RandomCollection;
import arekkuusu.solar.common.block.BlockMonolithicGlyph;
import arekkuusu.solar.common.block.ModBlocks;
import com.google.common.collect.Lists;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.util.List;

import static arekkuusu.solar.common.handler.ConfigHandler.GEN_CONFIG;

/**
 * Created by <Arekkuusu> on 09/12/2017.
 * It's distributed as part of Solar.
 */
public class ObeliskDecorator extends BaseGen {

	private final RandomCollection<ModGen.Structure> obelisks = new RandomCollection<ModGen.Structure>(random)
			.add(GEN_CONFIG.monolith.decorator.weights.monolithic, ModGen.Structure.MONOLITH_OBELISK)
			.add(GEN_CONFIG.monolith.decorator.weights.fragmented, ModGen.Structure.MONOLITH_OBELISK_FRAGMENTED);

	@Override
	void gen(World world, int x, int z, IChunkGenerator generator, IChunkProvider provider) {
		random.setSeed(world.getSeed());
		long good = random.nextLong();
		long succ = random.nextLong();

		good *= x >> 3;
		succ *= z >> 3;
		random.setSeed(good * succ * world.getSeed());
		//Generate
		if(GEN_CONFIG.monolith.decorator.rarity > 0D
				&& GEN_CONFIG.monolith.decorator.rarity / 100D > random.nextDouble()) {
			List<AxisAlignedBB> occupied = Lists.newArrayList();
			for(int i = 0; i < GEN_CONFIG.monolith.decorator.size; i++) {
				BlockPos top = world.getTopSolidOrLiquidBlock(randomVector().add(x, 0, z).toBlockPos());
				int below = random.nextInt(7);
				if(top.getY() > below) {
					top = top.add(0, -below, 0);
				}
				Template obelisk = obelisks.next().load(world);
				Rotation rotation = Rotation.values()[random.nextInt(4)];
				Vector3 vec = rotate(new Vector3.WrappedVec3i(obelisk.getSize()).asImmutable(), rotation);
				vec = vec.offset(rotate(Vector3.apply(-1,-1,-1), rotation), 1);
				AxisAlignedBB obeliskBB = new AxisAlignedBB(top, top.add(vec.toBlockPos())).grow(1);
				if(occupied.stream().noneMatch(bb -> bb.intersects(obeliskBB))) {
					PlacementSettings settings = new PlacementSettings();
					settings.setRotation(rotation);
					settings.setRandom(random);
					obelisk.addBlocksToWorld(world, top, settings);
					BlockPos.getAllInBox(top, top.add(vec.toBlockPos())).forEach(p -> {
						if(random.nextFloat() < 0.05) {
							IBlockState glyph = ModBlocks.MONOLITHIC_GLYPH.getDefaultState()
									.withProperty(BlockMonolithicGlyph.GLYPH, random.nextInt(16));
							world.setBlockState(p, glyph);
						}
					});
					occupied.add(obeliskBB);
				}
			}
		}
	}

	@Deprecated
	public Vector3 rotate(Vector3 vec, Rotation rotation) { //TODO: Add this to Vector3
		switch(rotation) {
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
		double x = 4 + random.nextInt(7);
		double z = 4 + random.nextInt(7);
		return Vector3.apply(x, 0, z);
	}
}
