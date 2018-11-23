/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.capability.relativity.RelativityHelper;
import arekkuusu.implom.api.helper.RayTraceHelper;
import arekkuusu.implom.api.state.State;
import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.block.tile.TileMechanicalTranslocator;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedPerspective;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 16/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockMechanicalTranslocator extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_BODY_MAP = FacingAlignedBB.create(
			new Vector3(4, 9.5, 4),
			new Vector3(12, 14.5, 12),
			EnumFacing.UP
	).build();
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_CRYSTAL_MAP = FacingAlignedBB.create(
			new Vector3(7, 7, 7),
			new Vector3(9, 9, 9),
			EnumFacing.UP
	).build();

	public BlockMechanicalTranslocator() {
		super(LibNames.MECHANICAL_TRANSLOCATOR, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(State.ACTIVE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileMechanicalTranslocator.class, world, pos).ifPresent(translocator -> {
				RelativityHelper.getCapability(translocator).ifPresent(handler -> {
					if(!handler.getKey().isPresent()) {
						RelativityHelper.getCapability(stack).ifPresent(subHandler -> {
							if(!subHandler.getKey().isPresent()) subHandler.setKey(UUID.randomUUID());
							subHandler.getKey().ifPresent(handler::setKey);
						});
					}
				});
			});
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		ItemStack stack = super.getItem(world, pos, state);
		getTile(TileMechanicalTranslocator.class, world, pos).ifPresent(translocator -> {
			RelativityHelper.getCapability(translocator).ifPresent(handler -> {
				handler.getKey().ifPresent(key -> {
					RelativityHelper.getCapability(stack).ifPresent(subHandler -> subHandler.setKey(key));
				});
			});
		});
		return stack;
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
		return new TileMechanicalTranslocator();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelRendered()
				.setTransforms(ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
						.put(ItemCameraTransforms.TransformType.GUI, BakedPerspective.mkTransform(0F, 2F, 0F, 30F, 45F, 0F, 0.75F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, 3F, 0F, 0F, 45F, 0F, 0.5F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, 3F, 0F, 0F, 225F, 0F, 0.5F))
						.put(ItemCameraTransforms.TransformType.GROUND, BakedPerspective.mkTransform(0F, 3.5F, 0F, 0F, 0F, 0F, 0.25F))
						.put(ItemCameraTransforms.TransformType.FIXED, BakedPerspective.mkTransform(0F, 1F, 0F, 0F, 0F, 0F, 0.5F))
						.build()
				).setParticle(ResourceLibrary.MECHANICAL_TRANSLOCATOR)
		);
		ModelHandler.registerModel(this, 0, "");
	}
}
