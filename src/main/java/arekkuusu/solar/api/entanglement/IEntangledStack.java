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
import arekkuusu.solar.api.helper.NBTHelper.NBTType;
import arekkuusu.solar.client.util.helper.TooltipBuilder;
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
	default TooltipBuilder getInfo(TooltipBuilder builder, UUID uuid) {
		builder.addI18("uuid_key", TooltipBuilder.DARK_GRAY_ITALIC).add(": ").end();
		String key = uuid.toString();
		builder.add(" > ").add(key.substring(0, 18)).end();
		builder.add(" > ").add(key.substring(18)).end();
		return builder;
	}

	default void setKey(ItemStack stack, UUID uuid) {
		NBTHelper.<NBTTagCompound>getOrCreate(stack, QuantumHandler.NBT_TAG, NBTType.COMPOUND).setUniqueId("key", uuid);
	}

	default Optional<UUID> getKey(ItemStack stack) {
		Optional<NBTTagCompound> optional = NBTHelper.getNBTTag(stack, QuantumHandler.NBT_TAG);
		return Optional.ofNullable(optional.map(nbtTagCompound -> nbtTagCompound.getUniqueId("key")).orElse(null));
	}
}
