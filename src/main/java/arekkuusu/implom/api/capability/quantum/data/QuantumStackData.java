/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.quantum.data;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

/**
 * Created by <Arekkuusu> on 14/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
@INBTData.NBTHolder(modId = "solar", name = "quantum_stack_nbt")
public class QuantumStackData implements INBTData<NBTTagList> {

	public List<ItemStack> stacks = Lists.newArrayList();

	@Override
	public boolean canDeserialize() {
		return !stacks.isEmpty();
	}

	@Override
	public void deserialize(NBTTagList tag) {
		stacks.clear();
		for(NBTBase base : tag) {
			ItemStack stack = new ItemStack((NBTTagCompound) base);
			stacks.add(stack);
		}
	}

	@Override
	public NBTTagList serialize() {
		NBTTagList list = new NBTTagList();
		if(stacks != null) stacks.forEach(s -> list.appendTag(s.writeToNBT(new NBTTagCompound())));
		return list;
	}
}
