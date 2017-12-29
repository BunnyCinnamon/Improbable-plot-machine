/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.api.sound.SolarSounds;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.client.render.baked.BakedElectron;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileElectron;
import arekkuusu.solar.common.block.tile.TileHyperConductor;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
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
		super(LibNames.ELECTRON, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(State.POWER, 0));
		setSound(SoundType.CLOTH);
		setLightLevel(0.2F);
		setHardness(1F);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote) {
			BlockPos vec = new BlockPos(8, 8, 8);
			BlockPos from = pos.add(vec);
			BlockPos to = pos.subtract(vec);
			BlockPos.getAllInBox(from, to).forEach(p ->
					getTile(TileHyperConductor.class, world, p).ifPresent(conductor -> conductor.addElectron(pos))
			);
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
		if(state.getValue(State.POWER) > 0 && world.rand.nextBoolean()) {
			for(int i = 0; i < 1 + world.rand.nextInt(3); i++) {
				Vector3 from = Vector3.create(pos).add(0.5D);
				Vector3 to = Vector3.create(0, 1, 0)
						.rotate(EnumFacing.Axis.X, world.rand.nextFloat() * 360)
						.rotate(EnumFacing.Axis.Y, world.rand.nextFloat() * 360)
						.rotate(EnumFacing.Axis.Z, world.rand.nextFloat() * 360)
						.add(from);
				ParticleUtil.spawnBolt(world, from, to, 4, 0.25F, 0x5194FF, true, true);
			}
			((WorldClient) world).playSound(pos, SolarSounds.SPARK, SoundCategory.BLOCKS, 0.05F, 1F, false);
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
		DummyBakedRegistry.register(Item.getItemFromBlock(this), BakedElectron::new);
		ModelHandler.registerModel(this, 0, "");
	}
}
