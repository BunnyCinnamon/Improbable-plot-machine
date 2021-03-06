/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.common.block.base.BlockBaseMultiblock;
import arekkuusu.implom.common.block.base.BlockBaseMultiblockGlass;
import arekkuusu.implom.common.block.fluid.ModFluids;
import arekkuusu.implom.common.block.tile.*;
import arekkuusu.implom.common.handler.CreativeTabHandler;
import arekkuusu.implom.common.lib.LibMod;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

/*
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@ObjectHolder(LibMod.MOD_ID)
public final class ModBlocks {

	private static final Block PLACE_HOLDER = new Block(Material.AIR);
	//--------------------------------Blocks--------------------------------//
	public static final Block QUANTUM_MIRROR = PLACE_HOLDER;
	public static final Block GRAVITY_HOPPER = PLACE_HOLDER;
	public static final Block MONOLITHIC_EYE = PLACE_HOLDER;
	public static final Block BLINKER = PLACE_HOLDER;
	public static final Block PHENOMENA = PLACE_HOLDER;
	public static final Block QUANTA = PLACE_HOLDER;
	public static final Block HYPER_CONDUCTOR = PLACE_HOLDER;
	public static final Block ELECTRON = PLACE_HOLDER;
	public static final Block MONOLITHIC = PLACE_HOLDER;
	public static final Block MONOLITHIC_GLYPH = PLACE_HOLDER;
	public static final Block ANGSTROM = PLACE_HOLDER;
	public static final Block QIMRANUT = PLACE_HOLDER;
	public static final Block LARGE_POT = PLACE_HOLDER;
	public static final Block VACUUM_CONVEYOR = PLACE_HOLDER;
	public static final Block MECHANICAL_TRANSLOCATOR = PLACE_HOLDER;
	public static final Block ALTERNATOR = PLACE_HOLDER;
	public static final Block DILATON = PLACE_HOLDER;
	public static final Block DILATON_EXTENSION = PLACE_HOLDER;
	public static final Block QELAION = PLACE_HOLDER;
	public static final Block NEUTRON_BATTERY = PLACE_HOLDER;
	public static final Block PHOLARIZER = PLACE_HOLDER;
	public static final Block FISSION_INDUCER = PLACE_HOLDER;
	public static final Block COMPRESSOR = PLACE_HOLDER;
	public static final Block MONOLITHIC_CONSUMER = PLACE_HOLDER;
	public static final Block SYMMETRICAL_MACHINATION = PLACE_HOLDER;
	public static final Block ASYMMETRICAL_MACHINATION = PLACE_HOLDER;
	public static final Block KONDENZATOR = PLACE_HOLDER;
	public static final Block IMBUED_QUARTZ = PLACE_HOLDER;
	public static final Block MUTATOR = PLACE_HOLDER;
	public static final Block FIRE_CLAY_BLOCK = PLACE_HOLDER;
	public static final Block FIRE_BRICK_BLOCK = PLACE_HOLDER;
	public static final Block FIRE_BRICKS = PLACE_HOLDER;
	public static final Block FIRE_BRICKS_GLASS = PLACE_HOLDER;
	public static final Block FIRE_BRICKS_WINDOW = PLACE_HOLDER;
	public static final Block BLAST_FURNACE_CONTROLLER = PLACE_HOLDER;
	public static final Block BLAST_FURNACE_DRAIN = PLACE_HOLDER;
	public static final Block BLAST_FURNACE_FILTER = PLACE_HOLDER;
	public static final Block BLAST_FURNACE_INPUT = PLACE_HOLDER;
	public static final Block BLAST_FURNACE_TUYERE = PLACE_HOLDER;
	public static final Block BLAST_FURNACE_AIR_VENT = PLACE_HOLDER;
	public static final Block BLAST_FURNACE_AIR_PUMP = PLACE_HOLDER;
	public static final Block BLAST_FURNACE_PIPE = PLACE_HOLDER;
	public static final Block HOT_BLAST_HEATER = PLACE_HOLDER;
	public static final Block BLAST_FURNACE_PIPE_GAUGE = PLACE_HOLDER;

	public static void register(IForgeRegistry<Block> registry) {
		ModFluids.FLUIDS.forEach(registry::register);
		registry.register(new BlockMonolithicGlyph());
		registry.register(new BlockQuantumMirror());
		registry.register(new BlockGravityHopper());
		registry.register(new BlockMonolithicEye());
		registry.register(new BlockBlinker());
		registry.register(new BlockPhenomena());
		registry.register(new BlockQuanta());
		registry.register(new BlockHyperConductor());
		registry.register(new BlockElectron());
		registry.register(new BlockMonolithic());
		registry.register(new BlockAngstrom());
		registry.register(new BlockQimranut());
		registry.register(new BlockLargePot());
		registry.register(new BlockVacuumConveyor());
		registry.register(new BlockMechanicalTranslocator());
		registry.register(new BlockAlternator());
		registry.register(new BlockDilaton());
		registry.register(new BlockDilaton.BlockDilatonExtension());
		registry.register(new BlockQelaion());
		registry.register(new BlockNeutronBattery());
		registry.register(new BlockPholarizer());
		registry.register(new BlockFissionInducer());
		registry.register(new BlockCompressor());
		registry.register(new BlockMonolithicConsumer());
		registry.register(new BlockSymmetricalMachination());
		registry.register(new BlockAsymmetricalMachination());
		registry.register(new BlockKondenzator());
		registry.register(new BlockImbuedQuartz());
		registry.register(new BlockMutator());
		registry.register(new BlockFireClayBricks());
		registry.register(new BlockBaseMultiblock(LibNames.FIRE_BRICK_BLOCK, Material.ROCK));
		registry.register(new BlockBaseMultiblock(LibNames.FIRE_BRICKS, Material.ROCK));
		registry.register(new BlockBaseMultiblockGlass(LibNames.FIRE_BRICKS_GLASS, Material.GLASS));
		registry.register(new BlockBaseMultiblockGlass(LibNames.FIRE_BRICKS_WINDOW, Material.GLASS));
		registry.register(new BlockBlastFurnaceController());
		registry.register(new BlockBlastFurnaceDrain());
		registry.register(new BlockBlastFurnaceFilter());
		registry.register(new BlockBlastFurnaceInput());
		registry.register(new BlockBlastFurnaceTuyere());
		registry.register(new BlockHotBlastAirVent());
		registry.register(new BlockHotBlastAirPump());
		registry.register(new BlockBlastFurnacePipe());
		registry.register(new BlockHotBlastHeater());
		registry.register(new BlockBlastFurnacePipeGauge());
		registerTiles();
	}

	private static void registerTiles() {
		registerTile(TileQuantumMirror.class, LibNames.QUANTUM_MIRROR);
		registerTile(TileGravityHopper.class, LibNames.GRAVITY_HOPPER);
		registerTile(TileBlinker.class, LibNames.BLINKER);
		registerTile(TilePhenomena.class, LibNames.PHENOMENA);
		registerTile(TileQuanta.class, LibNames.QUANTA);
		registerTile(TileHyperConductor.class, LibNames.HYPER_CONDUCTOR);
		registerTile(TileQimranut.class, LibNames.QIMRANUT);
		registerTile(TileVacuumConveyor.class, LibNames.VACUUM_CONVEYOR);
		registerTile(TileMechanicalTranslocator.class, LibNames.MECHANICAL_TRANSLOCATOR);
		registerTile(TileAlternator.class, LibNames.ALTERNATOR);
		registerTile(TileDilaton.class, LibNames.DILATON);
		registerTile(TileQelaion.class, LibNames.QELAION);
		registerTile(TileNeutronBattery.class, LibNames.NEUTRON_BATTERY);
		registerTile(TilePholarizer.class, LibNames.PHOLARIZER);
		registerTile(TileFissionInducer.class, LibNames.FISSION_INDUCER);
		registerTile(TileElectron.class, LibNames.ELECTRON);
		registerTile(TileQuartzConsumer.class, LibNames.MONOLITHIC_CONSUMER);
		registerTile(TileSymmetricalMachination.class, LibNames.SYMMETRICAL_MACHINATION);
		registerTile(TileAsymmetricalMachination.class, LibNames.ASYMMETRICAL_MACHINATION);
		registerTile(TileKondenzator.class, LibNames.KONDENZATOR);
		registerTile(TileMutator.class, LibNames.MUTATOR);
		registerTile(TileBlastFurnaceController.class, LibNames.BLAST_FURNACE_CONTROLLER);
		registerTile(TileBlastFurnaceTuyere.class, LibNames.BLAST_FURNACE_TUYERE);
		registerTile(TileBlastFurnaceDrain.class, LibNames.BLAST_FURNACE_DRAIN);
		registerTile(TileBlastFurnaceInput.class, LibNames.BLAST_FURNACE_INPUT);
		registerTile(TileHotBlastAirVent.class, LibNames.BLAST_FURNACE_AIR_VENT);
		registerTile(TileHotBlastAirPump.class, LibNames.BLAST_FURNACE_AIR_PUMP);
		registerTile(TileBlastFurnacePipe.class, LibNames.BLAST_FURNACE_PIPE);
		registerTile(TileMultiblockImouto.class, "multiblock_slave");
	}

	private static <T extends TileEntity> void registerTile(Class<T> tile, String name) {
		GameRegistry.registerTileEntity(tile, new ResourceLocation(LibMod.MOD_ID, name));
	}

	@SuppressWarnings({"UnusedReturnValue"}) //Shut up
	public static Block setRegistry(Block block, String id) {
		block.setUnlocalizedName(id);
		block.setRegistryName(LibMod.MOD_ID, id);
		block.setCreativeTab(CreativeTabHandler.MISC);
		return block;
	}
}
