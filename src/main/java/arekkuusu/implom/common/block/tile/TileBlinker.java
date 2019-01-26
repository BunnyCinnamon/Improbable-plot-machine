/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.INBTDataTransferable;
import arekkuusu.implom.api.capability.nbt.IPositionsNBTDataCapability;
import arekkuusu.implom.api.helper.PositionsHelper;
import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.common.block.BlockBlinker;
import arekkuusu.implom.common.handler.data.capability.provider.BlinkerProvider;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TileBlinker extends TileBase implements INBTDataTransferable {

	public final BlinkerProvider wrapper = new BlinkerProvider(this);

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		boolean refresh = super.shouldRefresh(world, pos, oldState, newState);
		if(!refresh) {
			int index = wrapper.positionInstance.index(world, pos, getFacingLazy());
			if(index != -1)
				wrapper.positionInstance.get().get(index).setFacing(newState.getValue(BlockDirectional.FACING));
		}
		return refresh;
	}

	@Override
	public void onLoad() {
		if(!world.isRemote) {
			wrapper.redstoneInstance.set(0);
		}
	}

	@Override
	public void onChunkUnload() {
		if(!world.isRemote) {
			wrapper.redstoneInstance.set(0);
		}
	}

	public boolean isPoweredLazy() {
		return getStateValue(Properties.ACTIVE, pos).orElse(false);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
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
		compound.setTag(BlockBlinker.Constants.NBT_REDSTONE, wrapper.serializeNBT());
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		wrapper.deserializeNBT(compound.getCompoundTag(BlockBlinker.Constants.NBT_REDSTONE));
	}

	@Override
	public void init(NBTTagCompound compound) {
		boolean noKey = !compound.hasUniqueId("key");
		boolean override = wrapper.redstoneInstance.getKey() == null && (noKey || !compound.getUniqueId("key").equals(wrapper.redstoneInstance.getKey()));
		if(override) {
			if(noKey) compound.setUniqueId("key", UUID.randomUUID());
			UUID uuid = compound.getUniqueId("key");
			wrapper.redstoneInstance.setKey(uuid);
		} else if(noKey) {
			compound.setUniqueId("key", wrapper.redstoneInstance.getKey());
		}
	}

	@Override
	public void fromItemStack(ItemStack stack) {
		PositionsHelper.getCapability(stack).map(IPositionsNBTDataCapability::getKey).ifPresent(wrapper.redstoneInstance::setKey);
	}

	@Override
	public void toItemStack(ItemStack stack) {
		PositionsHelper.getCapability(stack).ifPresent(instance -> instance.setKey(wrapper.redstoneInstance.getKey()));
	}
}
