/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.quantum.IQuantumItem;
import arekkuusu.solar.client.render.TESRModelRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class QuantumMirrorBakedModel extends RenderedBakedModel {

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
			Optional<UUID> optional = ((IQuantumItem) stack.getItem()).getKey(stack);
			if(optional.isPresent()) {
				ItemStack mirrored = SolarApi.getQuantumStack(optional.get(), 0);
				if(!mirrored.isEmpty()) {
					TESRModelRenderer.setTempItemRenderer(mirrored);
				}
			}
			return originalModel;
		}
	}
}
