/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.helper;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by <Arekkuusu> on 14/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
public final class NBTHelper {

	public static NBTTagCompound fixNBT(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		return tagCompound;
	}

	public static void setByte(NBTTagCompound compound, String tag, byte i) {
		compound.setByte(tag, i);
	}

	public static void setInteger(NBTTagCompound compound, String tag, int i) {
		compound.setInteger(tag, i);
	}

	public static void setFloat(NBTTagCompound compound, String tag, float i) {
		compound.setFloat(tag, i);
	}

	public static void setBoolean(NBTTagCompound compound, String tag, boolean i) {
		compound.setBoolean(tag, i);
	}

	public static void setString(NBTTagCompound compound, String tag, String i) {
		compound.setString(tag, i);
	}

	public static void setUniqueID(NBTTagCompound compound, String tag, UUID i) {
		compound.setUniqueId(tag, i);
	}

	public static void setBlockPos(NBTTagCompound compound, String tag, @Nullable BlockPos pos) {
		if(pos == null) pos = BlockPos.ORIGIN;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", pos.getX());
		nbt.setInteger("y", pos.getY());
		nbt.setInteger("z", pos.getZ());
		compound.setTag(tag, nbt);
	}

	public static byte getByte(NBTTagCompound compound, String tag) {
		return compound.getByte(tag);
	}

	public static int getInteger(NBTTagCompound compound, String tag) {
		return compound.getInteger(tag);
	}

	public static float getFloat(NBTTagCompound compound, String tag) {
		return compound.getFloat(tag);
	}

	public static boolean getBoolean(NBTTagCompound compound, String tag) {
		return compound.getBoolean(tag);
	}

	public static String getString(NBTTagCompound compound, String tag) {
		return compound.getString(tag);
	}

	@Nullable
	public static UUID getUniqueID(NBTTagCompound compound, String tag) {
		return compound.hasUniqueId(tag) ? compound.getUniqueId(tag) : null;
	}

	public static BlockPos getBlockPos(NBTTagCompound compound, String tag) {
		BlockPos pos = BlockPos.ORIGIN;
		if(hasTag(compound, tag, NBTType.COMPOUND)) {
			NBTTagCompound nbt = compound.getCompoundTag(tag);
			int x = nbt.getInteger("x");
			int y = nbt.getInteger("y");
			int z = nbt.getInteger("z");
			pos = pos.add(x, y, z);
		}
		return pos;
	}

	public static <T extends Enum<T> & IStringSerializable> void setEnum(NBTTagCompound compound, T t, String tag) {
		compound.setString(tag, t.getName());
	}

	public static <T extends Enum<T> & IStringSerializable> Optional<T> getEnum(Class<T> clazz, NBTTagCompound compound, String tag) {
		String value = compound.getString(tag);
		return Stream.of(clazz.getEnumConstants()).filter(e -> e.getName().equals(value)).findAny();
	}

	public static <T extends NBTBase> T setNBT(NBTTagCompound compound, String tag, T base) {
		compound.setTag(tag, base);
		return base;
	}

	public static Optional<NBTTagCompound> getNBTTag(NBTTagCompound compound, String tag) {
		return hasTag(compound, tag, NBTType.COMPOUND) ? Optional.of(compound.getCompoundTag(tag)) : Optional.empty();
	}

	public static Optional<NBTTagList> getNBTList(NBTTagCompound compound, String tag) {
		return hasTag(compound, tag, NBTType.COMPOUND) ? Optional.of(compound.getTagList(tag, NBTType.LIST.ordinal())) : Optional.empty();
	}

	public static <T extends Entity> Optional<T> getEntityByUUID(Class<T> clazz, UUID uuid, World world) {
		for(Entity entity : world.loadedEntityList) {
			if(clazz.isInstance(entity) && entity.getUniqueID().equals(uuid)) return Optional.of(clazz.cast(entity));
		}
		return Optional.empty();
	}

	public static boolean hasTag(NBTTagCompound compound, String tag, NBTType type) {
		return compound != null && compound.hasKey(tag, type.ordinal());
	}

	public static boolean hasTag(NBTTagCompound compound, String tag) {
		return compound != null && compound.hasKey(tag);
	}

	public static boolean hasUniqueID(NBTTagCompound compound, String tag) {
		return compound != null && compound.hasUniqueId(tag);
	}

	public static void removeTag(NBTTagCompound compound, String tag) {
		if(compound != null && compound.hasKey(tag)) {
			compound.removeTag(tag);
		}
	}

	public enum NBTType {
		END,
		BYTE,
		SHORT,
		INT,
		LONG,
		FLOAT,
		DOUBLE,
		BYTE_ARRAY,
		STRING,
		LIST,
		COMPOUND,
		INT_ARRAY,
		LONG_ARRAY
	}
}
