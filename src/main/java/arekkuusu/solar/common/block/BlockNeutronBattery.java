/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.common.block.tile.TileNeutronBattery;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockNeutronBattery extends BlockBase {

	public static final PropertyEnum<TileNeutronBattery.Capacity> CAPACITY = PropertyEnum.create("capacity", TileNeutronBattery.Capacity.class);

	public BlockNeutronBattery() {
		super(LibNames.NEUTRON_BATTERY, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(CAPACITY, TileNeutronBattery.Capacity.BLUE));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(CAPACITY, TileNeutronBattery.Capacity.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(CAPACITY).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CAPACITY);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileNeutronBattery(state.getValue(CAPACITY));
	}
}
