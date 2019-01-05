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
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static arekkuusu.implom.common.handler.ConfigHandler.GEN_CONFIG;

/**
 * Created by <Arekkuusu> on 09/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class MonolithObeliskDecorator extends BaseGen {

	private final RandomCollection<Structure> obelisks = new RandomCollection<Structure>(random)
			.add(GEN_CONFIG.monolith.obeliskDecorator.weights.obelisk_thicc, Structure.MONOLITH_OBELISK_PIECE_THICC)
			.add(GEN_CONFIG.monolith.obeliskDecorator.weights.obelisk_flat, Structure.MONOLITH_OBELISK_PIECE_FLAT);

	@Override
	void gen(World world, int x, int z, IChunkGenerator generator, IChunkProvider provider) {
		if(GEN_CONFIG.monolith.obeliskDecorator.rarity <= 0D
				|| GEN_CONFIG.monolith.obeliskDecorator.amount <= 0
				|| GEN_CONFIG.monolith.obeliskDecorator.weights.height <= 0)
			return;
		random.setSeed(world.getSeed());
		long good = random.nextLong();
		long succ = random.nextLong();

		good *= x >> 3;
		succ *= z >> 3;
		long seed = good ^ succ * world.getSeed();
		random.setSeed(seed);
		obelisks.setSeed(seed);
		//Build obelisk
		List<AxisAlignedBB> occupied = Lists.newArrayList();
		BlockPos origin = new BlockPos(x + 1, 0, z + 1);
		for(int i = 0; i < GEN_CONFIG.monolith.obeliskDecorator.amount; i++) {
			if(GEN_CONFIG.monolith.obeliskDecorator.rarity / 100D < random.nextDouble()) continue;
			boolean verticalExtremes = GEN_CONFIG.monolith.obeliskDecorator.weights.verticalExtremes / 100D > random.nextDouble();
			int pieces = random.nextInt(GEN_CONFIG.monolith.obeliskDecorator.weights.height);
			if(pieces <= 0) pieces = 1;
			//Select Structures
			List<Tuple<Structure, Template>> structures = new ArrayList<>(verticalExtremes ? pieces + 2 : pieces);
			if(verticalExtremes) {
				Structure structure = Structure.MONOLITH_OBELISK_PIECE_BOTTOM;
				structures.add(new Tuple<>(structure, structure.load(world)));
			}
			for(int j = 0; j < pieces; j++) {
				Structure structure = obelisks.next();
				structures.add(new Tuple<>(structure, structure.load(world)));
			}
			if(verticalExtremes) {
				Structure structure = Structure.MONOLITH_OBELISK_PIECE_TOP;
				structures.add(new Tuple<>(structure, structure.load(world)));
			}
			int maxX = structures.stream().map(s -> s.getSecond().getSize().getX())
					.max(Comparator.comparingInt(s -> s))
					.orElse(0);
			int maxZ = structures.stream().map(s -> s.getSecond().getSize().getZ())
					.max(Comparator.comparingInt(s -> s))
					.orElse(0);
			int totalHeight = structures.stream().mapToInt(s -> s.getSecond().getSize().getY()).sum();
			BlockPos mutableStart = origin.add(
					maxX < 13 ? random.nextInt(13 - maxX) : 0,
					0,
					maxZ < 13 ? random.nextInt(13 - maxZ) : 0
			);
			BlockPos surface = world.getTopSolidOrLiquidBlock(mutableStart);
			int burriedBlocks = surface.getY() > totalHeight ? random.nextInt(1 + totalHeight / 2) : 0;
			BlockPos finalPos = mutableStart.add(0, surface.getY() - burriedBlocks, 0);
			//Place Structures
			List<BlockPos> lootPosList = Lists.newArrayList();
			for(Tuple<Structure, Template> tuple : structures) {
				//Select Structure
				Structure structure = tuple.getFirst();
				Template template = tuple.getSecond();
				int height = template.getSize().getY();
				int widthX = template.getSize().getX();
				int widthZ = template.getSize().getZ();
				//Generate
				BlockPos mutableEnd = finalPos.add(
						widthX < maxX ? (widthX - 1) / 2 : 0,
						0,
						widthZ < maxZ ? (widthZ - 1) / 2 : 0
				);
				AxisAlignedBB obeliskBB = new AxisAlignedBB(mutableEnd, mutableEnd.add(template.getSize())).grow(1, 0, 1);
				if(occupied.stream().noneMatch(bb -> bb.intersects(obeliskBB))) {
					template.addBlocksToWorld(world, mutableEnd, new PlacementSettings());
					occupied.add(obeliskBB);
				} else continue;
				//Randomize glyphs
				switch (structure) {
					case MONOLITH_OBELISK_PIECE_THICC:
						BlockPos.MutableBlockPos start = new BlockPos.MutableBlockPos(mutableEnd.add(widthX - 1, 2, 0));
						Arrays.stream(EnumFacing.HORIZONTALS).forEach(facing -> {
							for(int j = 0; j < widthX - 1; j++) {
								IBlockState glyph = ModBlocks.MONOLITHIC_GLYPH.getDefaultState()
										.withProperty(BlockMonolithicGlyph.GLYPH, random.nextInt(16));
								world.setBlockState(start, glyph);
								start.move(facing, 1);
							}
						});
						break;
					case MONOLITH_OBELISK_PIECE_FLAT:
						BlockPos glyphPos = mutableEnd.add(1, 0, 1);
						IBlockState glyph = ModBlocks.MONOLITHIC_GLYPH.getDefaultState()
								.withProperty(BlockMonolithicGlyph.GLYPH, random.nextInt(16));
						world.setBlockState(glyphPos, glyph);
						if(GEN_CONFIG.monolith.obeliskDecorator.loot / 100D > random.nextDouble()) {
							int randX = random.nextInt(widthX);
							int randZ = random.nextInt(widthZ);
							lootPosList.add(mutableEnd.add(randX, 1, randZ));
						}
						break;
					default:
						if(GEN_CONFIG.monolith.obeliskDecorator.loot / 100D > random.nextDouble()) {
							int randX = random.nextInt(widthX);
							int randZ = random.nextInt(widthZ);
							lootPosList.add(mutableEnd.add(randX, height, randZ));
						}
						break;
				}
				//Increment obelisk height
				finalPos = finalPos.add(0, height + 1, 0);
			}
			if(GEN_CONFIG.monolith.cubeDecorator.loot > 0) {
				for(BlockPos lootPos : lootPosList) {
					IBlockState pot = ModBlocks.LARGE_POT.getDefaultState()
							.withProperty(BlockLargePot.POT_VARIANT, random.nextInt(3));
					world.setBlockState(lootPos, pot);
				}
			}
		}
	}
}
