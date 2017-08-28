/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.client.util.helper.IModel;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.handler.CreativeTabHandler;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
 */
public class BlockBase extends Block implements IModel {

	public BlockBase(String id, Material material) {
		super(material);
		setUnlocalizedName(id);
		setDefaultState(defaultState());
		setRegistryName(LibMod.MOD_ID, id);
		setCreativeTab(CreativeTabHandler.MISC_BLOCKS);
	}

	public Block setSound(SoundType type) {
		return super.setSoundType(type);
	}

	protected IBlockState defaultState() {
		return blockState.getBaseState();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
