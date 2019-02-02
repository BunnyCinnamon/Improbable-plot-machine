package arekkuusu.implom.api.capability.nbt;

import arekkuusu.implom.api.capability.data.WorldAccessNBTData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IWorldAccessNBTDataCapability extends INBTDataCapability<WorldAccessNBTData>, INBTSerializable<NBTTagCompound> {

	default void set(World world, BlockPos pos, EnumFacing facing) {
		Optional.ofNullable(get()).ifPresent(data -> {
			data.setWorld(world);
			data.setPos(pos);
			data.setFacing(facing);
			onChange();
		});
	}

	default void setWorld(EnumFacing facing) {
		Optional.ofNullable(get()).ifPresent(data -> {
			data.setFacing(facing);
			onChange();
		});
	}

	default void setFacing(EnumFacing facing) {
		Optional.ofNullable(get()).ifPresent(data -> {
			data.setFacing(facing);
			onChange();
		});
	}

	default void setPos(EnumFacing facing) {
		Optional.ofNullable(get()).ifPresent(data -> {
			data.setFacing(facing);
			onChange();
		});
	}

	default void onChange() {
		//For Rent
	}

	@Nullable
	default WorldAccessNBTData get() {
		return getData(getKey()).orElse(null);
	}

	@Override
	default Class<WorldAccessNBTData> getDataClass() {
		return WorldAccessNBTData.class;
	}
}
