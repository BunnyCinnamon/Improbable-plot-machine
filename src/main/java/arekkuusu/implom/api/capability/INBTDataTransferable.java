package arekkuusu.implom.api.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;

import java.util.Objects;

public interface INBTDataTransferable {

	default String group() {
		//noinspection unchecked
		return Objects.requireNonNull(TileEntity.getKey((Class<? extends TileEntity>) getClass())).toString();
	}

	EnumActionResult init(NBTTagCompound compound);

	void fromItemStack(ItemStack stack);

	void toItemStack(ItemStack stack);

	final class DefaultGroup {
		public static final String ALTERNATOR = "alternator";
		public static final String REDSTONE = "redstone";
		public static final String TRANSLOCATOR = "translocator";
		public static final String LUMEN = "lumen";
		public static final String WORLD_ACCESS = "world_access";
		public static final String INVENTORY = "inventory";
	}
}
