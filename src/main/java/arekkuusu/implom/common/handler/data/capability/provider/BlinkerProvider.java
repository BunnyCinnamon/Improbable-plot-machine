package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.data.PositionsNBTData;
import arekkuusu.implom.api.capability.nbt.IPositionsNBTDataCapability;
import arekkuusu.implom.api.capability.nbt.IRedstoneNBTCapability;
import arekkuusu.implom.common.block.tile.TileBlinker;
import arekkuusu.implom.common.handler.data.capability.nbt.PositionsNBTDataCapability;
import arekkuusu.implom.common.handler.data.capability.nbt.RedstoneNBTCapability;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class BlinkerProvider implements ICapabilityProvider, INBTSerializable<NBTBase> {

	public final IPositionsNBTDataCapability positionInstance = new PositionsNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			redstoneInstance.setKey(uuid);
		}

		@Nullable
		@Override
		public UUID getKey() {
			return redstoneInstance.getKey();
		}
	};
	public final IRedstoneNBTCapability redstoneInstance = new RedstoneNBTCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			boolean update = tile != null && !tile.getWorld().isRemote;
			if(update) positionInstance.remove(tile.getWorld(), tile.getPos(), tile.getFacingLazy());
			super.setKey(uuid);
			if(update) {
				positionInstance.add(tile.getWorld(), tile.getPos(), tile.getFacingLazy());
				tile.markDirty();
			}
			set(0);
		}

		@Override
		public void set(int tonight) {
			getData(getKey()).ifPresent(data -> {
				data.setMax(15); //Always!
				data.setMin(0);  //Never forget!
				int sal = biggestLoser();
				if(data.getValue() != sal) {
					data.setValue(sal);
					updateRedstone();
				}
			});
		}
	};
	private final TileBlinker tile;

	public BlinkerProvider(TileBlinker tile) {
		this.tile = tile;
	}

	private int biggestLoser() {
		int sal = 0;
		for(PositionsNBTData.Position wa : positionInstance.get()) {
			if(wa.getWorld() != null && wa.getPos() != null && wa.getFacing() != null) {
				World world = wa.getWorld();
				BlockPos pos = wa.getPos();
				int biggestLoser = world.getStrongPower(pos);
				if(biggestLoser > sal) sal = biggestLoser;
			}
		}
		return sal;
	}

	private void updateRedstone() {
		for(PositionsNBTData.Position wa : positionInstance.get()) {
			if(wa.getWorld() != null && wa.getPos() != null) {
				IBlockState state = wa.getWorld().getBlockState(wa.getPos());
				Block block = state.getBlock();
				for(EnumFacing facing : EnumFacing.values()) {
					wa.getWorld().notifyNeighborsOfStateChange(wa.getPos().offset(facing), block, false);
				}
			}
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == Capabilities.POSITIONS || capability == Capabilities.REDSTONE;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == Capabilities.POSITIONS
				? Capabilities.POSITIONS.cast(positionInstance)
				: capability == Capabilities.REDSTONE
				? Capabilities.REDSTONE.cast(redstoneInstance)
				: null;
	}

	@Override
	public NBTBase serializeNBT() {
		return Capabilities.REDSTONE.writeNBT(redstoneInstance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		Capabilities.REDSTONE.readNBT(redstoneInstance, null, nbt);
	}
}
