/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.entanglement.inventory;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import net.minecraft.item.ItemStack;

import java.util.UUID;

/*
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
public interface IEntangledIItemStack extends IEntangledStack {
	@Override
	default void setKey(ItemStack stack, UUID key) {
		if(!getKey(stack).isPresent()) {
			stack.getOrCreateSubCompound(EntangledIItemHandler.NBT_TAG).setUniqueId("key", key);
		}
	}
}
