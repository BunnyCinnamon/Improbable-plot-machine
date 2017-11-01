/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.api.material.FixedMaterial;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.block.tile.TileElectronNode;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

import static arekkuusu.solar.api.state.Power.POWER_AMOUNT;

/**
 * Created by <Arekkuusu> on 26/10/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockElectronNode extends BlockBase {

	private final AxisAlignedBB box = new AxisAlignedBB(0.4D,0.4D,0.4D, 0.6D, 0.6D, 0.6D);

	public BlockElectronNode() {
		super(LibNames.ELECTRON_NODE, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(POWER_AMOUNT, 0));
		setLightLevel(0.2F);
		setHardness(1F);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		for(int i = 0; i <= state.getValue(POWER_AMOUNT); i++) {
			if(rand.nextFloat() < 0.6F) {
				Vector3 vec = Vector3.getRandomVec(0.20F);
				vec.add(box.getCenter());
				vec.add(pos.getX(), pos.getY(), pos.getZ());

				ParticleUtil.spawnChargedIce(world, vec.x, vec.y, vec.z,
						0, 0, 0, 0xFFFFFF, 8, 0.45F * rand.nextFloat());
			}
		}
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return state.getValue(POWER_AMOUNT);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(POWER_AMOUNT, placer.isSneaking() ? 15 : 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(POWER_AMOUNT);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(POWER_AMOUNT, meta);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, POWER_AMOUNT);
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
		return box;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}


	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileElectronNode();
	}
}
