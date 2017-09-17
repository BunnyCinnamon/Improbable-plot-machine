/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.helper;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * This class was created by Arekkuusu on 02/03/2017.
 * It's distributed as part of Witchworks under
 * the MIT license.
 */
@SuppressWarnings("unused")
public final class NBTHelper {

	private NBTHelper() {
	}

	public static NBTTagCompound fixNBT(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		return tagCompound;
	}

	public static void setByte(ItemStack stack, String tag, byte i) {
		fixNBT(stack).setByte(tag, i);
	}

	public static void setInteger(ItemStack stack, String tag, int i) {
		fixNBT(stack).setInteger(tag, i);
	}

	public static void setFloat(ItemStack stack, String tag, float i) {
		fixNBT(stack).setFloat(tag, i);
	}

	public static void setBoolean(ItemStack stack, String tag, boolean i) {
		fixNBT(stack).setBoolean(tag, i);
	}

	public static void setString(ItemStack stack, String tag, String i) {
		fixNBT(stack).setString(tag, i);
	}

	public static void setUniqueID(ItemStack stack, String tag, UUID i) {
		fixNBT(stack).setUniqueId(tag, i);
	}

	public static byte getByte(ItemStack stack, String tag) {
		return fixNBT(stack).getByte(tag);
	}

	public static int getInteger(ItemStack stack, String tag) {
		return fixNBT(stack).getInteger(tag);
	}

	public static float getFloat(ItemStack stack, String tag) {
		return fixNBT(stack).getFloat(tag);
	}

	public static boolean getBoolean(ItemStack stack, String tag) {
		return fixNBT(stack).getBoolean(tag);
	}

	public static String getString(ItemStack stack, String tag) {
		return fixNBT(stack).getString(tag);
	}

	@Nullable
	public static UUID getUniqueID(ItemStack stack, String tag) {
		return fixNBT(stack).getUniqueId(tag);
	}

	public static <T extends NBTBase> T setNBT(ItemStack stack, String tag, T base) {
		fixNBT(stack).setTag(tag, base);
		return base;
	}

	@SuppressWarnings({"ConstantConditions", "unchecked"})
	public static <T extends NBTBase> Optional<T> getNBT(ItemStack stack, String tag) {
		return Optional.ofNullable((T) fixNBT(stack).getTag(tag));
	}

	public static <T extends NBTBase> T getOrCreate(ItemStack stack, String tag, Supplier<T> supplier) {
		return NBTHelper.<T>getNBT(stack, tag).orElseGet(() -> setNBT(stack, tag, supplier.get()));
	}

	public static <T extends Entity> Optional<T> getEntityByUUID(Class<T> clazz, UUID uuid, World world) {
		for (Entity entity : world.loadedEntityList) {
			if (clazz.isInstance(entity) && entity.getUniqueID().equals(uuid)) return Optional.of(clazz.cast(entity));
		}

		return Optional.empty();
	}

	public static boolean hasTag(ItemStack stack, String tag, int type) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		return tagCompound != null && tagCompound.hasKey(tag, type);
	}

	public static boolean hasTag(ItemStack stack, String tag) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		return tagCompound != null && tagCompound.hasKey(tag);
	}

	public static void removeTag(ItemStack stack, String tag) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound != null && tagCompound.hasKey(tag)) {
			tagCompound.removeTag(tag);
		}
	}
}
