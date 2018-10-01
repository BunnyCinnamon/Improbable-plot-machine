/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.block.tile.TileHyperConductor;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedRender;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
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
 * Created by <Arekkuusu> on 25/10/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockHyperConductor extends BlockBase {

	public static final int ELECTRIC_FIELD_REACH = 10;
	private static final AxisAlignedBB BB = new AxisAlignedBB(0.25D,0.25D,0.25D, 0.75D, 0.75D, 0.75D);

	public BlockHyperConductor() {
		super(LibNames.HYPER_CONDUCTOR, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(State.POWER, 0));
		setHarvestLevel(Tool.PICK, ToolLevel.IRON);
		setHardness(1F);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this) {
			getTile(TileHyperConductor.class, world, pos).ifPresent(conductor -> {
				if(conductor.getElectrons().contains(fromPos)) return;
				boolean wasPowered = conductor.isPowered();
				boolean isPowered = world.isBlockPowered(pos);
				if((isPowered || block.getDefaultState().canProvidePower()) && isPowered != wasPowered) {
					conductor.setPowered(isPowered);
				}
			});
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		getTile(TileHyperConductor.class, world, pos).ifPresent(conductor -> {
			if(state.getValue(State.POWER) > 0) {
				conductor.hyperInduceAtmosphere();
			}
		});
		super.breakBlock(world, pos, state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		Vector3 origin = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
		for(EnumFacing facing : EnumFacing.values()) {
			Vector3 vec = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable().multiply(0.025D);
			Solar.getProxy().spawnSquared(world, origin, vec, 40, 4F, 0xFFFFFF);
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(State.POWER);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(State.POWER, meta);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, State.POWER);
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
		return BB;
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
		return new TileHyperConductor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setParticle(ResourceLibrary.HYPER_CONDUCTOR)
		);
		ModelHandler.registerModel(this, 0, "");
	}
}
