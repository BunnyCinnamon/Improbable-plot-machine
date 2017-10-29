/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.lib.LibMod;
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
public class ModItems {

	private static final Item PLACE_HOLDER = new Item();
	//--------------------------------Items--------------------------------//
	public static final Item QUINGENTILLIARD = PLACE_HOLDER;
	public static final Item QUANTUM_QUARTZ = PLACE_HOLDER;

	public static void register(IForgeRegistry<Item> registry) {
		registry.register(itemBlock(ModBlocks.PRIMAL_STONE));
		registry.register(new ItemPrimalGlyph());
		registry.register(itemBlock(ModBlocks.singularity));
		registry.register(itemBlock(ModBlocks.PRISM_FLOWER));
		registry.register(new ItemQuantumMirror());
		registry.register(new ItemGravityHopper());
		registry.register(new ItemQuingentilliard());
		registry.register(new ItemSchrodingerGlyph());
		registry.register(itemBlock(ModBlocks.CRYSTAL_VOID));
		registry.register(new ItemBlinker());
		registry.register(itemBlock(ModBlocks.PHENOMENA));
		registry.register(new ItemQuantumQuartz());
		registry.register(itemBlock(ModBlocks.Q_SQUARED));
		registry.register(itemBlock(ModBlocks.THEOREMA));
		registry.register(itemBlock(ModBlocks.GRAVITY_INHIBITOR));
		registry.register(itemBlock(ModBlocks.HYPER_CONDUCTOR));
		registry.register(itemBlock(ModBlocks.ELECTRON_NODE));
	}

	@SuppressWarnings("ConstantConditions")
	private static Item itemBlock(Block block) {
		return new ItemBlock(block).setRegistryName(block.getRegistryName());
	}
}
