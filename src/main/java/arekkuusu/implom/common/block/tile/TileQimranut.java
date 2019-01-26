/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.INBTDataTransferable;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import arekkuusu.implom.api.helper.WorldAccessHelper;
import arekkuusu.implom.common.block.BlockQimranut;
import arekkuusu.implom.common.handler.data.capability.nbt.WorldAccessNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.provider.WorldAccessProvider;
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
public class TileQimranut extends TileBase implements INBTDataTransferable {

	public final WorldAccessProvider wrapper = new WorldAccessProvider(new WorldAccessNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			super.setKey(uuid);
			markDirty();
		}
	});

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? Optional.ofNullable(wrapper.instance.get())
				.filter(data -> data.getPos() != null && data.getWorld() != null && data.getFacing() != null)
				.map(data -> getTile(TileEntity.class, data.getWorld(), data.getPos()).map(tile -> tile.hasCapability(capability, data.getFacing())).orElse(false))
				.orElse(false)
				: wrapper.hasCapability(capability, facing)
				|| super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? Optional.ofNullable(wrapper.instance.get())
				.filter(data -> data.getPos() != null && data.getWorld() != null && data.getFacing() != null)
				.map(data -> getTile(TileEntity.class, data.getWorld(), data.getPos()).map(tile -> tile.getCapability(capability, data.getFacing())).orElse(null))
				.orElse(null) : wrapper.hasCapability(capability, facing)
				? wrapper.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setTag(BlockQimranut.Constants.NBT_WORLD_ACCESS, wrapper.serializeNBT());
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		wrapper.deserializeNBT(compound.getTag(BlockQimranut.Constants.NBT_WORLD_ACCESS));
	}

	@Override
	public void init(NBTTagCompound compound) {
		boolean noKey = !compound.hasUniqueId("key");
		boolean override = wrapper.instance.getKey() == null && (noKey || !compound.getUniqueId("key").equals(wrapper.instance.getKey()));
		if(override) {
			if(noKey) compound.setUniqueId("key", UUID.randomUUID());
			UUID uuid = compound.getUniqueId("key");
			wrapper.instance.setKey(uuid);
		} else if(noKey) {
			compound.setUniqueId("key", wrapper.instance.getKey());
		}
	}

	@Override
	public void fromItemStack(ItemStack stack) {
		WorldAccessHelper.getCapability(stack).map(IWorldAccessNBTDataCapability::getKey).ifPresent(wrapper.instance::setKey);
	}

	@Override
	public void toItemStack(ItemStack stack) {
		WorldAccessHelper.getCapability(stack).ifPresent(instance -> instance.setKey(wrapper.instance.getKey()));
	}
}
