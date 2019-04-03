/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.api.state.enums.Direction;
import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelQelaion;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TileQelaion;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 24/02/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockQelaion extends BlockBase {

	public static final AxisAlignedBB BB = new AxisAlignedBB(0.0625, 0.0625, 0.0625, 0.9375, 0.9375, 0.9375);

	public BlockQelaion() {
		super(LibNames.QELAION, IPMMaterial.MONOLITH);
		setDefaultState(getDefaultState().withProperty(Properties.ACTIVE, true));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
		setLightLevel(0.2F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			getTile(TileQelaion.class, world, pos).ifPresent(tile -> tile.put(facing));
		}
		return true;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		getTile(TileQelaion.class, world, pos).ifPresent(tile -> {
			Vector3 posVec = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			boolean on = state.getValue(Properties.ACTIVE);
			for(EnumFacing facing : EnumFacing.values()) {
				if(!tile.facings.contains(facing)) continue;
				for(int i = 0; i < 1 + rand.nextInt(3); i++) {
					Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 6);
					Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 6);
					double speed = 0.025D + 0.0025D * rand.nextDouble();
					Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
							.asImmutable()
							.multiply(speed)
							.rotate(x.multiply(z));
					IPM.getProxy().spawnSpeck(world, posVec, speedVec, 60, 2F, on ? 0x49FFFF : 0xFF0303, Light.GLOW, ResourceLibrary.GLOW_PARTICLE);
				}
			}
		});
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(Properties.ACTIVE) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(Properties.ACTIVE, meta != 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer.Builder(this).add(Properties.ACTIVE).add(Properties.DIR_UNLISTED).build();
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getTile(TileQelaion.class, world, pos).map(tile -> {
			Direction direction = Direction.getDirectionFromFacings(Arrays.stream(EnumFacing.values())
					.filter(f -> !tile.facings.contains(f))
					.distinct()
					.toArray(EnumFacing[]::new));
			return (IBlockState) ((IExtendedBlockState) state).withProperty(Properties.DIR_UNLISTED, direction);
		}).orElse(super.getExtendedState(state, world, pos));
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileQelaion();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelQelaion());
		ModelHelper.registerModel(this, 0, "");
	}
}