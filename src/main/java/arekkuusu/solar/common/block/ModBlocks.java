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
public final class ModBlocks {

	private static final Block PLACE_HOLDER = new Block(Material.AIR);
	//--------------------------------Blocks--------------------------------//
	public static final Block PRIMAL_STONE = PLACE_HOLDER;
	public static final Block QUANTUM_MIRROR = PLACE_HOLDER;
	public static final Block GRAVITY_HOPPER = PLACE_HOLDER;
	public static final Block SCHRODINGER_GLYPH = PLACE_HOLDER;
	public static final Block BLINKER = PLACE_HOLDER;
	public static final Block PHENOMENA = PLACE_HOLDER;
	public static final Block Q_SQUARED = PLACE_HOLDER;
	public static final Block THEOREMA = PLACE_HOLDER;
	public static final Block GRAVITY_INHIBITOR = PLACE_HOLDER;
	public static final Block HYPER_CONDUCTOR = PLACE_HOLDER;
	public static final Block ELECTRON = PLACE_HOLDER;
	public static final Block ASHEN = PLACE_HOLDER;
	public static final Block MONOLITHIC = PLACE_HOLDER;
	public static final Block MONOLITHIC_GLYPH = PLACE_HOLDER;
	public static final Block QUINGENTILLIARD = PLACE_HOLDER;
	public static final Block ANGSTROM = PLACE_HOLDER;
	public static final Block QIMRANUT = PLACE_HOLDER;
	public static final Block CELESTIAL_RESONATOR = PLACE_HOLDER;
	public static final Block LARGE_POT = PLACE_HOLDER;

	public static void register(IForgeRegistry<Block> registry) {
		registry.register(new BlockBase(LibNames.PRIMAL_STONE, Material.ROCK).setHardness(4F).setResistance(2000F));
		registry.register(new BlockMonolithicGlyph());
		registry.register(new BlockQuantumMirror());
		registry.register(new BlockGravityHopper());
		registry.register(new BlockSchrodingerGlyph());
		registry.register(new BlockBlinker());
		registry.register(new BlockPhenomena());
		registry.register(new BlockQSquared());
		registry.register(new BlockTheorema());
		registry.register(new BlockGravityInhibitor());
		registry.register(new BlockHyperConductor());
		registry.register(new BlockElectron());
		registry.register(new BlockAshen());
		registry.register(new BlockMonolithic());
		registry.register(new BlockQuingentilliard());
		registry.register(new BlockAngstrom());
		registry.register(new BlockQimranut());
		registry.register(new BlockCelestialResonator());
		registry.register(new BlockLargePot());
		registerTiles();
	}

	private static void registerTiles() {
		GameRegistry.registerTileEntity(TileQuantumMirror.class, LibMod.MOD_ID + ":quantum_mirror");
		GameRegistry.registerTileEntity(TileGravityHopper.class, LibMod.MOD_ID + ":gravity_hopper");
		GameRegistry.registerTileEntity(TileBlinker.class, LibMod.MOD_ID + ":blinker");
		GameRegistry.registerTileEntity(TilePhenomena.class, LibMod.MOD_ID + ":phenomena");
		GameRegistry.registerTileEntity(TileQSquared.class, LibMod.MOD_ID + ":q_squared");
		GameRegistry.registerTileEntity(TileTheorema.class, LibMod.MOD_ID + ":theorema");
		GameRegistry.registerTileEntity(TileGravityInhibitor.class, LibMod.MOD_ID + ":gravity_inhibitor");
		GameRegistry.registerTileEntity(TileHyperConductor.class, LibMod.MOD_ID + ":hyper_conductor");
		GameRegistry.registerTileEntity(TileElectron.class, LibMod.MOD_ID + ":electron");
		GameRegistry.registerTileEntity(TileQuingentilliard.class, LibMod.MOD_ID + ":quingentilliard");
		GameRegistry.registerTileEntity(TileQimranut.class, LibMod.MOD_ID + ":qimranut");
		GameRegistry.registerTileEntity(TileCelestialResonator.class, LibMod.MOD_ID + ":celestial_resonator");
	}
}
