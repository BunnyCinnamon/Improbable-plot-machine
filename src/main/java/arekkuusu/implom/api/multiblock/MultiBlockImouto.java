package arekkuusu.implom.api.multiblock;

import net.minecraft.util.math.BlockPos;

public interface MultiBlockImouto {

	void wakeUpOniichan();

	void overrideOniichan(BlockPos pos);

	boolean setOniichan(BlockPos pos);

	void removeOniichan();

	boolean hasValidOniichan();

	BlockPos getOniichan();
}
