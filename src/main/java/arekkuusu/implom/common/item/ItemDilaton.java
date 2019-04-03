/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.block.ModBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by <Arekkuusu> on 06/02/2018.
 * It's distributed as part of Improbable plot machine.
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
		ModelHelper.registerModel(this, 0);
	}
}
