/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.handler.CreativeTabHandler;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar.
 */
@ObjectHolder(LibMod.MOD_ID)
public final class ModItems {

	private static final Item PLACE_HOLDER = new Item();
	//--------------------------------Items--------------------------------//
	public static final Item CRYSTAL_QUARTZ = PLACE_HOLDER;
	public static final Item ANGSTROM = PLACE_HOLDER;
	public static final Item ASH = PLACE_HOLDER;
	public static final Item SINGULARITY = PLACE_HOLDER;
	public static final Item PLASMON = PLACE_HOLDER;
	public static final Item QELAION = PLACE_HOLDER;

	public static void register(IForgeRegistry<Item> registry) {
		registry.register(itemBlock(ModBlocks.PRIMAL_STONE));
		registry.register(new ItemQuantumMirror());
		registry.register(itemBlock(ModBlocks.GRAVITY_HOPPER));
		registry.register(itemBlock(ModBlocks.SCHRODINGER_GLYPH));
		registry.register(new ItemBlinker());
		registry.register(itemBlock(ModBlocks.PHENOMENA));
		registry.register(new ItemCrystalQuartz());
		registry.register(itemBlock(ModBlocks.Q_SQUARED));
		registry.register(itemBlock(ModBlocks.THEOREMA));
		registry.register(itemBlock(ModBlocks.HYPER_CONDUCTOR));
		registry.register(itemBlock(ModBlocks.ELECTRON));
		registry.register(itemBlock(ModBlocks.ASHEN));
		registry.register(itemBlock(ModBlocks.MONOLITHIC));
		registry.register(new ItemMonolithicGlyph());
		registry.register(new ItemAngstrom());
		registry.register(new ItemQimranut());
		registry.register(itemBlock(ModBlocks.CELESTIAL_RESONATOR));
		registry.register(itemBlock(ModBlocks.LARGE_POT));
		registry.register(new ItemBase(LibNames.ASH));
		registry.register(new ItemBase(LibNames.SINGULARITY));
		registry.register(new ItemBase(LibNames.PLASMON));
		registry.register(new ItemVacuumConveyor());
		registry.register(new ItemMechanicalTranslocator());
		registry.register(new ItemAlternator());
		registry.register(new ItemDilaton());
		registry.register(itemBlock(ModBlocks.DILATON_EXTENSION));
		registry.register(new ItemQelaion());
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
