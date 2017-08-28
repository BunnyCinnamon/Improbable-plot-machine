/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.client.util.helper.IModel;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * Created by <Arekkuusu> on 25/08/2017.
 * It's distributed as part of Solar.
 */
public abstract class ItemBaseBlock extends ItemBlock implements IModel {

	@SuppressWarnings("ConstantConditions")
	ItemBaseBlock(Block block) {
		super(block);
		this.setRegistryName(block.getRegistryName());
	}

	@Override
	public void registerModel() {

	}
}
