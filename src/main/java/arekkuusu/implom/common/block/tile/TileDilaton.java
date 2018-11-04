/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.state.State;
import arekkuusu.implom.client.util.helper.ProfilerHelper;
import arekkuusu.implom.common.block.ModBlocks;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Set;

/*
 * Created by <Arekkuusu> on 04/02/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileDilaton extends TileBase {

	private boolean powered;

	public void pushExtension() {
		if(!world.isRemote && powered) {
			ProfilerHelper.flagSection("[Dilaton] Redstone signal received");
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(getPos());
			EnumFacing facing = getFacingLazy();
			boolean active = isActiveLazy();
			int range = getRedstonePower() + 1;
			int pointer = 0;
			if(active) {
				ProfilerHelper.flagSection("[Dilaton] Locate nearest extension");
				boolean found = false;
				for(; pointer < range; pointer++) {
					if(!isPosValid(pos.move(facing))) return;
					IBlockState state = world.getBlockState(pos);
					if(state.getBlock() == ModBlocks.DILATON_EXTENSION) {
						world.setBlockToAir(pos);
						found = true;
						break;
					} else if(state.getBlock() == ModBlocks.DILATON && !state.getValue(State.ACTIVE) && state.getValue(BlockDirectional.FACING) == facing.getOpposite()) {
						world.setBlockState(pos, state.withProperty(State.ACTIVE, true));
						found = true;
						break;
					}
				}
				if(!found) return;
				pointer = 0;
			}
			ProfilerHelper.begin("[Dilaton] Gathering pushed blocks");
			if(active) facing = facing.getOpposite();
			List<Triple<IBlockState, NBTTagCompound, BlockPos>> pushed = Lists.newArrayList();
			List<BlockPos> removed = Lists.newArrayList();
			loop : for(; pointer < range; pointer++) {
				if(pos.move(facing).equals(getPos()) || !isPosValid(pos)) break;
				IBlockState next = world.getBlockState(pos);
				if(next.getBlock() == Blocks.AIR) continue;
				float hardness = next.getBlockHardness(world, pos);
				if(hardness > 2000F || hardness < 0F) break;
				EnumPushReaction reaction;
				if(next.getMaterial() == Material.WATER) reaction = EnumPushReaction.IGNORE;
				else reaction = next.getMobilityFlag();
				switch(reaction) {
					case PUSH_ONLY:
					case NORMAL:
						if(pushed.add(getStateTile(next, pos.toImmutable())) && pushed.size() > 15) break loop;
						else ++range;
						continue loop;
					case DESTROY:
						removed.add(pos.toImmutable());
						continue loop;
					case BLOCK:
						break loop;
					case IGNORE:
				}
			}
			ProfilerHelper.interrupt("[Dilaton] Relocating pushed blocks");
			Set<BlockPos> deleted = Sets.newHashSet();
			if(pushed.size() <= 15) {
				pos.move(facing.getOpposite());
			}
			for(int i = 0, size = pushed.size(); i < size; i++) {
				if(isPosReplaceable(pos)) {
					for(int index = pushed.size() - 1; index >= 0; index--) {
						Triple<IBlockState, NBTTagCompound, BlockPos> triplet = pushed.get(index);
						if(setStateTile(triplet.getLeft(), triplet.getMiddle(), pos.toImmutable()))
							removed.add(pos.toImmutable());
						BlockPos oldPos = triplet.getRight();
						if(deleted.add(oldPos))
							deleted.removeIf(a -> a.equals(pos));
						pos.move(facing.getOpposite());
					}
					deleted.forEach(delete -> {
						if(world.getTileEntity(delete) != null) world.removeTileEntity(delete);
						world.setBlockToAir(delete);
					});
					break;
				} else if(!pushed.isEmpty()) pushed.remove(pushed.size() - 1);
				pos.move(facing.getOpposite());
			}
			ProfilerHelper.flagSection("[Dilaton] Block drops");
			removed.forEach(p -> {
				IBlockState state = world.getBlockState(p);
				float chance = state.getMaterial() == Material.SNOW ? -1.0F : 1.0F;
				state.getBlock().dropBlockAsItemWithChance(world, p, state, chance, 0);
				world.setBlockToAir(p);
			});
			ProfilerHelper.end();
			ProfilerHelper.flagSection("[Dilaton] Place extension");
			if(pos.equals(getPos().offset(facing.getOpposite()))) {
				if(isActiveLazy()) world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(State.ACTIVE, false));
			} else if(!pos.equals(getPos())) {
				if(!isActiveLazy()) world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(State.ACTIVE, true));
				IBlockState extension = ModBlocks.DILATON_EXTENSION.getDefaultState().withProperty(BlockDirectional.FACING, facing);
				world.setBlockState(pos, extension);
			}
		}
	}

	private Triple<IBlockState, NBTTagCompound, BlockPos> getStateTile(IBlockState state, BlockPos pos) {
		NBTTagCompound tag = new NBTTagCompound();
		getTile(TileEntity.class, world, pos).ifPresent(tile -> {
			tile.writeToNBT(tag);
			tag.removeTag("x");
			tag.removeTag("y");
			tag.removeTag("z");
		});
		return Triple.of(state, tag, pos);
	}

	private boolean setStateTile(IBlockState state, NBTTagCompound tag, BlockPos pos) {
		boolean remove = isPosReplaceable(pos) && !state.getBlock().canPlaceBlockAt(world, pos); //Before setting
		if(state.getBlock() == ModBlocks.DILATON_EXTENSION) {
			state = state.withProperty(BlockDirectional.FACING, getFacingLazy());
		}
		world.setBlockState(pos, state);
		getTile(TileEntity.class, world, pos).ifPresent(tile -> {
			tag.setInteger("x", pos.getX());
			tag.setInteger("y", pos.getY());
			tag.setInteger("z", pos.getZ());
			tile.readFromNBT(tag);
		});
		return remove;
	}

	private boolean isPosValid(BlockPos pos) {
		return world.isValid(pos) && world.isBlockLoaded(pos);
	}

	private boolean isPosReplaceable(BlockPos pos) {
		return world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos);
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
