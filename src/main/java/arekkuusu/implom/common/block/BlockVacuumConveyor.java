/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.block.tile.TileVacuumConveyor;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static net.minecraft.block.BlockDirectional.FACING;

/*
 * Created by <Arekkuusu> on 06/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockVacuumConveyor extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.1875, 0.15625, 0.1875, 0.8125, 0.84375, 0.8125))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.1875, 0.15625, 0.1875, 0.8125, 0.84375, 0.8125))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.1875, 0.1875, 0.15625, 0.8125, 0.8125, 0.84375))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.1875, 0.1875, 0.15625, 0.8125, 0.8125, 0.84375))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.15625, 0.1875, 0.1875, 0.84375, 0.8125, 0.8125))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.15625, 0.1875, 0.1875, 0.84375, 0.8125, 0.8125))
			.build();

	public BlockVacuumConveyor() {
		super(LibNames.VACUUM_CONVEYOR, IPMMaterial.MONOLITH);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.DOWN));
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing side = EnumFacing.getDirectionFromEntityLiving(pos, placer);
		return defaultState().withProperty(FACING, side.getOpposite());
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
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
		return new TileVacuumConveyor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelRendered()
				.setParticle(ResourceLibrary.VACUUM_CONVEYOR)
		);
		ModelHelper.registerModel(this, 0, "");
	}
}
