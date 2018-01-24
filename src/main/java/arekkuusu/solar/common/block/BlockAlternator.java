/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.common.block.tile.TileAlternator;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Created by <Snack> on 23/01/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockAlternator extends BlockBase {

	public BlockAlternator() {
		super(LibNames.ALTERNATOR, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(State.ACTIVE, true));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		getTile(TileAlternator.class, world, pos).ifPresent(alternator -> {
			world.setBlockState(pos, state.withProperty(State.ACTIVE, alternator.areAllActive()));
		});
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileAlternator.class, world, pos).ifPresent(alternator -> {
				IEntangledStack entangled = (IEntangledStack) stack.getItem();
				if(!entangled.getKey(stack).isPresent()) {
					entangled.setKey(stack, UUID.randomUUID());
				}
				entangled.getKey(stack).ifPresent(alternator::setKey);
			});
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		getTile(TileAlternator.class, world, pos).ifPresent(alternator -> {
			ItemStack stack = getItem(world, pos, state);
			spawnAsEntity(world, pos, stack);
		});
		super.breakBlock(world, pos, state);
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		Optional<TileAlternator> optional = getTile(TileAlternator.class, world, pos);
		if(optional.isPresent()) {
			TileAlternator alternator = optional.get();
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			alternator.getKey().ifPresent(uuid -> {
				((IEntangledStack) stack.getItem()).setKey(stack, uuid);
			});
			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return state.getValue(State.ACTIVE);
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return canProvidePower(state);
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return state.getValue(State.ACTIVE) ? 15 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(State.ACTIVE, meta == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(State.ACTIVE) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, State.ACTIVE);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileAlternator();
	}
}
