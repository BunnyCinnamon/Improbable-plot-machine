/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TileQimranut;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedPerspective;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
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
import java.util.Random;

/*
 * Created by <Arekkuusu> on 23/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockQimranut extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = FacingAlignedBB.create(
			new Vector3(4, 8, 4),
			new Vector3(12, 15, 12),
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

	public BlockQimranut() {
		super(LibNames.QIMRANUT, IPMMaterial.MONOLITH);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
		setLightLevel(0.2F);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileQimranut.class, world, pos).ifPresent(tile -> {
				tile.fromItemStack(stack);
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
		getTile(TileQimranut.class, world, pos).ifPresent(tile -> {
			tile.toItemStack(stack);
		});
		return stack;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		Vector3 back = getOffSet(facing.getOpposite(), pos);
		for(int i = 0; i < 3 + rand.nextInt(6); i++) {
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 45);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 45);
			double speed = 0.005D + rand.nextDouble() * 0.005D;
			Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
					.asImmutable()
					.multiply(speed)
					.rotate(x.multiply(z));
			IPM.getProxy().spawnSpeck(world, back, speedVec, 45, 1F, 0x1BE564, Light.GLOW, ResourceLibrary.GLOW_PARTICLE);
		}
	}

	private Vector3 getOffSet(EnumFacing facing, BlockPos pos) {
		return FACING_MAP.get(facing).add(pos.getX(), pos.getY(), pos.getZ());
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
				).setParticle(ResourceLibrary.QIMRANUT)
		);
		ModelHelper.registerModel(this, 0, "");
	}

	public static class Constants {
		public static final String NBT_WORLD_ACCESS = "worldaccess";
	}
}
