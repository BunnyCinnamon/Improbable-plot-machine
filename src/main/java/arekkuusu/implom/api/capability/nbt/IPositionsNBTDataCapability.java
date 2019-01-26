package arekkuusu.implom.api.capability.nbt;

import arekkuusu.implom.api.capability.data.PositionsNBTData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface IPositionsNBTDataCapability extends INBTDataCapability<PositionsNBTData>, INBTSerializable<NBTTagCompound> {

	default List<PositionsNBTData.Position> get() {
		return getData(getKey()).map(data -> data.positions).orElse(Collections.emptyList());
	}

	default int index(World world, BlockPos pos, EnumFacing facing) {
		return getData(getKey()).map(data -> {
			PositionsNBTData.Position position = new PositionsNBTData.Position(pos, facing, world);
			return data.positions.indexOf(position);
		}).orElse(-1);
	}

	default void add(World world, BlockPos pos, EnumFacing facing) {
		getData(getKey()).ifPresent(data -> {
			PositionsNBTData.Position wa = new PositionsNBTData.Position(data);
			wa.setWorld(world);
			wa.setPos(pos);
			wa.setFacing(facing);
			data.positions.add(wa);
		});
	}

	default void remove(World world, BlockPos pos, EnumFacing facing) {
		getData(getKey()).ifPresent(data -> {
			data.positions.removeIf(wa -> wa.getWorld() == world && Objects.equals(wa.getPos(), pos) && wa.getFacing() == facing);
		});
	}

	void setKey(@Nullable UUID uuid);

	@Nullable
	UUID getKey();

	@Override
	default Class<PositionsNBTData> getDataClass() {
		return PositionsNBTData.class;
	}
}
