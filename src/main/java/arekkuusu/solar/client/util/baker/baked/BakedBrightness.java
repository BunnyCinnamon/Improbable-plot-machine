/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.client.proxy.ClientProxy;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by <Arekkuusu> on 03/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public abstract class BakedBrightness extends BakedPerspective {

	private final VertexFormat format;

	public BakedBrightness(VertexFormat format) {
		this.format = new VertexFormat(format);
		if(!ClientProxy.isOptifineInstalled()) {
			this.format.addElement(DefaultVertexFormats.TEX_2S);
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing facing, long rand) {
		if(facing != null) return Collections.emptyList();
		return getQuads(state, state == null ? DefaultVertexFormats.ITEM : format);
	}

	abstract List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format);

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(Lists.newArrayList()) {
			@Nullable
			@Override
			@SuppressWarnings("deprecation")
			public ResourceLocation applyOverride(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return null;
			}
		};
	}
}
