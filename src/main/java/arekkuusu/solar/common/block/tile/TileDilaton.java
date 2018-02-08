/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.common.block.ModBlocks;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

/**
 * Created by <Snack> on 04/02/2018.
 * It's distributed as part of Solar.
 */
public class TileDilaton extends TileBase {

	private boolean powered;

	public void pushExtension(boolean powered) {
		if(!world.isRemote && !(!isActiveLazy() && !powered)) {
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(getPos());
			EnumFacing facing = getFacingLazy();
			int range = powered ? getRedstonePower() + 1 : 16;
			int pointer = 0;
			if(isActiveLazy()) {
				for(; pointer < range; pointer++) {
					if(!isPosSafe(pos.move(facing)) || pointer >= range) return;
					IBlockState state = world.getBlockState(pos);
					if(state.getBlock() == ModBlocks.DILATON_EXTENSION) {
						world.setBlockToAir(pos);
						break;
					}
				}
				if(!powered) pointer = 0;
			}
			if(!powered) facing = facing.getOpposite();
			List<Pair<IBlockState, BlockPos>> pushed = Lists.newArrayList();
			List<BlockPos> removed = Lists.newArrayList();
			Set<BlockPos> deleted = Sets.newHashSet();
			loop : for(; pointer < range; pointer++) {
				if(!isPosSafe(pos.move(facing))) return;
				IBlockState next = world.getBlockState(pos);
				if(pos.equals(getPos()) || next.getBlock() == Blocks.OBSIDIAN) break;
				if(next.getBlock() == Blocks.AIR) continue;
				EnumPushReaction reaction = next.getMobilityFlag();
				switch(reaction) {
					case PUSH_ONLY:
					case NORMAL:
						if(pushed.add(Pair.of(next, pos.toImmutable())) && pushed.size() > 15) break loop;
						else ++range;
						continue;
					case DESTROY:
						removed.add(pos.toImmutable());
						continue;
					case BLOCK:
						break loop;
					case IGNORE:
				}
			}
			pos.move(facing.getOpposite());
			for(int i = 0, size = pushed.size(); i < size; i++) {
				if(isPosAvailable(pos)) {
					for(int index = pushed.size() - 1; index >= 0; index--) {
						Pair<IBlockState, BlockPos> p = pushed.get(index);
						world.setBlockState(pos, p.getKey());
						if(deleted.add(p.getValue()))
							deleted.removeIf(a -> a.equals(pos));
						pos.move(facing.getOpposite());
					}
					removed.forEach(p -> {
						IBlockState state = world.getBlockState(p);
						float chance = state.getBlock() instanceof BlockSnow ? -1.0F : 1.0F;
						state.getBlock().dropBlockAsItemWithChance(world, p, state, chance, 0);
						world.setBlockToAir(p);
					});
					deleted.forEach(world::setBlockToAir);
					break;
				} else if(!pushed.isEmpty()) pushed.remove(pushed.size() - 1);
				pos.move(facing.getOpposite());
			}
			if(pos.equals(getPos().offset(facing.getOpposite()))) {
				if(isActiveLazy()) world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(State.ACTIVE, false));
			} else if(!pos.equals(getPos())) {
				IBlockState extension = ModBlocks.DILATON_EXTENSION.getDefaultState().withProperty(BlockDirectional.FACING, facing);
				world.setBlockState(pos, extension);
				if(!isActiveLazy()) world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(State.ACTIVE, true));
			}
		}
	}

	private boolean isPosAvailable(BlockPos pos) {
		return world.isValid(pos) && world.isBlockLoaded(pos) && world.isAirBlock(pos)
				|| world.getBlockState(pos).getBlock().isReplaceable(world, pos);
	}

	private boolean isPosSafe(BlockPos pos) {
		return world.isValid(pos) && world.isBlockLoaded(pos);
	}

	public int getRedstonePower() {
		int power = 0;
		for(EnumFacing facing : EnumFacing.values()) {
			int detected = world.getRedstonePower(pos.offset(facing), facing);
			if(detected > power) power = detected;
		}
		return power;
	}

	public boolean isActiveLazy() {
		return getStateValue(State.ACTIVE, pos).orElse(false);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		powered = compound.getBoolean("powered");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setBoolean("powered", powered);
	}
}
