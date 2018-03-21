/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.entanglement.neutron.data.INeutron;
import net.minecraft.nbt.NBTBase;
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

	@CapabilityInject(INeutron.class)
	public static Capability<INeutron> NEUTRON_CAPABILITY = null;

	public void init() {
		register(INeutron.class, new Capability.IStorage<INeutron>() {

			@Override
			public NBTBase writeNBT(Capability<INeutron> capability, INeutron instance, EnumFacing side) {
				return new NBTTagInt(instance.get());
			}

			@Override
			public void readNBT(Capability<INeutron> capability, INeutron instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagInt) {
					instance.set(((NBTTagInt) nbt).getInt());
				}
			}
		}, INeutron.DEFAULT);
	}

	private <T> void register(Class<T> type, Capability.IStorage<T> storage, Callable<? extends T> factory) {
		CapabilityManager.INSTANCE.register(type, storage, factory);
	}
}
