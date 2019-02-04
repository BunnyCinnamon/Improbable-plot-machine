package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import arekkuusu.implom.api.helper.WorldAccessHelper;
import arekkuusu.implom.common.block.BlockMutator;
import arekkuusu.implom.common.handler.data.capability.nbt.WorldAccessNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.provider.WorldAccessProvider;
import arekkuusu.implom.common.network.PacketHelper;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.UUID;

public class TileMutator extends TileBase implements INBTDataTransferableImpl {

	public final WorldAccessProvider wrapper = new WorldAccessProvider(new WorldAccessNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			super.setKey(uuid);
			markDirty();
			sync();
		}

		@Override
		public void onChange() {
			if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				PacketHelper.sendMutatorPacket(this);
			}
		}
	});
	public boolean powered;

	public void overrideWorldAccess() {
		wrapper.instance.set(getWorld(), getPos().offset(getFacingLazy()), getFacingLazy().getOpposite());
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		boolean refresh = super.shouldRefresh(world, pos, oldState, newState);
		if(!refresh) {
			wrapper.instance.setFacing(newState.getValue(BlockDirectional.FACING));
		}
		return refresh;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return getFacingLazy().getOpposite() == facing && wrapper.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getFacingLazy().getOpposite() == facing && wrapper.hasCapability(capability, facing)
				? wrapper.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setTag(BlockMutator.Constants.NBT_WORLD_ACCESS, wrapper.serializeNBT());
		compound.setBoolean("powered", powered);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		wrapper.deserializeNBT(compound.getTag(BlockMutator.Constants.NBT_WORLD_ACCESS));
		powered = compound.getBoolean("powered");
	}

	@Override
	public String group() {
		return DefaultGroup.WORLD_ACCESS;
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
		WorldAccessHelper.getCapability(stack).map(IWorldAccessNBTDataCapability::getKey).ifPresent(wrapper.instance::setKey);
	}

	@Override
	public void toItemStack(ItemStack stack) {
		WorldAccessHelper.getCapability(stack).ifPresent(instance -> instance.setKey(wrapper.instance.getKey()));
	}
}
