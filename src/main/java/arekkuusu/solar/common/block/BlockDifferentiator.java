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
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileDifferentiator;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.mirror.client.baked.BakedRender;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockDifferentiator extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.25, 0.125, 0.25, 0.75, 0.875, 0.75))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.25, 0.125, 0.25, 0.75, 0.875, 0.75))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.25, 0.25, 0.875, 0.75, 0.75, 0.125))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.25, 0.25, 0.125, 0.75, 0.75, 0.875))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.875, 0.25, 0.25, 0.125, 0.75, 0.75))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.875, 0.25, 0.25, 0.125, 0.75, 0.75))
			.build();

	public BlockDifferentiator() {
		super(LibNames.DIFFERENTIATOR, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		BlockPos.MutableBlockPos posOffset = new BlockPos.MutableBlockPos(pos);
		float distance = 0;
		while (distance++ < TileDifferentiator.REACH) {
			IBlockState found = world.getBlockState(posOffset.move(facing));
			if(found.getBlock() == ModBlocks.DIFFERENTIATOR_INTERCEPTOR && found.getValue(BlockDirectional.FACING) == facing) {
				Vector3 offset = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable();
				Vector3 from = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5D).offset(offset, -0.19);
				FXUtil.addBeam(world, from, offset, distance + 0.7F, 36, 0.75F, 0xFF0303);
				break;
			}
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileDifferentiator();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setParticle(ResourceLibrary.DIFFERENTIATOR_BASE)
		);
		ModelHandler.registerModel(this, 0);
	}
}
