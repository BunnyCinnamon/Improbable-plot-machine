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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.UUID;

public class BlinkerCapabilityProvider extends CapabilityProvider {

	public final IRedstoneNBTCapability redstoneNBTCapability = new RedstoneNBTCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			getAs(TileBlinker.class).ifPresent(holder -> {
				positionsNBTDataCapability.remove(holder.getWorld(), holder.getPos(), holder.getFacingLazy());
			});
			super.setKey(uuid);
			getAs(TileBlinker.class).ifPresent(holder -> {
				positionsNBTDataCapability.add(holder.getWorld(), holder.getPos(), holder.getFacingLazy());
				holder.markDirty();
				holder.sync();
			});
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

		private void updateRedstone() {
			for(PositionsNBTData.Position wa : positionsNBTDataCapability.get()) {
				if(wa.getWorld() != null && wa.getPos() != null) {
					IBlockState state = wa.getWorld().getBlockState(wa.getPos());
					Block block = state.getBlock();
					for(EnumFacing facing : EnumFacing.values()) {
						wa.getWorld().notifyNeighborsOfStateChange(wa.getPos().offset(facing), block, false);
					}
				}
			}
		}

		private int biggestLoser() {
			int sal = 0;
			for(PositionsNBTData.Position wa : positionsNBTDataCapability.get()) {
				if(wa.getWorld() != null && wa.getPos() != null && wa.getFacing() != null) {
					World world = wa.getWorld();
					BlockPos pos = wa.getPos();
					int biggestLoser = world.getStrongPower(pos);
					if(biggestLoser > sal) sal = biggestLoser;
				}
			}
			return sal;
		}
	};
	public final IPositionsNBTDataCapability positionsNBTDataCapability = new PositionsNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			redstoneNBTCapability.setKey(uuid);
		}

		@Nullable
		@Override
		public UUID getKey() {
			return redstoneNBTCapability.getKey();
		}
	};

	public BlinkerCapabilityProvider(ICapabilitySerializable<NBTTagCompound> holder) {
		super(holder);
		capabilities.add(new CapabilityWrapper<>(Capabilities.REDSTONE, redstoneNBTCapability));
		capabilities.add(new CapabilityWrapper<>(Capabilities.POSITIONS, positionsNBTDataCapability));
	}
}
