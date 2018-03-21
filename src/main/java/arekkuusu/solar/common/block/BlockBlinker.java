/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.entanglement.relativity.RelativityHandler;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedBlinker;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileBlinker;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.mirror.data.Quat;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockBlinker extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.125, 0.9375, 0.125, 0.875, 1, 0.875))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.0625, 0.875))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.875, 0.0625))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.125, 0.125, 0.9375, 0.875, 0.875, 1))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.9375, 0.125, 0.875, 1, 0.875, 0.125))
			.put(EnumFacing.WEST, new AxisAlignedBB(0, 0.125, 0.125, 0.0625, 0.875, 0.875))
			.build();
	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.apply(0.5D, 0.2D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.apply(0.5D, 0.8D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.apply(0.5D, 0.5D, 0.8D))
			.put(EnumFacing.SOUTH, Vector3.apply(0.5D, 0.5D, 0.2D))
			.put(EnumFacing.EAST, Vector3.apply(0.2D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.apply(0.8D, 0.5D, 0.5D))
			.build();

	public BlockBlinker()  {
		super(LibNames.BLINKER, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(State.ACTIVE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
		setLightLevel(0.2F);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote) {
			world.scheduleUpdate(pos, this, tickRate(world));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(!world.isRemote) {
			getTile(TileBlinker.class, world, pos).ifPresent(blinker -> {
				boolean active = RelativityHandler.isPowered(blinker);
				if(active != state.getValue(State.ACTIVE)) {
					world.setBlockState(pos, state.withProperty(State.ACTIVE, active));
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
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileBlinker.class, world, pos).ifPresent(blinker -> {
				IEntangledStack entangled = (IEntangledStack) stack.getItem();
				if(!entangled.getKey(stack).isPresent()) {
					entangled.setKey(stack, UUID.randomUUID());
				}
				entangled.getKey(stack).ifPresent(blinker::setKey);
			});
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		Optional<TileBlinker> optional = getTile(TileBlinker.class, world, pos);
		if(optional.isPresent()) {
			TileBlinker blinker = optional.get();
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			blinker.getKey().ifPresent(uuid -> {
				((IEntangledStack) stack.getItem()).setKey(stack, uuid);
			});
			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		boolean active = state.getValue(State.ACTIVE);
		Vector3 back = getOffSet(facing.getOpposite(), pos);
		facing = facing.getOpposite();
		for(int i = 0; i < 3 + rand.nextInt(6); i++) {
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 5);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 5);
			double speed = 0.01D + rand.nextDouble() * 0.015D;
			Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
					.asImmutable()
					.multiply(speed)
					.rotate(x.multiply(z));
			FXUtil.spawnLight(world, back, speedVec, 60, 2.5F, active ? 0x49FFFF : 0xFFFFFF, Light.GLOW);
		}
	}

	private Vector3 getOffSet(EnumFacing facing, BlockPos pos) {
		return FACING_MAP.get(facing).add(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this && !pos.offset(state.getValue(BlockDirectional.FACING)).equals(fromPos)) {
			getTile(TileBlinker.class, world, pos).ifPresent(blinker -> {
				boolean wasPowered = RelativityHandler.isPowered(blinker);
				boolean isPowered = world.isBlockPowered(pos);
				if((isPowered || block.getDefaultState().canProvidePower()) && isPowered != wasPowered) {
					RelativityHandler.setPower(blinker, blinker.getRedstonePower(), true);
				}
			});
		}
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
		return state.getValue(BlockDirectional.FACING).getOpposite() == side
				? getTile(TileBlinker.class, world, pos).map(RelativityHandler::getPower).orElse(0) : 0;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing.getOpposite()).withProperty(State.ACTIVE, false);
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
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.SOLID;
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

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, BakedBlinker::new);
		ModelHandler.registerModel(this, 0, "");
	}
}
