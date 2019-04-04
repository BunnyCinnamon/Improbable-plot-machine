/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.nbt.IPositionsNBTDataCapability;
import arekkuusu.implom.api.capability.PositionsHelper;
import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.common.block.BlockAlternator;
import arekkuusu.implom.common.handler.data.capability.nbt.PositionsNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.provider.PositionsNBTProvider;
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

	public final PositionsNBTProvider wrapper = new PositionsNBTProvider(new PositionsNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			wrapper.instance.remove(getWorld(), getPos(), null);
			super.setKey(uuid);
			wrapper.instance.add(getWorld(), getPos(), null);
			markDirty();
		}
	});

	public boolean areAllActive() {
		return wrapper.instance.get().stream().noneMatch(wa -> (
				(wa.getWorld() == null || wa.getPos() == null) || !(wa.getWorld().isBlockLoaded(wa.getPos()))
		));
	}

	public boolean isActiveLazy() {
		return getStateValue(Properties.ACTIVE, pos).orElse(false);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return wrapper.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return wrapper.hasCapability(capability, facing)
				? wrapper.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setTag(BlockAlternator.Constants.NBT_POSITIONS, wrapper.serializeNBT());
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		wrapper.deserializeNBT(compound.getCompoundTag(BlockAlternator.Constants.NBT_POSITIONS));
	}

	@Override
	public String group() {
		return DefaultGroup.ALTERNATOR;
	}

	@Override
	public void setKey(UUID uuid) {
		wrapper.instance.setKey(uuid);
	}

	@Nullable
	@Override
	public UUID getKey() {
		return wrapper.instance.getKey();
	}

	@Override
	public void fromItemStack(ItemStack stack) {
		PositionsHelper.getCapability(stack).map(IPositionsNBTDataCapability::getKey).ifPresent(wrapper.instance::setKey);
	}

	@Override
	public void toItemStack(ItemStack stack) {
		PositionsHelper.getCapability(stack).ifPresent(instance -> instance.setKey(wrapper.instance.getKey()));
	}
}
