package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.common.block.base.BlockBase;
import arekkuusu.implom.common.block.tile.TileBlastFurnacePipe;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class BlockBlastFurnacePipe extends BlockBase {

	public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
			Stream.of(EnumFacing.VALUES)
					.map(facing -> PropertyBool.create(facing.getName()))
					.collect(Collectors.toList())
	);
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = FacingAlignedBB.create(
			new Vector3(5.5, 10, 5.5),
			new Vector3(9.5, 16, 9.5),
			EnumFacing.UP
	).build();
	private static final AxisAlignedBB BB = FacingAlignedBB.create(new Vector3(5.5, 5.5, 5.5), new Vector3(10.5, 10.5, 10.5));

	public BlockBlastFurnacePipe() {
		super(LibNames.BLAST_FURNACE_PIPE, IPMMaterial.FIRE_BRICK);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		getTile(TileBlastFurnacePipe.class, worldIn, pos).ifPresent(TileBlastFurnacePipe::resetConnections);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer.Builder(this).add(CONNECTED_PROPERTIES.toArray(new IProperty[0])).build();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		for(EnumFacing facing : EnumFacing.VALUES) {
			boolean connected = canConnectTo(world, pos, facing);
			state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()), connected);
		}
		return state;
	}

	public boolean canConnectTo(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection) {
		BlockPos neighbourPos = ownPos.offset(neighbourDirection);
		IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
		Block neighbourBlock = neighbourState.getBlock();

		boolean canConnect = false;
		if(neighbourBlock == ModBlocks.BLAST_FURNACE_PIPE) {
			canConnect = true;
		}
		else if(neighbourBlock == ModBlocks.BLAST_FURNACE_PIPE_GAUGE && ownPos.offset(neighbourState.getValue(BlockDirectional.FACING).getOpposite()).equals(neighbourPos)) {
			canConnect = true;
		}
		else if(Optional.ofNullable(worldIn.getTileEntity(neighbourPos)).map(tile ->
						tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighbourDirection.getOpposite())
		).orElse(false)) {
			canConnect = true;
		}

		return canConnect;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, BB);
		for(EnumFacing facing : EnumFacing.VALUES) {
			if(isConnected(getActualState(state, worldIn, pos), facing)) {
				AxisAlignedBB axisAlignedBB = BB_MAP.get(facing);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, axisAlignedBB);
			}
		}
	}

	public final boolean isConnected(IBlockState state, EnumFacing facing) {
		return state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
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
		return new TileBlastFurnacePipe();
	}
}
