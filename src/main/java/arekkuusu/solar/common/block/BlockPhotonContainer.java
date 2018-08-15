/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.common.block.tile.TilePhotonContainer;
import arekkuusu.solar.common.entity.Megumin;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;

/*
 * Created by <Arekkuusu> on 5/3/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockPhotonContainer extends BlockBase {

	public static final PropertyEnum<ContainerState> PROPERTY = PropertyEnum.create("container", ContainerState.class);

	public BlockPhotonContainer() {
		super(LibNames.PHOTON_CONTAINER, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(PROPERTY, ContainerState.INACTIVE));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(0.3F);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos from) {
		if(block != this) {
			boolean match = hasLuminicMechanism(world, pos);
			if(match && state.getValue(PROPERTY) == ContainerState.INACTIVE) {
				getTile(TilePhotonContainer.class, world, pos).ifPresent(tile -> {
					if(tile.hasLumen()) {
						world.setBlockState(pos, state.withProperty(PROPERTY, ContainerState.LUMEN_ON));
						tile.nou = true;
					} else {
						world.setBlockState(pos, state.withProperty(PROPERTY, ContainerState.LUMEN_OFF));
					}
				});
			} else if(!match && state.getValue(PROPERTY) != ContainerState.INACTIVE) {
				world.setBlockState(pos, state.withProperty(PROPERTY, ContainerState.INACTIVE));
			}
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return hasLuminicMechanism(world, pos) ? getDefaultState().withProperty(PROPERTY, ContainerState.LUMEN_OFF) : getDefaultState();
	}

	private boolean hasLuminicMechanism(World world, BlockPos pos) {
		return Arrays.stream(EnumFacing.values())
				.map(facing -> world.getBlockState(pos.offset(facing)).getBlock())
				.anyMatch(b -> b == ModBlocks.LUMINIC_MECHANISM);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if(state.getValue(PROPERTY) == ContainerState.LUMEN_ON) {
			Vector3 vec = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			Megumin.chant(world, vec, 5F, true).explosion();
			if(!world.isRemote) world.setBlockToAir(pos);
		}
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(PROPERTY) == ContainerState.LUMEN_ON ? 5 : 0;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PROPERTY).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PROPERTY, ContainerState.values()[meta]);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PROPERTY);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePhotonContainer();
	}

	public enum ContainerState implements IStringSerializable {
		INACTIVE,
		LUMEN_ON,
		LUMEN_OFF;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
