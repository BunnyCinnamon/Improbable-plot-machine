/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.client.util.helper.IModel;
import arekkuusu.solar.client.util.helper.ModelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class BlockBase extends Block implements IModel {

	public BlockBase(String id, Material material) {
		super(material);
		this.setDefaultState(defaultState());
		ModBlocks.setRegistry(this, id);
	}

	public Block setHarvestLevel(Tool lvl, ToolLevel i) {
		setHarvestLevel(lvl.name, i.ordinal());
		return this;
	}

	public Block setSound(SoundType type) {
		return super.setSoundType(type);
	}

	IBlockState defaultState() {
		return blockState.getBaseState();
	}

	@SuppressWarnings("unchecked")
	<T extends TileEntity> Optional<T> getTile(Class<T> clazz, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return clazz.isInstance(tile) ? Optional.of((T) tile) : Optional.empty();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}

	public enum Tool {
		PICK("pickaxe"),
		AXE("axe"),
		SHOVEL("shovel");

		final String name;

		Tool(String name) {
			this.name = name;
		}
	}

	public enum ToolLevel {
		WOOD_GOLD,
		STONE,
		IRON,
		DIAMOND
	}
}
