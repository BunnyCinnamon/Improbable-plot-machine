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
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.block.tile.TileHyperConductor;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Created by <Arekkuusu> on 26/10/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockElectron extends BlockBase {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0.4D,0.4D,0.4D, 0.6D, 0.6D, 0.6D);

	public BlockElectron() {
		super(LibNames.ELECTRON, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(State.POWER, 0));
		setLightLevel(0.2F);
		setHardness(1F);
		setSound(SoundType.CLOTH);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote) {
			BlockPos vec = new BlockPos(8, 8, 8);
			BlockPos from = pos.add(vec);
			BlockPos to = pos.subtract(vec);
			BlockPos.getAllInBox(from, to).forEach(p -> getTile(TileHyperConductor.class, world, p).ifPresent(conductor -> {
				double distance = conductor.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
				if(distance <= 8D) {
					conductor.addElectron(world.getBlockState(pos), pos);
				}
			}));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		for(int i = 0; i <= Math.min(state.getValue(State.POWER), 8); i++) {
			if(rand.nextFloat() < 0.2F) {
				Vector3 vec = Vector3.getRandomVec(0.15F);
				vec.add(BB.getCenter());
				vec.add(pos.getX(), pos.getY(), pos.getZ());

				ParticleUtil.spawnChargedIce(world, vec,
						Vector3.ImmutableVector3.NULL, 0xFFFFFF, 45, 0.45F * rand.nextFloat());
			}
		}
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return state.getValue(State.POWER);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(State.POWER, placer.isSneaking() ? 15 : 0);
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
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

}
