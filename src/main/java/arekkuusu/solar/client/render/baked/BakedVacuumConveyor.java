/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.client.render.SpecialModelRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class BakedVacuumConveyor extends BakedRender {

	private final ItemOverrides overrides = new ItemOverrides();

	public BakedVacuumConveyor() {
		setTransforms(BakedPerspective.BLOCK_TRANSFORMS);
	}

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
			if(NBTHelper.hasTag(stack, "lookup")) {
				ItemStack lookup = new ItemStack(NBTHelper.<NBTTagCompound>getNBT(stack, "lookup").orElse(new NBTTagCompound()));
				if(!lookup.isEmpty()) {
					SpecialModelRenderer.setTempItemRenderer(lookup);
				}
			}
			return originalModel;
		}
	}
}
