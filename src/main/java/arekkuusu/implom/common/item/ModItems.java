/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.CreativeTabHandler;
import arekkuusu.implom.common.lib.LibMod;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

/*
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@ObjectHolder(LibMod.MOD_ID)
public final class ModItems {

	private static final Item PLACE_HOLDER = new Item();
	//--------------------------------Items--------------------------------//
	public static final Item CRYSTAL_PRISM = PLACE_HOLDER;
	public static final Item ANGSTROM = PLACE_HOLDER;
	public static final Item SINGULARITY = PLACE_HOLDER;
	public static final Item PLASMON = PLACE_HOLDER;
	public static final Item NEUTRON_BATTERY = PLACE_HOLDER;
	public static final Item THEOREMA = PLACE_HOLDER;
	public static final Item INTRINSIC_CELL = PLACE_HOLDER;
	public static final Item CRYSTAL_SHARD = PLACE_HOLDER;
	public static final Item QUARTZ = PLACE_HOLDER;
	public static final Item MAGNETIC_BOLT = PLACE_HOLDER;
	public static final Item MAGNETIC_GEAR = PLACE_HOLDER;
	public static final Item MAGNETIC_SPRING = PLACE_HOLDER;
	public static final Item CLOCKWORK = PLACE_HOLDER;
	public static final Item BOUND_PHOTON = PLACE_HOLDER;
	public static final Item QUANTA = PLACE_HOLDER;
	public static final Item QUANTUM_MIRROR = PLACE_HOLDER;
	public static final Item HYPER_CONDUCTOR = PLACE_HOLDER;
	public static final Item VACUUM_CONVEYOR = PLACE_HOLDER;
	public static final Item MECHANICAL_TRANSLOCATOR = PLACE_HOLDER;
	public static final Item QIMRANUT = PLACE_HOLDER;
	public static final Item PHOLARIZER = PLACE_HOLDER;
	public static final Item FISSION_INDUCER = PLACE_HOLDER;
	public static final Item ELECTRON = PLACE_HOLDER;
	public static final Item SYMMETRICAL_MACHINATION = PLACE_HOLDER;
	public static final Item ASYMMETRICAL_MACHINATION = PLACE_HOLDER;
	public static final Item KONDENZATOR = PLACE_HOLDER;
	public static final Item MAINSPRING_MECHANISM = PLACE_HOLDER;
	public static final Item FRAME_CORE = PLACE_HOLDER;
	public static final Item MUTATOR = PLACE_HOLDER;
	public static final Item IMBUED_QUARTZ = PLACE_HOLDER;
	public static final Item SOLENOID = PLACE_HOLDER;
	public static final Item RESISTOR = PLACE_HOLDER;
	public static final Item CAPACITOR = PLACE_HOLDER;
	public static final Item GOLD_PLATE = PLACE_HOLDER;
	public static final Item MAGNETIC_PLATE = PLACE_HOLDER;
	public static final Item INDUCTOR = PLACE_HOLDER;
	public static final Item MAGNETIC_ACTUATOR = PLACE_HOLDER;
	public static final Item ELECTROMAGNET = PLACE_HOLDER;
	public static final Item INTRINSIC_CAPACITOR = PLACE_HOLDER;
	public static final Item FIRE_BRICK = PLACE_HOLDER;
	public static final Item FIRE_BRICK_PLATE = PLACE_HOLDER;
	public static final Item FIRE_CLAY = PLACE_HOLDER;
	public static final Item FIRE_CLAY_BRICK = PLACE_HOLDER;
	public static final Item FIRE_CLAY_PLATE = PLACE_HOLDER;

	public static void register(IForgeRegistry<Item> registry) {
		registry.register(new ItemQuantumMirror());
		registry.register(itemBlock(ModBlocks.GRAVITY_HOPPER));
		registry.register(itemBlock(ModBlocks.MONOLITHIC_EYE));
		registry.register(new ItemBlinker());
		registry.register(itemBlock(ModBlocks.PHENOMENA));
		registry.register(new ItemBase(LibNames.CRYSTAL_PRISM));
		registry.register(itemBlock(ModBlocks.QUANTA));
		registry.register(itemBlock(ModBlocks.HYPER_CONDUCTOR));
		registry.register(itemBlock(ModBlocks.ELECTRON));
		registry.register(itemBlock(ModBlocks.MONOLITHIC));
		registry.register(new ItemMonolithicGlyph());
		registry.register(new ItemAngstrom());
		registry.register(new ItemQimranut());
		registry.register(itemBlock(ModBlocks.LARGE_POT));
		registry.register(new ItemBase(LibNames.SINGULARITY));
		registry.register(new ItemBase(LibNames.PLASMON));
		registry.register(new ItemVacuumConveyor());
		registry.register(new ItemMechanicalTranslocator());
		registry.register(new ItemAlternator());
		registry.register(new ItemDilaton());
		registry.register(itemBlock(ModBlocks.DILATON_EXTENSION));
		registry.register(new ItemQelaion());
		registry.register(new ItemNeutronBattery());
		registry.register(itemBlock(ModBlocks.PHOLARIZER));
		registry.register(itemBlock(ModBlocks.FISSION_INDUCER));
		registry.register(itemBlock(ModBlocks.COMPRESSOR));
		registry.register(itemBlock(ModBlocks.MONOLITHIC_CONSUMER));
		registry.register(itemBlock(ModBlocks.SYMMETRICAL_MACHINATION));
		registry.register(itemBlock(ModBlocks.ASYMMETRICAL_MACHINATION));
		registry.register(itemBlock(ModBlocks.KONDENZATOR));
		registry.register(new ItemTheorema());
		registry.register(itemBlock(ModBlocks.IMBUED_QUARTZ));
		registry.register(new ItemBase(LibNames.INTRINSIC_CELL));
		registry.register(new ItemCrystalShard());
		registry.register(new ItemQuartz());
		registry.register(new ItemBase(LibNames.MAGNETIC_BOLT));
		registry.register(new ItemBase(LibNames.MAGNETIC_GEAR));
		registry.register(new ItemBase(LibNames.MAGNETIC_SPRING));
		registry.register(new ItemClockwork());
		registry.register(new ItemBoundPhoton());
		registry.register(new ItemBase(LibNames.MAINSPRING_MECHANISM));
		registry.register(new ItemBase(LibNames.FRAME_CORE));
		registry.register(new ItemMutator());
		registry.register(new ItemBase(LibNames.SOLENOID));
		registry.register(new ItemBase(LibNames.RESISTOR));
		registry.register(new ItemBase(LibNames.CAPACITOR));
		registry.register(new ItemBase(LibNames.GOLD_PLATE));
		registry.register(new ItemBase(LibNames.MAGNETIC_PLATE));
		registry.register(new ItemBase(LibNames.INDUCTOR));
		registry.register(new ItemBase(LibNames.MAGNETIC_ACTUATOR));
		registry.register(new ItemBase(LibNames.ELECTROMAGNET));
		registry.register(new ItemBase(LibNames.INTRINSIC_CAPACITOR));
		registry.register(new ItemBase(LibNames.FIRE_BRICK));
		registry.register(new ItemBase(LibNames.FIRE_BRICK_PLATE));
		registry.register(new ItemBase(LibNames.FIRE_CLAY));
		registry.register(new ItemBase(LibNames.FIRE_CLAY_BRICK));
		registry.register(new ItemBase(LibNames.FIRE_CLAY_PLATE));
		registry.register(itemBlock(ModBlocks.FIRE_CLAY_BLOCK));
		registry.register(itemBlock(ModBlocks.FIRE_BRICK_BLOCK));
		registry.register(itemBlock(ModBlocks.FIRE_BRICKS));
		registry.register(itemBlock(ModBlocks.AIR_VENT));
		registry.register(itemBlock(ModBlocks.FIRE_BRICKS_GLASS));
		registry.register(itemBlock(ModBlocks.FIRE_BRICKS_WINDOW));
	}

	@SuppressWarnings("ConstantConditions")
	private static Item itemBlock(Block block) {
		return new ItemBlock(block).setRegistryName(block.getRegistryName());
	}

	@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"}) //Shut up
	public static Item setRegistry(Item item, String id) {
		item.setUnlocalizedName(id);
		item.setRegistryName(LibMod.MOD_ID, id);
		item.setCreativeTab(CreativeTabHandler.MISC);
		return item;
	}
}
