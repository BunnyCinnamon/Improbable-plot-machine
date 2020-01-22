/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.WorldAccessHelper;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.provider.QimranutCapabilityProvider;
import net.minecraft.block.BlockDirectional;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 23/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TileQimranut extends TileBase implements INBTDataTransferableImpl {

	public final QimranutCapabilityProvider provider = new QimranutCapabilityProvider(this);

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? Optional.ofNullable(provider.worldAccessNBTDataCapability.get())
				.filter(data -> data.getPos() != null && data.getWorld() != null && data.getFacing() != null)
				.map(data -> getTile(TileEntity.class, data.getWorld(), data.getPos()).map(tile -> tile.hasCapability(capability, data.getFacing())).orElse(false))
				.orElse(false)
				: provider.hasCapability(capability, facing)
				|| super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? Optional.ofNullable(provider.worldAccessNBTDataCapability.get())
				.filter(data -> data.getPos() != null && data.getWorld() != null && data.getFacing() != null)
				.flatMap(data -> getTile(TileEntity.class, data.getWorld(), data.getPos()).map(tile -> tile.getCapability(capability, data.getFacing())))
				.orElse(null) : provider.hasCapability(capability, facing)
				? provider.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
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
		return DefaultGroup.WORLD_ACCESS;
	}

	@Override
	public void setKey(@Nullable UUID uuid) {
		provider.worldAccessNBTDataCapability.setKey(uuid);
	}

	@Nullable
	@Override
	public UUID getKey() {
		return provider.worldAccessNBTDataCapability.getKey();
	}

	@Override
	public void fromItemStack(ItemStack stack) {
		WorldAccessHelper.getCapability(stack).map(IWorldAccessNBTDataCapability::getKey).ifPresent(this::setKey);
	}

	@Override
	public void toItemStack(ItemStack stack) {
		WorldAccessHelper.getCapability(stack).ifPresent(instance -> instance.setKey(this.getKey()));
	}
}
