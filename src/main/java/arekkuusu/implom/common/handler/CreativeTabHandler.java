package arekkuusu.implom.common.handler;

import arekkuusu.implom.IPM;
import arekkuusu.implom.common.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public final class CreativeTabHandler {

    public static final CreativeTab MISC = new Bamboozled();

    private static abstract class CreativeTab extends ItemGroup {
        CreativeTab(String name) {
            super(IPM.MOD_ID + "." + name);
        }
    }

    private static class Bamboozled extends CreativeTab {

        Bamboozled() {
            super("misc_tab");
            setBackgroundImageName("items.png");
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.FIRE_BRICK_BLOCK.get());
        }
    }
}
