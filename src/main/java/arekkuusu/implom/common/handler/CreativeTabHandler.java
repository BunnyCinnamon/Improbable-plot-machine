/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler;

import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
public final class CreativeTabHandler {

	public static final CreativeTab MISC = new Bamboozled();

	private static abstract class CreativeTab extends CreativeTabs {

		NonNullList<ItemStack> list;

		CreativeTab(String name) {
			super(LibMod.MOD_ID + "." + name);
		}

		@Override
		@SideOnly(Side.CLIENT)
		@Nonnull
		public ItemStack getTabIconItem() {
			return getIconItemStack();
		}

		@SideOnly(Side.CLIENT)
		void addItem(Item item) {
			item.getSubItems(this, list);
		}

		@SideOnly(Side.CLIENT)
		void addBlock(Block block) {
			block.getSubBlocks(this, list);
		}
	}

	private static class Bamboozled extends CreativeTab {

		Bamboozled() {
			super("misc_tab");
			setBackgroundImageName("items.png");
		}

		@Override
		@Nonnull
		public ItemStack getIconItemStack() {
			return new ItemStack(ModBlocks.SCHRODINGER_GLYPH);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
			this.list = list;
			addBlock(ModBlocks.SAPROLITE);
			addBlock(ModBlocks.MONOLITHIC);
			addBlock(ModBlocks.MONOLITHIC_GLYPH);
			addBlock(ModBlocks.SCHRODINGER_GLYPH);
			addBlock(ModBlocks.ASHEN);
			addBlock(ModBlocks.LARGE_POT);
			addBlock(ModBlocks.PHENOMENA);
			addBlock(ModBlocks.QUANTA);
			addBlock(ModBlocks.ANGSTROM);
			addBlock(ModBlocks.IMBUED_QUARTZ);
			addBlock(ModBlocks.ALTERNATOR);
			addBlock(ModBlocks.DILATON);
			addBlock(ModBlocks.QELAION);
			addBlock(ModBlocks.VACUUM_CONVEYOR);
			addBlock(ModBlocks.MECHANICAL_TRANSLOCATOR);
			addBlock(ModBlocks.QIMRANUT);
			addBlock(ModBlocks.GRAVITY_HOPPER);
			addBlock(ModBlocks.BLINKER);
			addBlock(ModBlocks.QUANTUM_MIRROR);
			addBlock(ModBlocks.HYPER_CONDUCTOR);
			addBlock(ModBlocks.ELECTRON);
			addBlock(ModBlocks.NEUTRON_BATTERY);
			addBlock(ModBlocks.LUMINIC_DECOMPRESSOR);
			addBlock(ModBlocks.LUMEN_COMPRESSOR);
			addBlock(ModBlocks.FISSION_INDUCER);
			addBlock(ModBlocks.QUARTZ_CONSUMER);
			addBlock(ModBlocks.PHOLARIZER);
			addBlock(ModBlocks.SYMMETRIC_NEGATOR);
			addBlock(ModBlocks.SYMMETRIC_EXTENSION);
			addBlock(ModBlocks.KONDENZATOR);
			addItem(ModItems.INTRINSIC_CELL);
			addItem(ModItems.MAGNETIC_BOLT);
			addItem(ModItems.MAGNETIC_GEAR);
			addItem(ModItems.MAGNETIC_SPRING);
			addItem(ModItems.CLOCKWORK);
			addItem(ModItems.BOUND_PHOTON);
			addItem(ModItems.CRYSTAL_PRISM);
			addItem(ModItems.CRYSTAL_SHARD);
			addItem(ModItems.QUARTZ);
			addItem(ModItems.SINGULARITY);
			addItem(ModItems.PLASMON);
			addItem(ModItems.ASH);
			addItem(ModItems.THEOREMA);
		}
	}
}
