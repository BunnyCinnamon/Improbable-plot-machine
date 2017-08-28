/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
public class ItemGravityHopper extends ItemBaseBlock {

	public ItemGravityHopper() {
		super(ModBlocks.gravity_hopper);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0, ResourceLibrary.getModel("gravity_hopper_", ""));
	}
}
