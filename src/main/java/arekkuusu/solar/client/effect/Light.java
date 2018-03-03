package arekkuusu.solar.client.effect;

import arekkuusu.solar.client.util.ResourceLibrary;
import net.minecraft.util.ResourceLocation;

public enum Light {
	DULL(ResourceLibrary.DULL_PARTICLE, false),
	GLOW(ResourceLibrary.GLOW_PARTICLE, true);

	public final ResourceLocation location;
	public final boolean additive;

	Light(ResourceLocation location, boolean additive) {
		this.location = location;
		this.additive = additive;
	}
}
