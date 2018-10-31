package arekkuusu.implom.client.render.entity;

import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class LumenRenderer extends RenderEntity {

	public LumenRenderer(RenderManager aaa) {
		super(aaa);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		//NO-OP
	}
}
