/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.ModBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Snack> on 06/02/2018.
 * It's distributed as part of Solar.
 */
public class ItemDilaton extends ItemBaseBlock {

	public ItemDilaton() {
		super(ModBlocks.DILATON);
		addPropertyOverride(new ResourceLocation("active"), (stack, world, entity) ->
				stack.getOrCreateSubCompound("dilaton").getBoolean("active") ? 1F : 0F
		);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
