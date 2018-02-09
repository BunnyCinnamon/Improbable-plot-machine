/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.util.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.block.tile.TileDilaton;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by <Snack> on 04/02/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockDilaton extends BlockBaseFacing {

	public BlockDilaton() {
		super(LibNames.DILATON, Material.ROCK);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(State.ACTIVE, false));
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this) {
			EnumFacing facing = state.getValue(BlockDirectional.FACING);
			boolean front = pos.offset(facing).equals(fromPos);
			if(front) {
				IBlockState from = world.getBlockState(fromPos);
				if(from.getBlock() == ModBlocks.DILATON_EXTENSION
						&& state.getValue(State.ACTIVE)
						&& from.getValue(BlockDirectional.FACING).getOpposite() == facing) {
					world.setBlockState(pos, state.withProperty(State.ACTIVE, false));
					world.setBlockToAir(fromPos);
				}
			} else {
				getTile(TileDilaton.class, world, pos).ifPresent(dilaton -> {
					boolean wasPowered = dilaton.isPowered();
					boolean isPowered = world.isBlockPowered(pos);
					if((isPowered || block.getDefaultState().canProvidePower()) && isPowered != wasPowered) {
						dilaton.setPowered(isPowered);
						dilaton.pushExtension(isPowered);
					}
				});
			}
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		ItemStack stack = placer.getHeldItem(hand);
		boolean active = !stack.isEmpty() && stack.getOrCreateSubCompound("dilaton").getBoolean("active");
		return defaultState().withProperty(BlockDirectional.FACING, facing).withProperty(State.ACTIVE, active);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateSubCompound("dilaton").setBoolean("active", state.getValue(State.ACTIVE));
		return stack;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(state.getValue(State.ACTIVE)) {
			boolean powered = world.isBlockPowered(pos);
			EnumFacing facing = state.getValue(BlockDirectional.FACING);
			Vector3 vec = Vector3.create(facing).multiply(0.025D + 0.005D * rand.nextFloat());
			ParticleUtil.spawnLightParticle(world, Vector3.create(pos).add(0.5D), vec, powered ? 0x49FFFF : 0xFF0303, 60, 2F);
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(BlockDirectional.FACING).ordinal();
		if(state.getValue(State.ACTIVE)) {
			i |= 8;
		}
		return i;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.values()[meta & 7];
		return this.getDefaultState().withProperty(BlockDirectional.FACING, enumfacing).withProperty(State.ACTIVE, (meta & 8) > 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockDirectional.FACING, State.ACTIVE);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileDilaton();
	}

	public static class BlockDilatonExtension extends BlockBaseFacing {

		public BlockDilatonExtension() {
			super(LibNames.DILATON_EXTENSION, Material.ROCK);
			setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
			setBlockUnbreakable();
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void registerModel() {
			//Yoink!
		}
	}
}
