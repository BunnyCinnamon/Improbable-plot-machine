/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.PositionsHelper;
import arekkuusu.implom.api.capability.nbt.IPositionsNBTDataCapability;
import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.common.handler.data.capability.provider.PositionsDefaultProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 23/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileAlternator extends TileBase implements INBTDataTransferableImpl {

	public final PositionsDefaultProvider provider = new PositionsDefaultProvider(this);

	public boolean areAllActive() {
		return provider.positionsNBTDataCapability.get().stream().noneMatch(wa -> (
				(wa.getWorld() == null || wa.getPos() == null) || !(wa.getWorld().isBlockLoaded(wa.getPos()))
		));
	}

	public boolean isActiveLazy() {
		return getStateValue(Properties.ACTIVE, pos).orElse(false);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing)
				? provider.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	/* NBT */
	public static final String NBT_PROVIDER = "provider";

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setTag(NBT_PROVIDER, provider.serializeNBT());
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		provider.deserializeNBT(compound.getCompoundTag(NBT_PROVIDER));
	}

	@Override
	public String group() {
		return DefaultGroup.ALTERNATOR;
	}

	@Override
	public void setKey(@Nullable UUID uuid) {
		provider.positionsNBTDataCapability.setKey(uuid);
	}

	@Nullable
	@Override
	public UUID getKey() {
		return provider.positionsNBTDataCapability.getKey();
	}

	@Override
	public void fromItemStack(ItemStack stack) {
		PositionsHelper.getCapability(stack).map(IPositionsNBTDataCapability::getKey).ifPresent(this::setKey);
	}

	@Override
	public void toItemStack(ItemStack stack) {
		PositionsHelper.getCapability(stack).ifPresent(instance -> instance.setKey(this.getKey()));
	}
}
