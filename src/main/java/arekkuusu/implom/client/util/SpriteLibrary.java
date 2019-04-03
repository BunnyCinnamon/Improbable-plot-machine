package arekkuusu.implom.client.util;

import arekkuusu.implom.client.util.sprite.SpriteAtlasResource;
import arekkuusu.implom.client.util.sprite.SpriteFrameResource;
import arekkuusu.implom.client.util.sprite.SpriteResource;
import arekkuusu.implom.common.IPM;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class SpriteLibrary {

	public static final SpriteAtlasResource EMPTY = find(
			ResourceLibrary.EMPTY
	);
	public static final SpriteAtlasResource QUANTA = find(
			ResourceLibrary.QUANTA
	);
	public static final SpriteAtlasResource MUTATOR_SELECTION = find(
			ResourceLibrary.MUTATOR_SELECTION
	);
	public static final SpriteFrameResource QUANTUM_MIRROR = load(
			ResourceLibrary.QUANTUM_MIRROR, 9, 1
	);

	public static SpriteAtlasResource find(ResourceLocation location) {
		SpriteAtlasResource sprite = new SpriteAtlasResource(location);
		ResourceLibrary.ATLAS_SET.add(location);
		return sprite;
	}

	public static SpriteResource load(ResourceLocation location) {
		return new SpriteResource(location);
	}

	public static SpriteFrameResource load(ResourceLocation location, int rows, int columns) {
		if(rows <= 0 || columns <= 0) {
			IPM.LOG.fatal("[SpriteLibrary] Your sprite can't have 0 rows or columns" + location.toString());
		}
		return new SpriteFrameResource(location, rows, columns);
	}

	public static void preInit() {
		IPM.LOG.info("[MAKING PIE!]");
	}
}
