/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileDilaton;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Created by <Snack> on 04/02/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockDilaton extends BlockBaseFacing {

	public static final PropertyEnum<DilatonHead> MODE = PropertyEnum.create("mode", DilatonHead.class);

	public BlockDilaton() {
		super(LibNames.DILATON, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(State.ACTIVE, false));
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this) {

		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(BlockDirectional.FACING).ordinal();
		if(state.getValue(State.ACTIVE)) {
			i |= 8;
		}
		return i;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.values()[meta & 7];
		return this.getDefaultState().withProperty(BlockDirectional.FACING, enumfacing).withProperty(State.ACTIVE, (meta & 8) > 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockDirectional.FACING, State.ACTIVE);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileDilaton();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0, "");
	}

	public static class BlockDilatonPiece extends BlockBaseFacing {

		public BlockDilatonPiece() {
			super(LibNames.DILATON_EXTENSION, FixedMaterial.DONT_MOVE);
			setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(MODE, DilatonHead.DEFAULT));
			setBlockUnbreakable();
		}

		@Override
		public int getMetaFromState(IBlockState state) {
			int i = state.getValue(BlockDirectional.FACING).ordinal();
			if(state.getValue(MODE) == DilatonHead.DEFAULT) {
				i |= 8;
			}
			return i;
		}

		@Override
		public IBlockState getStateFromMeta(int meta) {
			EnumFacing enumfacing = EnumFacing.values()[meta & 7];
			return this.getDefaultState().withProperty(BlockDirectional.FACING, enumfacing).withProperty(MODE, (meta & 8) > 0 ? DilatonHead.DEFAULT : DilatonHead.OVERRIDE);
		}

		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, BlockDirectional.FACING, MODE);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void registerModel() {
			//Yoink!
		}
	}

	public enum DilatonHead implements IStringSerializable {
		DEFAULT,
		OVERRIDE;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
