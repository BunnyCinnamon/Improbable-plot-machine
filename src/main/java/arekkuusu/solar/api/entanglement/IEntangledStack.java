/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement;

import arekkuusu.solar.api.entanglement.inventory.EntangledIItemHandler;
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

	/**
	 * Adds the {@param uuid} information to the {@param builder}
	 *
	 * @param builder The {@link Tooltip} builder
	 * @param uuid    An {@link UUID}
	 * @return The tooltip with the {@param uuid} information
	 */
	@SideOnly(Side.CLIENT)
	default Tooltip getInfo(Tooltip builder, UUID uuid) {
		String key = uuid.toString();
		return builder.addI18n("tlp.uuid_key.name", Tooltip.DarkGrayItalic()).add(": ").newline()
				.add(" > ").add(key.substring(0, 18)).newline()
				.add(" > ").add(key.substring(18)).newline();
	}

	/**
	 * Gets the {@link UUID} if it exists
	 *
	 * @return An {@link Optional<UUID>} containing the key
	 */
	default Optional<UUID> getKey(ItemStack stack) {
		Optional<NBTTagCompound> optional = NBTHelper.getNBTTag(stack, EntangledIItemHandler.NBT_TAG);
		return optional.map(nbtTagCompound -> nbtTagCompound.getUniqueId("key"));
	}

	/**
	 * Sets the {@param key}
	 *
	 * @param key An {@link UUID}
	 */
	default void setKey(ItemStack stack, UUID key) {
		stack.getOrCreateSubCompound(EntangledIItemHandler.NBT_TAG).setUniqueId("key", key);
	}
}
