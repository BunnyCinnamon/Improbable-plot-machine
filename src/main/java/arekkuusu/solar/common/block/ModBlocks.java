/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.common.block.tile.*;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar.
 */
@ObjectHolder(LibMod.MOD_ID)
public class ModBlocks {

	private static final Block PLACE_HOLDER = new Block(Material.AIR);
	//--------------------------------Blocks--------------------------------//
	public static final Block primal_stone = PLACE_HOLDER;
	public static final Block primal_glyph = PLACE_HOLDER;
	public static final Block singularity = PLACE_HOLDER;
	public static final Block prism_flower = PLACE_HOLDER;
	public static final Block quantum_mirror = PLACE_HOLDER;
	public static final Block gravity_hopper = PLACE_HOLDER;
	public static final Block schrodinger_glyph = PLACE_HOLDER;
	public static final Block crystal_void = PLACE_HOLDER;
	public static final Block blinker = PLACE_HOLDER;
	public static final Block phenomena = PLACE_HOLDER;
	public static final Block q_squared = PLACE_HOLDER;

	public static void register(IForgeRegistry<Block> registry) {
		registry.register(new BlockBase(LibNames.PRIMAL_STONE, Material.ROCK).setHardness(4F).setResistance(2000F));
		registry.register(new BlockPrimalGlyph());
		registry.register(new BlockSingularity());
		registry.register(new BlockPrismFlower());
		registry.register(new BlockQuantumMirror());
		registry.register(new BlockGravityHopper());
		registry.register(new BlockSchrodingerGlyph());
		registry.register(new BlockCrystalVoid());
		registry.register(new BlockBlinker());
		registry.register(new BlockPhenomena());
		registry.register(new BlockQSquared());
		registerTiles();
	}

	private static void registerTiles() {
		GameRegistry.registerTileEntity(TileSingularity.class, LibMod.MOD_ID + ":singularity");
		GameRegistry.registerTileEntity(TilePrismFlower.class, LibMod.MOD_ID + ":prism_flower");
		GameRegistry.registerTileEntity(TileQuantumMirror.class, LibMod.MOD_ID + ":quantum_mirror");
		GameRegistry.registerTileEntity(TileGravityHopper.class, LibMod.MOD_ID + ":gravity_hopper");
		GameRegistry.registerTileEntity(TileCrystalVoid.class, LibMod.MOD_ID + ":crystal_void");
		GameRegistry.registerTileEntity(TileBlinker.class, LibMod.MOD_ID + ":blinker");
		GameRegistry.registerTileEntity(TilePhenomena.class, LibMod.MOD_ID + ":phenomena");
		GameRegistry.registerTileEntity(TileQSquared.class, LibMod.MOD_ID + ":q_squared");
		GameRegistry.registerTileEntity(RenderDummy.Quingentilliard.class,LibMod.MOD_ID + ":quingentilliard_dummy");
	}
}
