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
import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileQimranut;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.mirror.client.baked.BakedPerspective;
import net.katsstuff.mirror.client.baked.BakedRender;
import net.katsstuff.mirror.data.Quat;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 23/12/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockQimranut extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.1875, 0.625, 0.1875, 0.8125, 0.875, 0.8125))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.1875, 0.125, 0.1875, 0.8125, 0.375, 0.8125))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.1875, 0.1875, 0.375, 0.8125, 0.8125, 0.125))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.1875, 0.1875, 0.625, 0.8125, 0.8125, 0.875))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.875, 0.1875, 0.1875, 0.625, 0.8125, 0.8125))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.375, 0.1875, 0.1875, 0.125, 0.8125, 0.8125))
			.build();
	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.apply(0.5D, 0.2D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.apply(0.5D, 0.8D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.apply(0.5D, 0.5D, 0.8D))
			.put(EnumFacing.SOUTH, Vector3.apply(0.5D, 0.5D, 0.2D))
			.put(EnumFacing.EAST, Vector3.apply(0.2D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.apply(0.8D, 0.5D, 0.5D))
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
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
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
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		Vector3 back = getOffSet(facing.getOpposite(), pos);
		for(int i = 0; i < 3 + rand.nextInt(6); i++) {
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 75);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 75);
			double speed = 0.005D + rand.nextDouble() * 0.005D;
			Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
					.asImmutable()
					.multiply(speed)
					.rotate(x.multiply(z));
			FXUtil.spawnLight(world, back, speedVec, 45, 2F, 0x1BE564, Light.GLOW);
		}
	}

	private Vector3 getOffSet(EnumFacing facing, BlockPos pos) {
		return FACING_MAP.get(facing).add(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
				.put(EnumFacing.UP, new AxisAlignedBB(0.25, 0.5, 0.25, 0.75, 0.9375, 0.75))
				.put(EnumFacing.DOWN, new AxisAlignedBB(0.25, 0.0625, 0.25, 0.75, 0.5, 0.75))
				.put(EnumFacing.NORTH, new AxisAlignedBB(0.25, 0.25, 0.5, 0.75, 0.75, 0.0625))
				.put(EnumFacing.SOUTH, new AxisAlignedBB(0.25, 0.25, 0.5, 0.75, 0.75, 0.9375))
				.put(EnumFacing.EAST, new AxisAlignedBB(0.9375, 0.25, 0.25, 0.5, 0.75, 0.75))
				.put(EnumFacing.WEST, new AxisAlignedBB(0.5, 0.25, 0.25, 0.0625, 0.75, 0.75))
				.build();
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
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setTransformsJava(ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
						.put(ItemCameraTransforms.TransformType.GUI, BakedPerspective.mkTransform(0F, 2F, 0F, 30F, 45F, 0F, 0.75F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, 3F, 0F, 0F, 45F, 0F, 0.5F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, 3F, 0F, 0F, 225F, 0F, 0.5F))
						.put(ItemCameraTransforms.TransformType.GROUND, BakedPerspective.mkTransform(0F, 3.5F, 0F, 0F, 0F, 0F, 0.25F))
						.put(ItemCameraTransforms.TransformType.FIXED, BakedPerspective.mkTransform(0F, 1F, 0F, 0F, 0F, 0F, 0.5F))
						.build())
				.setParticle(ResourceLibrary.QIMRANUT_BASE)
		);
		ModelHandler.registerModel(this, 0, "");
	}
}
