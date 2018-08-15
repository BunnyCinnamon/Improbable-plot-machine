/*
 * Arekkuusu / solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.client.util.resource.sprite;

import arekkuusu.solar.client.util.resource.SpriteManager;
import arekkuusu.solar.common.Solar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by <Arekkuusu> on 03/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class SpriteResource {

	private final ResourceLocation location;
	private ITextureObject texture;

	public SpriteResource(ResourceLocation location) {
		this.location = location;
		load();
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public void bindManager() {
		if(texture == null || SpriteManager.isReloading()) return;
		GlStateManager.bindTexture(texture.getGlTextureId());
	}

	public void reload() {
		if(!SpriteManager.isReloading()) return;
		unLoad();
		load();
	}

	private void load() {
		if(texture != null) return;
		texture = new SimpleTexture(location);
		try {
			texture.loadTexture(Minecraft.getMinecraft().getResourceManager());
		} catch(Exception e) {
			Solar.LOG.warn("[Sprite Resource] Failed to load texture " + location.toString());
			e.printStackTrace();
			texture = TextureUtil.MISSING_TEXTURE;
		}
	}

	private void unLoad() {
		if(texture != null)
			GL11.glDeleteTextures(texture.getGlTextureId());
		texture = null;
	}
}
