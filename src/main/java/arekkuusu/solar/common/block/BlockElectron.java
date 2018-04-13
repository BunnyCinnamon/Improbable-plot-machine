/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.sound.SolarSounds;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.util.FixedDamage;
import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileElectron;
import arekkuusu.solar.common.block.tile.TileHyperConductor;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.mirror.client.baked.BakedRender;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by <Arekkuusu> on 26/10/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockElectron extends BlockBase {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0.3125D,0.3125D,0.3125D, 0.6875D, 0.6875D, 0.6875D);

	public BlockElectron() {
		super(LibNames.ELECTRON, Material.ROCK);
		setDefaultState(getDefaultState().withProperty(State.POWER, 0));
		setSound(SoundType.CLOTH);
		setHardness(1F);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote) {
			BlockPos from = pos.add(-8, -8, -8);
			BlockPos to = pos.add(8, 8, 8);
			BlockPos.getAllInBoxMutable(from, to).forEach(p ->
					getTile(TileHyperConductor.class, world, p).ifPresent(conductor -> conductor.addElectron(pos))
			);
			world.scheduleUpdate(pos, this, tickRate(world));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		int power = state.getValue(State.POWER);
		if(!world.isRemote && power > 0) {
			world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(1)).forEach(e -> {
				e.attackEntityFrom(FixedDamage.ELECTRICITY, power * 0.5F);
			});
		}
		world.scheduleUpdate(pos, this, tickRate(world));
	}

	@Override
	public int tickRate(World world) {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(state.getValue(State.POWER) > 0 && world.rand.nextBoolean()) {
			for(int i = 0; i < 1 + world.rand.nextInt(3); i++) {
				Vector3 from = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
				Vector3 to = Vector3.rotateRandom().add(from);
				FXUtil.spawnVolt(world, from, to, 4, 0.25F, 15, 0x5194FF, true, true);
			}
			FXUtil.playSound(world, pos, SolarSounds.SPARK, SoundCategory.BLOCKS, 0.05F);
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
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return (int) (state.getValue(State.POWER) * 0.5);
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
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileElectron();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setParticle(ResourceLibrary.ELECTRON)
		);
		ModelHandler.registerModel(this, 0);
	}
}
