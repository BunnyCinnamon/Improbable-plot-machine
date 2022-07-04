package cinnamon.implom.api.multiblock;

import net.minecraft.core.BlockPos;

public interface MultiBlockImouto {

	void wakeUpOniichan();

	void overrideOniichan(BlockPos pos);

	boolean setOniichan(BlockPos pos);

	void removeOniichan();

	boolean hasValidOniichan();

	BlockPos getOniichan();
}
