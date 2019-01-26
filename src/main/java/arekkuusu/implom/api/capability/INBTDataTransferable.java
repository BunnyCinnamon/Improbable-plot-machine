package arekkuusu.implom.api.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.Objects;

public interface INBTDataTransferable {

	default String key() {
		//noinspection unchecked
		return Objects.requireNonNull(TileEntity.getKey((Class<? extends TileEntity>) getClass())).toString();
	}

	void init(NBTTagCompound compound);

	void fromItemStack(ItemStack stack);

	void toItemStack(ItemStack stack);
}
