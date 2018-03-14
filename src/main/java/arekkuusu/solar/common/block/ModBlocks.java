/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.common.block.tile.*;
import arekkuusu.solar.common.handler.CreativeTabHandler;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
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
	public static final Block HYPER_CONDUCTOR = PLACE_HOLDER;
	public static final Block ELECTRON = PLACE_HOLDER;
	public static final Block ASHEN = PLACE_HOLDER;
	public static final Block MONOLITHIC = PLACE_HOLDER;
	public static final Block MONOLITHIC_GLYPH = PLACE_HOLDER;
	public static final Block ANGSTROM = PLACE_HOLDER;
	public static final Block QIMRANUT = PLACE_HOLDER;
	public static final Block CELESTIAL_RESONATOR = PLACE_HOLDER;
	public static final Block LARGE_POT = PLACE_HOLDER;
	public static final Block VACUUM_CONVEYOR = PLACE_HOLDER;
	public static final Block MECHANICAL_TRANSLOCATOR = PLACE_HOLDER;
	public static final Block ALTERNATOR = PLACE_HOLDER;
	public static final Block DILATON = PLACE_HOLDER;
	public static final Block DILATON_EXTENSION = PLACE_HOLDER;
	public static final Block QELAION = PLACE_HOLDER;

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
		registry.register(new BlockHyperConductor());
		registry.register(new BlockElectron());
		registry.register(new BlockAshen());
		registry.register(new BlockMonolithic());
		registry.register(new BlockAngstrom());
		registry.register(new BlockQimranut());
		registry.register(new BlockCelestialResonator());
		registry.register(new BlockLargePot());
		registry.register(new BlockVacuumConveyor());
		registry.register(new BlockMechanicalTranslocator());
		registry.register(new BlockAlternator());
		registry.register(new BlockDilaton());
		registry.register(new BlockDilaton.BlockDilatonExtension());
		registry.register(new BlockQelaion());
		registerTiles();
	}

	private static void registerTiles() {
		registerTile(TileQuantumMirror.class, LibNames.QUANTUM_MIRROR);
		registerTile(TileGravityHopper.class, LibNames.GRAVITY_HOPPER);
		registerTile(TileBlinker.class, LibNames.BLINKER);
		registerTile(TilePhenomena.class, LibNames.PHENOMENA);
		registerTile(TileQSquared.class, LibNames.Q_SQUARED);
		registerTile(TileTheorema.class, LibNames.THEOREMA);
		registerTile(TileHyperConductor.class, LibNames.HYPER_CONDUCTOR);
		registerTile(TileQimranut.class, LibNames.QIMRANUT);
		registerTile(TileVacuumConveyor.class, LibNames.VACUUM_CONVEYOR);
		registerTile(TileMechanicalTranslocator.class, LibNames.MECHANICAL_TRANSLOCATOR);
		registerTile(TileAlternator.class, LibNames.ALTERNATOR);
		registerTile(TileDilaton.class, LibNames.DILATON);
		registerTile(TileQelaion.class, LibNames.QELAION);
	}

	private static <T extends TileEntity> void registerTile(Class<T> tile, String name) {
		GameRegistry.registerTileEntity(tile, LibMod.MOD_ID + ":" + name);
	}

	@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"}) //Shut up
	public static Block setRegistry(Block block, String id) {
		block.setUnlocalizedName(id);
		block.setRegistryName(LibMod.MOD_ID, id);
		block.setCreativeTab(CreativeTabHandler.MISC);
		return block;
	}
}
