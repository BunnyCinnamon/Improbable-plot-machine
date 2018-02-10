/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.state.MoonPhase;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedCosmicResonator;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Created by <Arekkuusu> on 29/12/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockCelestialResonator extends BlockBase {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0.3D,0.3D,0.3D, 0.7D, 0.7D, 0.7D);

	public BlockCelestialResonator() {
		super(LibNames.CELESTIAL_RESONATOR, Material.ROCK);
		setDefaultState(getDefaultState().withProperty(State.ACTIVE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.WOOD_GOLD);
		setHardness(0.5F);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote) {
			world.scheduleUpdate(pos, this, tickRate(world));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(!world.isRemote) {
			MoonPhase currentPhase = state.getValue(MoonPhase.MOON_PHASE);
			MoonPhase newPhase = MoonPhase.getMoonPhase(world);
			if(currentPhase != newPhase) {
				world.setBlockState(pos, state.withProperty(MoonPhase.MOON_PHASE, newPhase).withProperty(State.ACTIVE, true));
			} else if(state.getValue(State.ACTIVE)) {
				world.setBlockState(pos, state.withProperty(State.ACTIVE, false));
			}
			world.scheduleUpdate(pos, this, tickRate(world));
		}
	}

	@Override
	public int tickRate(World world) {
		return 1;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return state.getValue(State.ACTIVE);
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return state.getValue(State.ACTIVE) ? 1 : 0;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		MoonPhase phase = state.getValue(MoonPhase.MOON_PHASE);
		switch(phase) {
			case FULL_MOON:
				return 15;
			case WAXING_GIBBOUS:
			case WANING_GIBBOUS:
				return 12;
			case FIRST_QUARTER:
			case LAST_QUARTER:
				return 8;
			case WAXING_CRESCENT:
			case WANING_CRESCENT:
				return 3;
			case NEW_MOON:
			case ECLIPSE:
				return 0;
		}
		return 0;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(MoonPhase.MOON_PHASE).ordinal();

		if(state.getValue(State.ACTIVE)) {
			i |= 8;
		}

		return i;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		MoonPhase phase = MoonPhase.values()[meta & 7];

		return getDefaultState().withProperty(MoonPhase.MOON_PHASE, phase).withProperty(State.ACTIVE, (meta & 8) > 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, State.ACTIVE, MoonPhase.MOON_PHASE);
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
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, BakedCosmicResonator::new);
		ModelHandler.registerModel(this, 0, "");
	}
}
