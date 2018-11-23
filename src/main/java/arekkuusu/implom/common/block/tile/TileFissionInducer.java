/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.Map;

/*
 * Created by <Arekkuusu> on 4/5/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileFissionInducer extends TileBase implements ITickable {

	private final Map<EnumFacing.Axis, BlockPos[]> POSITIONS = ImmutableMap.<EnumFacing.Axis, BlockPos[]>builder()
			.put(EnumFacing.Axis.Z, new BlockPos[]{
					new BlockPos(0, 1, 0),
					new BlockPos(1, 1, 0),
					new BlockPos(-1, 1, 0),
					new BlockPos(0, -1, 0),
					new BlockPos(1, -1, 0),
					new BlockPos(-1, -1, 0),
					new BlockPos(1, 0, 0),
					new BlockPos(-1, 0, 0)
			}).put(EnumFacing.Axis.X, new BlockPos[]{
					new BlockPos(0, 1, 0),
					new BlockPos(0, 1, 1),
					new BlockPos(0, 1, -1),
					new BlockPos(0, -1, 0),
					new BlockPos(0, -1, 1),
					new BlockPos(0, -1, -1),
					new BlockPos(0, 0, 1),
					new BlockPos(0, 0, -1)
			}).put(EnumFacing.Axis.Y, new BlockPos[]{
					new BlockPos(1, 0, 1),
					new BlockPos(-1, 0, -1),
					new BlockPos(1, 0, 0),
					new BlockPos(0, 0, 1),
					new BlockPos(-1, 0, 0),
					new BlockPos(0, 0, -1),
					new BlockPos(-1, 0, 1),
					new BlockPos(1, 0, -1)
			}).build();
	private int[] ticks = new int[8];
	private int tick;

	public TileFissionInducer() {
		Arrays.fill(ticks, -1);
	}

	@Override
	public void update() {

	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.DOWN);
	}
}
