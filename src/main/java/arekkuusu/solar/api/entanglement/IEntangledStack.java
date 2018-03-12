/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement;

import arekkuusu.solar.api.entanglement.quantum.QuantumHandler;
import arekkuusu.solar.api.helper.NBTHelper;
import net.katsstuff.mirror.client.helper.Tooltip;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public interface IEntangledStack {

	@SideOnly(Side.CLIENT)
	default Tooltip getInfo(Tooltip builder, UUID uuid) {
		String key = uuid.toString();
		return builder.addI18n("tlp.uuid_key.name", Tooltip.DarkGrayItalic()).add(": ").newline()
				.add(" > ").add(key.substring(0, 18)).newline()
				.add(" > ").add(key.substring(18)).newline();
	}

	default void setKey(ItemStack stack, UUID uuid) {
		stack.getOrCreateSubCompound(QuantumHandler.NBT_TAG).setUniqueId("key", uuid);
	}

	default Optional<UUID> getKey(ItemStack stack) {
		Optional<NBTTagCompound> optional = NBTHelper.getNBTTag(stack, QuantumHandler.NBT_TAG);
		return optional.map(nbtTagCompound -> nbtTagCompound.getUniqueId("key"));
	}
}
