/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 5/2/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockLumenCompressor extends BlockBaseFacing {

	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.apply(0.5D, 0.05D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.apply(0.5D, 0.95D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.apply(0.5D, 0.5D, 0.95D))
			.put(EnumFacing.SOUTH, Vector3.apply(0.5D, 0.5D, 0.05D))
			.put(EnumFacing.EAST, Vector3.apply(0.05D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.apply(0.95D, 0.5D, 0.5D))
			.build();
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0, 0.5, 0, 1, 1, 1))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0, 0, 0, 1, 0.5, 1))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0, 0, 0, 1, 1, 0.5))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0, 0, 0.5, 1, 1, 1))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.5, 0, 0, 1, 1, 1))
			.put(EnumFacing.WEST, new AxisAlignedBB(0, 0, 0, 0.5, 1, 1))
			.build();

	public BlockLumenCompressor() {
		super(LibNames.LUMEN_COMPRESSOR, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.DOWN));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing.getOpposite());
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		IBlockState stateFacing = world.getBlockState(pos.offset(facing));
		if(!stateFacing.getBlock().isFullCube(stateFacing)) {
			Vector3 back = getOffSet(facing.getOpposite(), pos);
			for(int i = 0; i < 3 + rand.nextInt(2); i++) {
				Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 45);
				Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 45);
				double speed = 0.005D + rand.nextDouble() * 0.015D;
				Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
						.asImmutable()
						.multiply(speed)
						.rotate(x.multiply(z));
				Vector3 posVec = back.add(Vector3.rotateRandom().multiply(0.2D));
				Solar.PROXY.spawnMute(world, posVec, speedVec, 45, 1F, 0xFFE077, Light.GLOW);
			}
			for(int i = 0; i < 3; i++) {
				double speed = 0.005D + rand.nextDouble() * 0.015D;
				Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable().multiply(speed);
				Solar.PROXY.spawnMute(world, back, speedVec, 45, 2F, 0xFFE077, Light.GLOW);
			}
		}
	}

	private Vector3 getOffSet(EnumFacing facing, BlockPos pos) {
		return FACING_MAP.get(facing).add(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return state.getValue(BlockDirectional.FACING).getOpposite() == facing ? BlockFaceShape.BOWL : BlockFaceShape.SOLID;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}
}
