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
import arekkuusu.implom.common.block.base.BlockBaseFacing;
import arekkuusu.implom.common.block.tile.TileAsymmetricalMachination;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
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

/*
 * Created by <Arekkuusu> on 6/21/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockAsymmetricalMachination extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = FacingAlignedBB.create(
			new Vector3(2, 6.5, 2),
			new Vector3(14, 15.5, 14),
			EnumFacing.UP
	).build();

	public BlockAsymmetricalMachination() {
		super(LibNames.ASYMMETRICAL_MACHINATION, IPMMaterial.MONOLITH);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING).getOpposite();
		BlockPos.MutableBlockPos posOffset = new BlockPos.MutableBlockPos(pos);
		float distance = 0;
		while(distance++ < Constants.REACH) {
			IBlockState found = world.getBlockState(posOffset.move(facing));
			if(found.getBlock() == ModBlocks.SYMMETRICAL_MACHINATION && found.getValue(BlockDirectional.FACING) == facing) {
				Vector3 offset = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable();
				Vector3 from = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5D).offset(offset, -0.19);
				IPM.getProxy().spawnBeam(world, from, offset, distance + 0.41F, 36, 0.75F, 0xFF0303, Light.GLOW, GlowTexture.GLOW.getTexture());
				break;
			} else if(found.getBlock() == ModBlocks.ASYMMETRICAL_MACHINATION) break;
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
		return new TileAsymmetricalMachination();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelRendered()
				.setParticle(ResourceLibrary.ASYMMETRICAL_MACHINATION)
		);
		ModelHelper.registerModel(this, 0);
	}

	public static class Constants {
		public static int REACH = 15;
	}
}
