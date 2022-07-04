package cinnamon.implom.api.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

public final class NBTHelper {

    /* Basic Helpers */
    public static <T extends Tag> void putNBT(CompoundTag compound, String tag, T base) {
        compound.put(tag, base);
    }
    /* Basic Helpers */

    /* Complex Helpers */
    public static void putArray(CompoundTag compound, String tag, String... array) {
        var list = new ListTag();
        for (String s : array) {
            list.add(StringTag.valueOf(s));
        }
        compound.put(tag, list);
    }

    public static String[] getArray(CompoundTag compound, String tag) {
        var list = compound.getList(tag, Tag.TAG_STRING);
        var array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.getString(i);
        }
        return array;
    }

    public static void putBlockPos(CompoundTag compound, String tag, @Nullable BlockPos pos) {
        if (pos == null) pos = BlockPos.ZERO;
        var nbt = new CompoundTag();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        compound.put(tag, nbt);
    }

    public static BlockPos getBlockPos(CompoundTag compound, String tag) {
        var pos = BlockPos.ZERO;
        if (compound.contains(tag, Tag.TAG_COMPOUND)) {
            CompoundTag nbt = compound.getCompound(tag);
            var x = nbt.getInt("x");
            var y = nbt.getInt("y");
            var z = nbt.getInt("z");
            pos = new BlockPos(x, y, z);
        }
        return pos;
    }

    public static void putVector(CompoundTag compound, String tag, Vec3 vec) {
        var nbt = new CompoundTag();
        nbt.putDouble("x", vec.x);
        nbt.putDouble("y", vec.y);
        nbt.putDouble("z", vec.z);
        compound.put(tag, nbt);
    }

    public static Vec3 getVector(CompoundTag compound, String tag) {
        var vec = Vec3.ZERO;
        if (compound.contains(tag, Tag.TAG_COMPOUND)) {
            var nbt = compound.getCompound(tag);
            var x = nbt.getDouble("x");
            var y = nbt.getDouble("y");
            var z = nbt.getDouble("z");
            vec = new Vec3(x, y, z);
        }
        return vec;
    }

    public static <T extends IForgeRegistry<T>> void putRegistry(CompoundTag compound, String tag, T instance) {
        putResourceLocation(compound, tag, Objects.requireNonNull(instance.getRegistryName()));
    }

    public static <T extends IForgeRegistry<V>, V> V getRegistry(CompoundTag compound, String tag, T registry) {
        ResourceLocation location = getResourceLocation(compound, tag);
        return registry.getValue(location);
    }

    public static void putResourceLocation(CompoundTag compound, String tag, ResourceLocation location) {
        compound.putString(tag, location.toString());
    }

    public static ResourceLocation getResourceLocation(CompoundTag compound, String tag) {
        return new ResourceLocation(compound.getString(tag));
    }

    public static <T extends Enum<T> & StringRepresentable> void putEnum(CompoundTag compound, String tag, T t) {
        compound.putString(tag, t.getSerializedName());
    }

    public static <T extends Enum<T> & StringRepresentable> T getEnum(Class<T> clazz, CompoundTag compound, String tag) {
        String value = compound.getString(tag);
        return Stream.of(clazz.getEnumConstants()).filter(e -> e.getSerializedName().equals(value)).findAny().orElseGet(() -> clazz.getEnumConstants()[0]);
    }

    public static CompoundTag getNBTTag(CompoundTag compound, String tag) {
        return compound.contains(tag, Tag.TAG_COMPOUND) ? compound.getCompound(tag) : new CompoundTag();
    }

    public static ListTag getNBTList(CompoundTag compound, String tag) {
        return compound.getList(tag, Tag.TAG_COMPOUND);
    }
    /* Complex Helpers */
}
