/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler.data;

import arekkuusu.implom.api.capability.binary.data.IBinary;
import arekkuusu.implom.api.capability.energy.data.IComplexLumen;
import arekkuusu.implom.api.capability.energy.data.ILumen;
import arekkuusu.implom.api.capability.quantum.IQuantum;
import arekkuusu.implom.api.capability.relativity.data.IRelative;
import arekkuusu.implom.api.capability.relativity.data.IRelativeRedstone;
import arekkuusu.implom.api.capability.worldaccess.data.IWorldAccess;
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
 * It's distributed as part of Improbable plot machine.
 */
public final class ModCapability {

	@CapabilityInject(ILumen.class)
	public static Capability<ILumen> LUMEN_CAPABILITY = null;
	@CapabilityInject(IComplexLumen.class)
	public static Capability<IComplexLumen> COMPLEX_LUMEN_CAPABILITY = null;
	@CapabilityInject(IBinary.class)
	public static Capability<IBinary> BINARY_CAPABILITY = null;
	@CapabilityInject(IRelative.class)
	public static Capability<IRelative> RELATIVE_CAPABILITY = null;
	@CapabilityInject(IRelativeRedstone.class)
	public static Capability<IRelativeRedstone> RELATIVE_REDSTONE_CAPABILITY = null;
	@CapabilityInject(IWorldAccess.class)
	public static Capability<IWorldAccess> RELATIVE_WORLD_ACCESS_CAPABILITY = null;

	public static void init() {
		register(ILumen.class, new Capability.IStorage<ILumen>() {
			@Override
			public NBTBase writeNBT(Capability<ILumen> capability, ILumen instance, EnumFacing side) {
				if(instance instanceof IQuantum) {
					NBTTagCompound tag = new NBTTagCompound();
					((IQuantum) instance).getKey().ifPresent(key -> tag.setUniqueId("key", key));
					return tag;
				} else {
					return new NBTTagInt(instance.get());
				}
			}

			@Override
			public void readNBT(Capability<ILumen> capability, ILumen instance, EnumFacing side, NBTBase nbt) {
				if(instance instanceof IQuantum && nbt instanceof NBTTagCompound) {
					((IQuantum) instance).setKey(((NBTTagCompound) nbt).getUniqueId("key"));
				} else if(nbt instanceof NBTTagInt) {
					instance.set(((NBTTagInt) nbt).getInt());
				}
			}
		}, ILumen.DEFAULT);
		register(IComplexLumen.class, new Capability.IStorage<IComplexLumen>() {
			@Override
			public NBTBase writeNBT(Capability<IComplexLumen> capability, IComplexLumen instance, EnumFacing side) {
				return ModCapability.LUMEN_CAPABILITY.writeNBT(instance, side);
			}

			@Override
			public void readNBT(Capability<IComplexLumen> capability, IComplexLumen instance, EnumFacing side, NBTBase nbt) {
				ModCapability.LUMEN_CAPABILITY.readNBT(instance, side, nbt);
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
		register(IRelative.class, new Capability.IStorage<IRelative>() {
			@Override
			public NBTBase writeNBT(Capability<IRelative> capability, IRelative instance, EnumFacing side) {
				NBTTagCompound tag = new NBTTagCompound();
				instance.getKey().ifPresent(key -> tag.setUniqueId("key", key));
				return tag;
			}

			@Override
			public void readNBT(Capability<IRelative> capability, IRelative instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound) {
					instance.setKey(((NBTTagCompound) nbt).getUniqueId("key"));
				}
			}
		}, IRelative.DEFAULT);
		register(IRelativeRedstone.class, new Capability.IStorage<IRelativeRedstone>() {
			@Override
			public NBTBase writeNBT(Capability<IRelativeRedstone> capability, IRelativeRedstone instance, EnumFacing side) {
				NBTTagCompound tag = new NBTTagCompound();
				instance.getKey().ifPresent(key -> tag.setUniqueId("key", key));
				return tag;
			}

			@Override
			public void readNBT(Capability<IRelativeRedstone> capability, IRelativeRedstone instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound) {
					instance.setKey(((NBTTagCompound) nbt).getUniqueId("key"));
				}
			}
		}, IRelativeRedstone.DEFAULT);
		register(IWorldAccess.class, new Capability.IStorage<IWorldAccess>() {
			@Override
			public NBTBase writeNBT(Capability<IWorldAccess> capability, IWorldAccess instance, EnumFacing side) {
				NBTTagCompound tag = new NBTTagCompound();
				instance.getKey().ifPresent(key -> tag.setUniqueId("key", key));
				return tag;
			}

			@Override
			public void readNBT(Capability<IWorldAccess> capability, IWorldAccess instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound) {
					instance.setKey(((NBTTagCompound) nbt).getUniqueId("key"));
				}
			}
		}, IWorldAccess.DEFAULT);
	}

	private static <T> void register(Class<T> type, Capability.IStorage<T> storage, Callable<? extends T> factory) {
		CapabilityManager.INSTANCE.register(type, storage, factory);
	}
}
