/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.common.block.tile.TileQuartzConsumer;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.mirror.data.Quat;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by <Arekkuusu> on 4/30/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockQuartzConsumer extends BlockBase {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.125, 0, 0.375, 0.875, 0.9375, 0.625))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.125, 0, 0.375, 0.875, 0.9375, 0.625))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.625, 0, 0.875, 0.375, 0.9375, 0.125))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.625, 0, 0.875, 0.375, 0.9375, 0.125))
			.build();

	public BlockQuartzConsumer() {
		super(LibNames.QUARTZ_CONSUMER, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		Vector3 posVec = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5D, 0.55D, 0.5D);
		getTile(TileQuartzConsumer.class, world, pos).ifPresent(t -> {
			if(t.getHasItem()) {
				EnumFacing facing = state.getValue(BlockHorizontal.FACING);
				for(int i = 0; i < 3 + rand.nextInt(6); i++) {
					Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 5);
					Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 5);
					double speed = world.rand.nextDouble() * 0.03D;
					facing = facing.getOpposite();
					Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
							.asImmutable()
							.rotate(x.multiply(z))
							.multiply(speed);
					FXUtil.spawnLight(world, posVec, speedVec, 45, 1.5F, 0x49FFFF, Light.GLOW);
				}
			} else for(int i = 0; i < 3 + rand.nextInt(6); i++) {
				Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 45);
				Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 45);
				Vector3 randVec = Vector3.randomVector().multiply(0.1D);
				double speed = 0.005D + rand.nextDouble() * 0.005D;
				Vector3 speedVec = Vector3.rotateRandom()
						.multiply(speed)
						.rotate(x.multiply(z));
				FXUtil.spawnLight(world, posVec.add(randVec), speedVec, 45, 0.5F, 0x49FFFF, Light.GLOW);
			}
		});
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		return getTile(TileQuartzConsumer.class, world, pos).map(consumer -> consumer.consume(stack)).orElse(false);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockHorizontal.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockHorizontal.FACING);
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		EnumFacing facing = rot.rotate(state.getValue(BlockHorizontal.FACING));
		if(facing.getAxis().isHorizontal()) {
			state = state.withProperty(BlockHorizontal.FACING, facing);
		}
		return state;
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withProperty(BlockHorizontal.FACING, mirror.mirror(state.getValue(BlockHorizontal.FACING)));
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return face != EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileQuartzConsumer();
	}
}
