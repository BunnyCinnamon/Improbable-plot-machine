/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.helper.RayTraceHelper;
import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TileDilaton;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 04/02/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockDilaton extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_BASE = FacingAlignedBB.create(
			new Vector3(0, 0, 0),
			new Vector3(16, 12, 16),
			EnumFacing.UP
	).build();
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_PIECE = FacingAlignedBB.create(
			new Vector3(0, 12, 0),
			new Vector3(16, 16, 16),
			EnumFacing.UP
	).build();

	public BlockDilaton() {
		super(LibNames.DILATON, Material.ROCK);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(Properties.ACTIVE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(5F);
		setResistance(2000F);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this) {
			EnumFacing facing = state.getValue(BlockDirectional.FACING);
			if(pos.offset(facing).equals(fromPos)) {
				IBlockState from = world.getBlockState(fromPos);
				if(from.getBlock() == ModBlocks.DILATON_EXTENSION
						&& state.getValue(Properties.ACTIVE)
						&& from.getValue(BlockDirectional.FACING).getOpposite() == facing) {
					world.setBlockState(pos, state.withProperty(Properties.ACTIVE, false));
					world.setBlockToAir(fromPos);
				}
			} else {
				getTile(TileDilaton.class, world, pos).ifPresent(dilaton -> {
					boolean wasPowered = dilaton.isPowered();
					boolean isPowered = world.isBlockPowered(pos);
					if((isPowered || block.getDefaultState().canProvidePower()) && isPowered != wasPowered) {
						dilaton.setPowered(isPowered);
						dilaton.pushExtension();
					}
				});
			}
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		ItemStack stack = placer.getHeldItem(hand);
		boolean active = !stack.isEmpty() && stack.getOrCreateSubCompound("dilaton").getBoolean("active");
		return defaultState().withProperty(BlockDirectional.FACING, facing).withProperty(Properties.ACTIVE, active);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		EnumFacing blockFacing = state.getValue(BlockDirectional.FACING);
		return blockFacing == facing || blockFacing == facing.getOpposite() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		this.onBlockHarvested(world, pos, state, player);
		RayTraceResult result = RayTraceHelper.tracePlayerHighlight(player);
		boolean active = state.getValue(Properties.ACTIVE);
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		IBlockState newState = !active && RayTraceHelper.isHittingSideOfBlock(result, facing, world, state, pos)
				? state.withProperty(Properties.ACTIVE, true)
				: net.minecraft.init.Blocks.AIR.getDefaultState();
		return world.setBlockState(pos, newState, world.isRemote ? 11 : 3);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		IBlockState blockState = worldIn.getBlockState(pos);
		boolean active = blockState.getBlock() == ModBlocks.DILATON && blockState.getValue(Properties.ACTIVE);
		boolean unactive = !state.getValue(Properties.ACTIVE);
		if(active && unactive) {
			ModBlocks.DILATON_EXTENSION.harvestBlock(worldIn, player, pos, state, te, stack);
		} else {
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		if(target.sideHit == state.getValue(BlockDirectional.FACING)) {
			return new ItemStack(ModBlocks.DILATON_EXTENSION);
		}
		return super.getPickBlock(state, target, world, pos, player);
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateSubCompound("dilaton").setBoolean("active", state.getValue(Properties.ACTIVE));
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(state.getValue(Properties.ACTIVE)) {
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
				IPM.getProxy().spawnSpeck(world, posVec, speedVec, 60, 2F, powered ? 0x49FFFF : 0xFFFFFF, Light.GLOW, ResourceLibrary.GLOW_PARTICLE);
			}
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(BlockDirectional.FACING).ordinal();
		if(state.getValue(Properties.ACTIVE)) {
			i |= 8;
		}
		return i;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.values()[meta & 7];
		return this.getDefaultState().withProperty(BlockDirectional.FACING, enumfacing).withProperty(Properties.ACTIVE, (meta & 8) > 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockDirectional.FACING, Properties.ACTIVE);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(Properties.ACTIVE) ? BB_BASE.get(state.getValue(BlockDirectional.FACING)) : FULL_BLOCK_AABB;
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
