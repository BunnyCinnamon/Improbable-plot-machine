package cinnamon.implom.common.handler;

import cinnamon.implom.IPM;
import cinnamon.implom.common.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class CreativeTabHandler {

    public static final CreativeTab MISC = new Bamboozled();

    private static abstract class CreativeTab extends CreativeModeTab {
        CreativeTab(String name) {
            super(IPM.MOD_ID + "." + name);
        }
    }

    private static class Bamboozled extends CreativeTab {

        Bamboozled() {
            super("misc_tab");
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.FIRE_BRICK_BLOCK.get());
        }
    }
}
