package arekkuusu.implom.api.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public final class NBTHelper {

    /* ItemStack Fixer */
    public static CompoundNBT fixNBT(ItemStack stack) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound == null) {
            tagCompound = new CompoundNBT();
            stack.setTag(tagCompound);
        }
        return tagCompound;
    }

    /* Basic Helpers */
    public static void putByte(CompoundNBT compound, String tag, byte i) {
        compound.putByte(tag, i);
    }

    public static void putInt(CompoundNBT compound, String tag, int i) {
        compound.putInt(tag, i);
    }

    public static void putFloat(CompoundNBT compound, String tag, float i) {
        compound.putFloat(tag, i);
    }

    public static void putDouble(CompoundNBT compound, String tag, double i) {
        compound.putDouble(tag, i);
    }

    public static void putBoolean(CompoundNBT compound, String tag, boolean i) {
        compound.putBoolean(tag, i);
    }

    public static void putString(CompoundNBT compound, String tag, String i) {
        compound.putString(tag, i);
    }

    public static void putUUID(CompoundNBT compound, String tag, UUID i) {
        compound.putUniqueId(tag, i);
    }

    public static byte getByte(CompoundNBT compound, String tag) {
        return compound.getByte(tag);
    }

    public static int getInteger(CompoundNBT compound, String tag) {
        return compound.getInt(tag);
    }

    public static float getFloat(CompoundNBT compound, String tag) {
        return compound.getFloat(tag);
    }

    public static double getDouble(CompoundNBT compound, String tag) {
        return compound.getDouble(tag);
    }

    public static boolean getBoolean(CompoundNBT compound, String tag) {
        return compound.getBoolean(tag);
    }

    public static String getString(CompoundNBT compound, String tag) {
        return compound.getString(tag);
    }

    @Nullable
    public static UUID getUUID(CompoundNBT compound, String tag) {
        return compound.hasUniqueId(tag) ? compound.getUniqueId(tag) : null;
    }

    public static <T extends INBT> T setNBT(CompoundNBT compound, String tag, T base) {
        compound.put(tag, base);
        return base;
    }

    public static boolean hasTag(CompoundNBT compound, String tag, int type) {
        return compound != null && compound.contains(tag, type);
    }

    public static boolean hasTag(CompoundNBT compound, String tag) {
        return compound != null && compound.contains(tag);
    }

    /* Complex Helpers */
    public static void setArray(CompoundNBT compound, String tag, String... array) {
        ListNBT list = new ListNBT();
        for (String s : array) {
            list.add(StringNBT.valueOf(s));
        }
        compound.put(tag, list);
    }

    public static String[] getArray(CompoundNBT compound, String tag) {
        ListNBT list = compound.getList(tag, Constants.NBT.TAG_STRING);
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.getString(i);
        }
        return array;
    }

    public static void setBlockPos(CompoundNBT compound, String tag, @Nullable BlockPos pos) {
        if (pos == null) pos = BlockPos.ZERO;
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        compound.put(tag, nbt);
    }

    public static BlockPos getBlockPos(CompoundNBT compound, String tag) {
        BlockPos pos = BlockPos.ZERO;
        if (hasTag(compound, tag, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT nbt = compound.getCompound(tag);
            int x = nbt.getInt("x");
            int y = nbt.getInt("y");
            int z = nbt.getInt("z");
            pos = pos.add(x, y, z);
        }
        return pos;
    }

    public static void setVector(CompoundNBT compound, String tag, Vector3d vec) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putDouble("x", vec.x);
        nbt.putDouble("y", vec.y);
        nbt.putDouble("z", vec.z);
        compound.put(tag, nbt);
    }

    public static Vector3d getVector(CompoundNBT compound, String tag) {
        Vector3d vec = Vector3d.ZERO;
        if (hasTag(compound, tag, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT nbt = compound.getCompound(tag);
            double x = nbt.getDouble("x");
            double y = nbt.getDouble("y");
            double z = nbt.getDouble("z");
            vec = vec.add(x, y, z);
        }
        return vec;
    }

    public static <T extends IForgeRegistryEntry<T>> void setRegistry(CompoundNBT compound, String tag, IForgeRegistryEntry<T> instance) {
        setResourceLocation(compound, tag, Objects.requireNonNull(instance.getRegistryName()));
    }

    public static <T extends IForgeRegistryEntry<T>> T getRegistry(CompoundNBT compound, String tag, Class<T> registry) {
        ResourceLocation location = getResourceLocation(compound, tag);
        return GameRegistry.findRegistry(registry).getValue(location);
    }

    public static void setResourceLocation(CompoundNBT compound, String tag, ResourceLocation location) {
        compound.putString(tag, location.toString());
    }

    public static ResourceLocation getResourceLocation(CompoundNBT compound, String tag) {
        return new ResourceLocation(compound.getString(tag));
    }

    public static <T extends Enum<T> & IStringSerializable> void setEnum(CompoundNBT compound, T t, String tag) {
        compound.putString(tag, t.getString());
    }

    public static <T extends Enum<T> & IStringSerializable> Optional<T> getEnum(Class<T> clazz, CompoundNBT compound, String tag) {
        String value = compound.getString(tag);
        return Stream.of(clazz.getEnumConstants()).filter(e -> e.getString().equals(value)).findAny();
    }

    public static CompoundNBT getNBTTag(CompoundNBT compound, String tag) {
        return hasTag(compound, tag, Constants.NBT.TAG_COMPOUND) ? compound.getCompound(tag) : new CompoundNBT();
    }

    public static Optional<ListNBT> getNBTList(CompoundNBT compound, String tag) {
        return hasTag(compound, tag, Constants.NBT.TAG_LIST) ? Optional.of(compound.getList(tag, Constants.NBT.TAG_COMPOUND)) : Optional.empty();
    }

    public static boolean hasUniqueID(CompoundNBT compound, String tag) {
        return compound != null && compound.hasUniqueId(tag);
    }

    public static void removeTag(CompoundNBT compound, String tag) {
        if (compound != null && compound.contains(tag)) {
            compound.remove(tag);
        }
    }
}
