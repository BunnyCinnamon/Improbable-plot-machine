/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.capability.binary.data.IBinary;
import arekkuusu.solar.api.capability.energy.data.IComplexLumen;
import arekkuusu.solar.api.capability.energy.data.ILumen;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public final class ModCapability {

	@CapabilityInject(ILumen.class)
	public static Capability<ILumen> LUMEN_CAPABILITY = null;
	@CapabilityInject(IComplexLumen.class)
	public static Capability<IComplexLumen> COMPLEX_LUMEN_CAPABILITY = null;
	@CapabilityInject(IBinary.class)
	public static Capability<IBinary> BINARY_CAPABILITY = null;

	public static void init() {
		register(ILumen.class, new Capability.IStorage<ILumen>() {
			@Override
			public NBTBase writeNBT(Capability<ILumen> capability, ILumen instance, EnumFacing side) {
				return new NBTTagInt(instance.get());
			}

			@Override
			public void readNBT(Capability<ILumen> capability, ILumen instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagInt) {
					instance.set(((NBTTagInt) nbt).getInt());
				}
			}
		}, ILumen.DEFAULT);
		register(IComplexLumen.class, new Capability.IStorage<IComplexLumen>() {
			@Override
			public NBTBase writeNBT(Capability<IComplexLumen> capability, IComplexLumen instance, EnumFacing side) {
				NBTTagCompound tag = new NBTTagCompound();
				instance.getKey().ifPresent(key -> tag.setUniqueId("key", key));
				return tag;
			}

			@Override
			public void readNBT(Capability<IComplexLumen> capability, IComplexLumen instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound) {
					instance.setKey(((NBTTagCompound) nbt).getUniqueId("key"));
				}
			}
		}, IComplexLumen.DEFAULT);
		register(IBinary.class, new Capability.IStorage<IBinary>() {
			@Override
			public NBTBase writeNBT(Capability<IBinary> capability, IBinary instance, EnumFacing side) {
				NBTTagCompound tag = new NBTTagCompound();
				instance.getKey().ifPresent(key -> tag.setUniqueId("key", key));
				return tag;
			}

			@Override
			public void readNBT(Capability<IBinary> capability, IBinary instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound) {
					instance.setKey(((NBTTagCompound) nbt).getUniqueId("key"));
				}
			}
		}, IBinary.DEFAULT);
	}

	private static <T> void register(Class<T> type, Capability.IStorage<T> storage, Callable<? extends T> factory) {
		CapabilityManager.INSTANCE.register(type, storage, factory);
	}
}
