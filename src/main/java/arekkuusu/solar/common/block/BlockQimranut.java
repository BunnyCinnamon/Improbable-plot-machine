/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.entanglement.relativity.RelativityHandler;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedPerspective;
import arekkuusu.solar.client.util.baker.baked.BakedRender;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileQimranut;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 23/12/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockQimranut extends BlockBase {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.1875, 0.75, 0.1875, 0.8125, 0.8125, 0.8125))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.1875, 0.1875, 0.1875, 0.8125, 0.25, 0.8125))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.1875, 0.1875, 0.25, 0.8125, 0.8125, 0.1875))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.1875, 0.1875, 0.75, 0.8125, 0.8125, 0.8125))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.8125, 0.1875, 0.1875, 0.75, 0.8125, 0.8125))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.25, 0.1875, 0.1875, 0.1875, 0.8125, 0.8125))
			.build();

	public BlockQimranut() {
		super(LibNames.QIMRANUT, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
		setLightLevel(0.2F);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileQimranut.class, world, pos).ifPresent(qimranut -> {
				IEntangledStack entangled = (IEntangledStack) stack.getItem();
				Optional<UUID> optional = entangled.getKey(stack);
				if(!optional.isPresent() || RelativityHandler.getRelatives(optional.get()).size() >= 2) {
					entangled.setKey(stack, UUID.randomUUID());
				}
				entangled.getKey(stack).ifPresent(qimranut::setKey);
			});
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		getTile(TileQimranut.class, world, pos).ifPresent(qimranut -> {
			ItemStack stack = getItem(world, pos, state);
			spawnAsEntity(world, pos, stack);
		});
		super.breakBlock(world, pos, state);
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		Optional<TileQimranut> optional = getTile(TileQimranut.class, world, pos);
		if(optional.isPresent()) {
			TileQimranut qimranut = optional.get();
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			qimranut.getKey().ifPresent(uuid -> {
				((IEntangledStack) stack.getItem()).setKey(stack, uuid);
			});
			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing.getOpposite()).withProperty(State.ACTIVE, false);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockDirectional.FACING).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.values()[meta]);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileQimranut();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, (v, f) -> new BakedRender()
				.setTransforms(BakedPerspective.BLOCK_TRANSFORMS)
				.setParticle(ResourceLibrary.QIMRANUT_BASE)
		);
		ModelHandler.registerModel(this, 0, "");
	}
}
