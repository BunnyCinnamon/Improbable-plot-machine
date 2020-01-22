package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.WorldAccessHelper;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.provider.MutatorCapabilityProvider;
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

public class TileMutator extends TileBase implements INBTDataTransferableImpl {

	public final MutatorCapabilityProvider provider = new MutatorCapabilityProvider(this);
	public boolean powered;

	public void overrideWorldAccess() {
		provider.worldAccessInstance.set(getWorld(), getPos().offset(getFacingLazy()), getFacingLazy().getOpposite());
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		boolean refresh = super.shouldRefresh(world, pos, oldState, newState);
		if(!refresh) {
			provider.worldAccessInstance.setFacing(newState.getValue(BlockDirectional.FACING));
		}
		return refresh;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return getFacingLazy().getOpposite() == facing && provider.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getFacingLazy().getOpposite() == facing && provider.hasCapability(capability, facing)
				? provider.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	/* NBT */
	public static final String NBT_PROVIDER = "provider";
	public static final String NBT_POWERED = "powered";

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setTag(NBT_PROVIDER, provider.serializeNBT());
		compound.setBoolean(NBT_POWERED, powered);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		provider.deserializeNBT(compound.getCompoundTag(NBT_PROVIDER));
		powered = compound.getBoolean(NBT_POWERED);
	}

	@Override
	public String group() {
		return DefaultGroup.WORLD_ACCESS;
	}

	@Override
	public void setKey(@Nullable UUID uuid) {
		provider.worldAccessInstance.setKey(uuid);
	}

	@Nullable
	@Override
	public UUID getKey() {
		return provider.worldAccessInstance.getKey();
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
