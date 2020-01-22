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
import arekkuusu.implom.common.handler.data.capability.provider.BlinkerCapabilityProvider;
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
public class TileBlinker extends TileBase implements INBTDataTransferableImpl {

	public final BlinkerCapabilityProvider provider = new BlinkerCapabilityProvider(this);

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		boolean refresh = super.shouldRefresh(world, pos, oldState, newState);
		if(!refresh) {
			int index = provider.positionsNBTDataCapability.index(world, pos, getFacingLazy());
			if(index != -1) {
				provider.positionsNBTDataCapability.get().get(index)
						.setFacing(newState.getValue(BlockDirectional.FACING));
			}
		}
		return refresh;
	}

	@Override
	public void onLoad() {
		if(!world.isRemote) {
			provider.redstoneNBTCapability.set(0);
		}
	}

	@Override
	public void onChunkUnload() {
		if(!world.isRemote) {
			provider.redstoneNBTCapability.set(0);
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
	private static final String NBT_PROVIDER = "provider";

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
		return DefaultGroup.REDSTONE;
	}

	@Override
	public void setKey(@Nullable UUID uuid) {
		provider.redstoneNBTCapability.setKey(uuid);
	}

	@Nullable
	@Override
	public UUID getKey() {
		return provider.redstoneNBTCapability.getKey();
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
