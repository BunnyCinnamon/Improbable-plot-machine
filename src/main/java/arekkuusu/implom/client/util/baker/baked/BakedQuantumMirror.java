/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.baker.baked;

import arekkuusu.implom.api.capability.inventory.EntangledIItemHandler;
import arekkuusu.implom.api.capability.inventory.EntangledIItemHelper;
import arekkuusu.implom.client.render.SpecialModelRenderer;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedRender;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;

/*
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public class BakedQuantumMirror extends BakedRender {

	private final ItemOverrides overrides = new ItemOverrides();

	@Override
	public ItemOverrideList getOverrides() {
		return overrides;
	}

	private static class ItemOverrides extends ItemOverrideList {

		ItemOverrides() {
			super(Collections.emptyList());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
			EntangledIItemHelper.getCapability(stack).ifPresent(entangled -> {
				entangled.getKey().ifPresent(key -> {
					ItemStack mirrored = EntangledIItemHandler.getEntanglementStack(key, 0);
					if(!mirrored.isEmpty()) {
						SpecialModelRenderer.setTempItemRenderer(mirrored);
					}
				});
			});
			return originalModel;
		}
	}
}
