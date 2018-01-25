/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.helper.RayTraceHelper;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedPerspective;
import arekkuusu.solar.client.util.baker.baked.BakedRender;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileMechanicalTranslocator;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Snack> on 16/01/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockMechanicalTranslocator extends BlockBase {


	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_BODY_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.25, 0.59375, 0.25, 0.75, 0.90625, 0.75))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.25, 0.09375, 0.25, 0.75, 0.40625, 0.75))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.25, 0.25, 0.40625, 0.75, 0.75, 0.09375))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.25, 0.25, 0.59375, 0.75, 0.75, 0.90625))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.90625, 0.25, 0.25, 0.59375, 0.75, 0.75))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.09375, 0.25, 0.25, 0.40625, 0.75, 0.75))
			.build();
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_CRYSTAL_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.4375, 0.4375, 0.4375, 0.5625, 0.5625, 0.5625))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.4375, 0.4375, 0.4375, 0.5625, 0.5625, 0.5625))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.4375, 0.4375, 0.4375, 0.5625, 0.5625, 0.5625))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.4375, 0.4375, 0.4375, 0.5625, 0.5625, 0.5625))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.4375, 0.4375, 0.4375, 0.5625, 0.5625, 0.5625))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.4375, 0.4375, 0.4375, 0.5625, 0.5625, 0.5625))
			.build();
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.25, 0.28125, 0.25, 0.75, 0.90625, 0.75))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.25, 0.09375, 0.25, 0.75, 0.71875, 0.75))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.25, 0.25, 0.71875, 0.75, 0.75, 0.09375))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.25, 0.25, 0.28125, 0.75, 0.75, 0.90625))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.90625, 0.25, 0.25, 0.28125, 0.75, 0.75))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.09375, 0.25, 0.25, 0.71875, 0.75, 0.75))
			.build();

	public BlockMechanicalTranslocator() {
		super(LibNames.MECHANICAL_TRANSLOCATOR, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(State.ACTIVE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileMechanicalTranslocator.class, world, pos).ifPresent(tile -> {
				IEntangledStack entangled = (IEntangledStack) stack.getItem();
				if(!entangled.getKey(stack).isPresent()) {
					entangled.setKey(stack, UUID.randomUUID());
				}
				entangled.getKey(stack).ifPresent(tile::setKey);
			});
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		getTile(TileMechanicalTranslocator.class, world, pos).ifPresent(tile -> {
			ItemStack stack = getItem(world, pos, state);
			spawnAsEntity(world, pos, stack);
		});
		super.breakBlock(world, pos, state);
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		Optional<TileMechanicalTranslocator> optional = getTile(TileMechanicalTranslocator.class, world, pos);
		if(optional.isPresent()) {
			TileMechanicalTranslocator tile = optional.get();
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			tile.getKey().ifPresent(uuid -> {
				((IEntangledStack) stack.getItem()).setKey(stack, uuid);
			});
			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this && !pos.offset(state.getValue(BlockDirectional.FACING)).equals(fromPos)) {
			getTile(TileMechanicalTranslocator.class, world, pos)
					.filter(TileMechanicalTranslocator::isTransferable)
					.ifPresent(tile -> {
						boolean wasPowered = tile.isPowered();
						boolean isPowered = world.isBlockPowered(pos);
						if((isPowered || block.getDefaultState().canProvidePower()) && isPowered != wasPowered) {
							tile.setPowered(isPowered);
							if(isPowered) {
								tile.activate();
							}
						}
					});
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean success = state.getValue(BlockDirectional.FACING).getOpposite() == facing && player.getHeldItem(hand).isEmpty();
		if(!world.isRemote && success) {
			getTile(TileMechanicalTranslocator.class, world, pos).ifPresent(tile -> tile.setTransferable(!tile.isTransferable()));
		}
		return success;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing.getOpposite()).withProperty(State.ACTIVE, true);
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
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(BlockDirectional.FACING, rot.rotate(state.getValue(BlockDirectional.FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withRotation(mirror.toRotation(state.getValue(BlockDirectional.FACING)));
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return state.getValue(BlockDirectional.FACING) == facing ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
		getCollisionBoxList(state).forEach(box -> addCollisionBoxToList(pos, entityBox, collidingBoxes, box));
	}

	@Nullable
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
		return RayTraceHelper.rayTraceAllAABB(getCollisionBoxList(state), pos, start, end);
	}

	private List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return Lists.newArrayList(BB_BODY_MAP.get(facing), BB_CRYSTAL_MAP.get(facing));
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_BODY_MAP.get(facing).offset(pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMechanicalTranslocator();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, (format, g) -> new BakedRender()
				.setTransforms(BakedPerspective.BLOCK_TRANSFORMS)
				.setParticle(ResourceLibrary.MECHANICAL_TRANSLOCATOR)
		);
		ModelHandler.registerModel(this, 0, "");
	}
}
