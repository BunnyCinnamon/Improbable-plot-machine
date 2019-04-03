/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TileBlinker;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockBlinker extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = FacingAlignedBB.create(
			new Vector3(2, 15, 2),
			new Vector3(14, 16, 14),
			EnumFacing.UP
	).build();
	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.apply(0.5D, 0.2D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.apply(0.5D, 0.8D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.apply(0.5D, 0.5D, 0.8D))
			.put(EnumFacing.SOUTH, Vector3.apply(0.5D, 0.5D, 0.2D))
			.put(EnumFacing.EAST, Vector3.apply(0.2D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.apply(0.8D, 0.5D, 0.5D))
			.build();

	public BlockBlinker() {
		super(LibNames.BLINKER, IPMMaterial.MONOLITH);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(Properties.ACTIVE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
		setLightLevel(0.2F);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!worldIn.isRemote) {
			getTile(TileBlinker.class, worldIn, pos).ifPresent(tile -> {
				tile.fromItemStack(stack);
			});
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		getTile(TileBlinker.class, worldIn, pos).ifPresent(tile -> {
			tile.wrapper.positionInstance.remove(worldIn, pos, state.getValue(BlockDirectional.FACING));
			tile.wrapper.redstoneInstance.set(0);
		});
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote) {
			world.scheduleUpdate(pos, this, tickRate(world));
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this) {
			getTile(TileBlinker.class, world, pos).ifPresent(tile -> {
				tile.wrapper.redstoneInstance.set(0);
			});
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(!world.isRemote) {
			getTile(TileBlinker.class, world, pos).ifPresent(tile -> {
				boolean active = tile.wrapper.redstoneInstance.get() > 0;
				if(active != state.getValue(Properties.ACTIVE)) {
					world.setBlockState(pos, state.withProperty(Properties.ACTIVE, active));
					for(EnumFacing facing : EnumFacing.values()) {
						world.notifyNeighborsOfStateChange(pos.offset(facing), this, false);
					}
				}
			});
			world.scheduleUpdate(pos, this, tickRate(world));
		}
	}

	@Override
	public int tickRate(World worldIn) {
		return 0;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		ItemStack stack = super.getItem(world, pos, state);
		getTile(TileBlinker.class, world, pos).ifPresent(tile -> {
			tile.toItemStack(stack);
		});
		return stack;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		boolean active = state.getValue(Properties.ACTIVE);
		Vector3 back = getOffSet(facing.getOpposite(), pos);
		facing = facing.getOpposite();
		for(int i = 0; i < 3 + rand.nextInt(6); i++) {
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 25);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 25);
			double speed = 0.01D + rand.nextDouble() * 0.015D;
			Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
					.asImmutable()
					.multiply(speed)
					.rotate(x.multiply(z));
			IPM.getProxy().spawnSpeck(world, back, speedVec, 60, 2.5F, active ? 0x49FFFF : 0xFFFFFF, Light.GLOW, ResourceLibrary.GLOW_PARTICLE);
		}
	}

	private Vector3 getOffSet(EnumFacing facing, BlockPos pos) {
		return FACING_MAP.get(facing).add(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return facing.getOpposite() != side && facing != side;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return !world.getBlockState(pos.offset(facing)).canProvidePower() || facing != side.getOpposite()
				? getTile(TileBlinker.class, world, pos)
				.map(tile -> tile.wrapper.redstoneInstance.get())
				.orElse(0) : 0;
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return !world.getBlockState(pos.offset(side.getOpposite())).canProvidePower()
				? getTile(TileBlinker.class, world, pos)
				.map(tile -> tile.wrapper.redstoneInstance.get())
				.orElse(0) : 0;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing.getOpposite()).withProperty(Properties.ACTIVE, false);
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
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.SOLID;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileBlinker();
	}

	public static class Constants {
		public static final String NBT_REDSTONE = "redstone";
	}
}
