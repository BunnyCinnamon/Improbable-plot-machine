/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.block.tile.TileDifferentiatorInterceptor;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
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

/*
 * Created by <Arekkuusu> on 6/21/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockDifferentiatorInterceptor extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.125, 0.40625, 0.125, 0.875, 0.875, 0.875))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.125, 0.125, 0.125, 0.875, 0.59375, 0.875))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.125, 0.125, 0.125, 0.875, 0.875, 0.59375))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.125, 0.125, 0.875, 0.875, 0.875, 0.40625))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.875, 0.125, 0.125, 0.40625, 0.875, 0.875))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.125, 0.125, 0.125, 0.59375, 0.875, 0.875))
			.build();

	public BlockDifferentiatorInterceptor() {
		super(LibNames.DIFFERENTIATOR_INTERCEPTOR, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
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
		return new TileDifferentiatorInterceptor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelRendered()
				.setParticle(ResourceLibrary.DIFFERENTIATOR_INTERCEPTOR_BASE)
		);
		ModelHandler.registerModel(this, 0);
	}
}
