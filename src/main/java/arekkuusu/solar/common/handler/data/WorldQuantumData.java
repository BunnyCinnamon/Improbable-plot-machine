/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.entanglement.quantum.QuantumHandler;
import arekkuusu.solar.api.entanglement.quantum.data.IQuantumData;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.network.PacketHelper;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/08/2017.
 * It's distributed as part of Solar.
 */
public class WorldQuantumData extends WorldSavedData implements IQuantumData {

	private static final String NAME = LibMod.MOD_ID + ":" + QuantumHandler.NBT_TAG;

	public static WorldQuantumData get(World world) {
		MapStorage storage = world.getMapStorage();
		//noinspection ConstantConditions
		WorldQuantumData data = (WorldQuantumData) storage.getOrLoadData(WorldQuantumData.class, NAME);
		if(data == null) {
			data = new WorldQuantumData(NAME);
			storage.setData(NAME, data);
		}
		return data;
	}

	private final Map<UUID, List<ItemStack>> ENTANGLEMENT_MAP = Maps.newHashMap();

	public WorldQuantumData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = (NBTTagList) compound.getTag(QuantumHandler.NBT_TAG);
		list.forEach(stackList -> {
			NBTTagList stacks = (NBTTagList) ((NBTTagCompound) stackList).getTag("list");
			UUID key = ((NBTTagCompound) stackList).getUniqueId("key");
			if(key != null) {
				getEntanglement(key).clear();
				stacks.forEach(tag -> getEntanglement(key).add(new ItemStack((NBTTagCompound) tag)));
			}
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		getEntanglements().forEach((uuid, itemStacks) -> {
			NBTTagList stackList = new NBTTagList();
			itemStacks.forEach(stack -> stackList.appendTag(stack.writeToNBT(new NBTTagCompound())));
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setUniqueId("key", uuid);
			nbt.setTag("list", stackList);
			list.appendTag(nbt);
		});
		compound.setTag(QuantumHandler.NBT_TAG, list);
		return compound;
	}

	@Override
	public void setEntanglementStack(UUID uuid, ItemStack stack, int slot) {
		List<ItemStack> list = getEntanglement(uuid);
		if(!hasSlot(uuid, slot)) {
			if(!stack.isEmpty()) {
				list.add(stack);
			}
		} else if(!stack.isEmpty()) {
			list.set(slot, stack);
		} else {
			list.remove(slot);
		}
		if(FMLCommonHandler.instance().getSide() == Side.SERVER) {
			PacketHelper.sendQuantumChange(uuid, stack, slot);
		}
		markDirty();
	}

	@Override
	public Map<UUID, List<ItemStack>> getEntanglements() {
		return ENTANGLEMENT_MAP;
	}
}
