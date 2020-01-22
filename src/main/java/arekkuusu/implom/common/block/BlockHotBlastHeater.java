package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.common.block.base.BlockBaseMultiblock;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHotBlastHeater extends BlockBaseMultiblock {

	public BlockHotBlastHeater() {
		super(LibNames.HOT_BLAST_HEATER, IPMMaterial.FIRE_BRICK);
	}

	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP;
	}
}
