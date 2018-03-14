/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum.data;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

/**
 * Created by <Snack> on 14/03/2018.
 * It's distributed as part of Solar.
 */
public class QuantumIItemData implements IQuantumData<NBTTagList> {

	public List<ItemStack> stacks = Lists.newArrayList();

	@Override
	public void read(NBTTagList tag) {
		stacks.clear();
		for(NBTBase base : tag) {
			ItemStack stack = new ItemStack((NBTTagCompound) base);
			stacks.add(stack);
		}
	}

	@Override
	public NBTTagList write() {
		NBTTagList list = new NBTTagList();
		if(stacks != null) stacks.forEach(s -> list.appendTag(s.writeToNBT(new NBTTagCompound())));
		return list;
	}
}
