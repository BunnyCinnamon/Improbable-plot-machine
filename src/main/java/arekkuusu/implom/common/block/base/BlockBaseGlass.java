package arekkuusu.implom.common.block.base;

import arekkuusu.implom.client.util.helper.IModel;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.block.ModBlocks;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Optional;

public class BlockBaseGlass extends BlockGlass implements IModel {

	public BlockBaseGlass(String id, Material material) {
		super(material, false);
		this.setDefaultState(defaultState());
		ModBlocks.setRegistry(this, id);
	}

	@SuppressWarnings("unchecked")
	public <T extends TileEntity> Optional<T> getTile(Class<T> clazz, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return clazz.isInstance(tile) ? Optional.of((T) tile) : Optional.empty();
	}

	public IBlockState defaultState() {
		return blockState.getBaseState();
	}

	@Override
	public void registerModel() {
		ModelHelper.registerModel(this, 0);
	}
}
