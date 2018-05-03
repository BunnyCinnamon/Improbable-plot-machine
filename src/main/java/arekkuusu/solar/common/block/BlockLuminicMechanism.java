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
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileLuminicMechanism;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.mirror.client.baked.BakedRender;
import net.katsstuff.mirror.data.Quat;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

/**
 * Created by <Arekkuusu> on 4/9/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockLuminicMechanism extends BlockBaseFacing {

	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.apply(0.5D, 0.1D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.apply(0.5D, 0.9D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.apply(0.5D, 0.5D, 0.9D))
			.put(EnumFacing.SOUTH, Vector3.apply(0.5D, 0.5D, 0.1D))
			.put(EnumFacing.EAST, Vector3.apply(0.1D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.apply(0.9D, 0.5D, 0.5D))
			.build();

	public BlockLuminicMechanism() {
		super(LibNames.LUMINIC_MECHANISM, FixedMaterial.DONT_MOVE);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
		setLightLevel(0.2F);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		/*EnumFacing facing = state.getValue(BlockDirectional.FACING);
		Vector3 back = getOffSet(facing.getOpposite(), pos);
		for(int i = 0; i < 6 + rand.nextInt(6); i++) {
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 45);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 45);
			double speed = 0.015D + rand.nextDouble() * 0.015D;
			Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
					.asImmutable()
					.multiply(speed)
					.rotate(x.multiply(z));
			Vector3 posVec = back.add(Vector3.rotateRandom().multiply(0.2D));
			FXUtil.spawnLight(world, posVec, speedVec, 45, 1F, 0xFFE077, Light.GLOW);
		}
		for(int i = 0; i < 5; i++) {
			double speed = 0.015D + rand.nextDouble() * 0.025D;
			Vector3 speedVec = Vector3.Down().multiply(speed);
			FXUtil.spawnLight(world, back, speedVec, 45, 2F, 0xFFE077, Light.GLOW);
		}*/
	}

	private Vector3 getOffSet(EnumFacing facing, BlockPos pos) {
		return FACING_MAP.get(facing).add(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileLuminicMechanism();
	}
}
