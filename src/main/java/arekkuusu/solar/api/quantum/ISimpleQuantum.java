/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.api.quantum;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public interface ISimpleQuantum {

	ItemStack getQuantumItem(int slot);

	void setQuantumItem(ItemStack stack, int slot);

	@Nullable
	UUID getKey();

	void setKey(@Nullable UUID key);
}
