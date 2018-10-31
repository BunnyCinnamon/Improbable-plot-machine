/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.energy.LumenHelper;
import arekkuusu.implom.api.capability.energy.data.ILumen;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.BlockLuminicDecompressor;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.data.ModCapability;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

/**
 * Created by <Arekkuusu> on 4/9/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileLuminicDecompressor extends TileBase implements ITickable {

	@Override
	public void update() {
		if(world.getTotalWorldTime() % 40 == 0) {
			getDrained().ifPresent(drained -> {
				BlockPos.MutableBlockPos posOffset = new BlockPos.MutableBlockPos(pos);
				EnumFacing facing = getFacingLazy().getOpposite();
				float distance = 0;
				while(distance++ < BlockLuminicDecompressor.REACH) {
					IBlockState found = world.getBlockState(posOffset.move(facing));
					if(found.getBlock() == ModBlocks.LUMEN_COMPRESSOR && found.getValue(BlockDirectional.FACING) == facing) {
						getFilled(posOffset).ifPresent(filled -> {
							if(!world.isRemote) {
								LumenHelper.transfer(drained, filled, 16, false);
							} else {
								makeParticles(facing);
							}
						});
						break;
					} else if(!found.getBlock().isReplaceable(world, posOffset)) break;
				}
			});
		}
	}

	private Optional<ILumen> getDrained() {
		EnumFacing facing = getFacingLazy();
		return getTile(TileEntity.class, world, pos.offset(facing))
				.filter(tile -> tile.hasCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
				.map(tile -> tile.getCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
				.filter(lumen -> lumen.get() > 0);
	}

	private Optional<ILumen> getFilled(BlockPos pos) {
		EnumFacing facing = getFacingLazy().getOpposite();
		return getTile(TileEntity.class, world, pos.offset(facing))
				.filter(tile -> tile.hasCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
				.map(tile -> tile.getCapability(ModCapability.LUMEN_CAPABILITY, facing.getOpposite()))
				.filter(lumen -> lumen.get() < lumen.getMax());
	}

	private void makeParticles(EnumFacing facing) {
		Vector3 offset = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable().multiply(0.05D);
		Vector3 from = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5D);
		IPM.getProxy().spawnNeutronBlast(world, from, offset, 120, 1F, 0xFFE077, true);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
