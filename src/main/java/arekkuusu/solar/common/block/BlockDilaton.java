/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.block.tile.TileDilaton;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 04/02/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockDilaton extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_BASE = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0, 0, 0, 1, 0.75, 1))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0, 0.25, 0, 1, 1, 1))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0, 0, 0, 1, 1, 0.75))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0, 0, 0.25, 1, 1, 1))
			.put(EnumFacing.EAST, new AxisAlignedBB(0, 0, 0, 0.75, 1, 1))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.25, 0, 0, 1, 1, 1))
			.build();
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_PIECE = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0, 0.75, 0, 1, 1, 1))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0, 0, 0, 1, 0.25, 1))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0, 0, 0, 1, 1, 0.25))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0, 0, 0.75, 1, 1, 1))
			.put(EnumFacing.WEST, new AxisAlignedBB(0, 0, 0, 0.25, 1, 1))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.75, 0, 0, 1, 1, 1))
			.build();
	private static final ImmutableMap<EnumFacing, Vector3> VEC_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.apply(0.5D, 0.75D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.apply(0.5D, 0.25D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.apply(0.5D, 0.5D, 0.25D))
			.put(EnumFacing.SOUTH, Vector3.apply(0.5D, 0.5D, 0.75D))
			.put(EnumFacing.EAST, Vector3.apply(0.75D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.apply(0.25D, 0.5D, 0.5D))
			.build();

	public BlockDilaton() {
		super(LibNames.DILATON, Material.ROCK);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(State.ACTIVE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(5F);
		setResistance(2000F);
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
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(state.getValue(State.ACTIVE)) {
			boolean powered = world.isBlockPowered(pos);
			EnumFacing facing = state.getValue(BlockDirectional.FACING);
			Vector3 posVec = Vector3.apply(pos.getX(), pos.getY(), pos.getZ()).add(0.5D);
			for(int i = 0; i < 1 + rand.nextInt(3); i++) {
				Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 6);
				Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 6);
				double speed = 0.025D + 0.005D * rand.nextDouble();
				Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
						.asImmutable()
						.multiply(speed)
						.rotate(x.multiply(z));
				Solar.getProxy().spawnMute(world, posVec, speedVec, 60, 2F, powered ? 0x49FFFF : 0xFF0303, Light.GLOW);
			}
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(State.ACTIVE) ? BB_BASE.get(state.getValue(BlockDirectional.FACING)) : FULL_BLOCK_AABB;
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
			setHarvestLevel(Tool.PICK, ToolLevel.STONE);
			setHardness(5F);
			setResistance(2000F);
		}

		@Override
		public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
			return BB_PIECE.get(state.getValue(BlockDirectional.FACING));
		}
	}
}
