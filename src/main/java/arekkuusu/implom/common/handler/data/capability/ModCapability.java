/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler.data.capability;

import arekkuusu.implom.api.capability.ILumenCapability;
import arekkuusu.implom.api.capability.nbt.IInventoryNBTDataCapability;
import arekkuusu.implom.api.capability.nbt.IPositionsNBTDataCapability;
import arekkuusu.implom.api.capability.nbt.IRedstoneNBTCapability;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.nbt.InventoryNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.nbt.PositionsNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.nbt.RedstoneNBTCapability;
import arekkuusu.implom.common.handler.data.capability.nbt.WorldAccessNBTDataCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
public final class ModCapability {

	public static void init() {
		register(ILumenCapability.class, new Capability.IStorage<ILumenCapability>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<ILumenCapability> capability, ILumenCapability instance, EnumFacing side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<ILumenCapability> capability, ILumenCapability instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound)
					instance.deserializeNBT((NBTTagCompound) nbt);
				else instance.deserializeNBT(new NBTTagCompound());
			}
		}, LumenCapability::new);
		register(IWorldAccessNBTDataCapability.class, new Capability.IStorage<IWorldAccessNBTDataCapability>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IWorldAccessNBTDataCapability> capability, IWorldAccessNBTDataCapability instance, EnumFacing side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IWorldAccessNBTDataCapability> capability, IWorldAccessNBTDataCapability instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound)
					instance.deserializeNBT((NBTTagCompound) nbt);
				else instance.deserializeNBT(new NBTTagCompound());
			}
		}, WorldAccessNBTDataCapability::new);
		register(IPositionsNBTDataCapability.class, new Capability.IStorage<IPositionsNBTDataCapability>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IPositionsNBTDataCapability> capability, IPositionsNBTDataCapability instance, EnumFacing side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IPositionsNBTDataCapability> capability, IPositionsNBTDataCapability instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound)
					instance.deserializeNBT((NBTTagCompound) nbt);
				else instance.deserializeNBT(new NBTTagCompound());
			}
		}, PositionsNBTDataCapability::new);
		register(IInventoryNBTDataCapability.class, new Capability.IStorage<IInventoryNBTDataCapability>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IInventoryNBTDataCapability> capability, IInventoryNBTDataCapability instance, EnumFacing side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IInventoryNBTDataCapability> capability, IInventoryNBTDataCapability instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound)
					instance.deserializeNBT((NBTTagCompound) nbt);
				else instance.deserializeNBT(new NBTTagCompound());
			}
		}, InventoryNBTDataCapability::new);
		register(IRedstoneNBTCapability.class, new Capability.IStorage<IRedstoneNBTCapability>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IRedstoneNBTCapability> capability, IRedstoneNBTCapability instance, EnumFacing side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IRedstoneNBTCapability> capability, IRedstoneNBTCapability instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagCompound)
					instance.deserializeNBT((NBTTagCompound) nbt);
				else instance.deserializeNBT(new NBTTagCompound());
			}
		}, RedstoneNBTCapability::new);
	}

	private static <T> void register(Class<T> type, Capability.IStorage<T> storage, Callable<? extends T> factory) {
		CapabilityManager.INSTANCE.register(type, storage, factory);
	}
}
