package arekkuusu.implom.common.block.base;

import arekkuusu.implom.client.util.helper.IModel;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.block.ModBlocks;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockBaseGlass extends BlockGlass implements IModel {

	public BlockBaseGlass(String id, Material material) {
		super(material, false);
		this.setDefaultState(defaultState());
		ModBlocks.setRegistry(this, id);
	}

	public IBlockState defaultState() {
		return blockState.getBaseState();
	}

	@Override
	public void registerModel() {
		ModelHelper.registerModel(this, 0);
	}
}
