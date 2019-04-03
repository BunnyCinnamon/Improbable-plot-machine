/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.sound.IMPSounds;
import arekkuusu.implom.api.util.IPMDamage;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TileElectron;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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

/*
 * Created by <Arekkuusu> on 26/10/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockElectron extends BlockBase {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);

	public BlockElectron() {
		super(LibNames.ELECTRON, Material.ROCK);
		setSound(SoundType.CLOTH);
		setHardness(1F);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote) {
			world.scheduleUpdate(pos, this, tickRate(world));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		getTile(TileElectron.class, world, pos).filter(tile -> tile.power > 0).ifPresent(tile -> {
			world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(1)).forEach(e -> {
				e.attackEntityFrom(IPMDamage.SHOCK, tile.power * 0.5F);
			});
		});
		world.scheduleUpdate(pos, this, tickRate(world));
	}

	@Override
	public int tickRate(World world) {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(getTile(TileElectron.class, world, pos).map(tile -> tile.power > 0).orElse(false)) {
			for(int i = 0; i < 1 + world.rand.nextInt(3); i++) {
				Vector3 from = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
				Vector3 to = Vector3.rotateRandom().add(from);
				IPM.getProxy().spawnArcDischarge(world, from, to, 4, 0.25F, 15, 0x5194FF, true, true);
			}
			IPM.getProxy().playSound(world, pos, IMPSounds.SPARK, SoundCategory.BLOCKS, 0.05F);
		}
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return getTile(TileElectron.class, world, pos).map(tile -> tile.power).orElse(0);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return (int) (getTile(TileElectron.class, world, pos).map(tile -> tile.power).orElse(0) * 0.5);
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

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
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
		DummyModelRegistry.register(this, new ModelRendered()
				.setParticle(ResourceLibrary.ELECTRON)
		);
		ModelHelper.registerModel(this, 0);
	}
}
