package arekkuusu.implom.client.util.baker.baked;

import net.katsstuff.teamnightclipse.mirror.client.baked.BakedRender;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Map;

public class BakedRendered extends BakedRender {

	private final ItemOverrideList override;
	private final ResourceLocation particle;

	public BakedRendered(Map<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, ItemOverrideList override, ResourceLocation particle) {
		setTransformsJava(transforms);
		this.override = override;
		this.particle = particle;
	}

	@Override
	public ResourceLocation getParticle() {
		return particle;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return override;
	}
}
