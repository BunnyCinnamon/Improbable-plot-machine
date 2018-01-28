/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.client.util.resource.SpriteManager;
import arekkuusu.solar.client.util.resource.sprite.FrameSpriteResource;
import arekkuusu.solar.client.util.resource.sprite.SpriteResource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static arekkuusu.solar.client.util.ResourceLibrary.TextureLocation.*;

/**
 * Created by <Arekkuusu> on 03/07/2017.
 * It's distributed as part of solar.
 */
@SideOnly(Side.CLIENT)
public final class SpriteLibrary {

	//Particle
	public static final FrameSpriteResource QUORN_PARTICLE = SpriteManager.load(EFFECT, "quorn_particle", 7, 1);
	public static final SpriteResource NEUTRON_PARTICLE = SpriteManager.load(EFFECT, "neutron_particle");
	public static final SpriteResource LIGHT_PARTICLE = SpriteManager.load(EFFECT, "light_particle");
	public static final SpriteResource DARK_PARTICLE = SpriteManager.load(EFFECT, "dark_particle");
	public static final SpriteResource CHARGED_ICE = SpriteManager.load(EFFECT, "charged_ice");
	public static final FrameSpriteResource SQUARED = SpriteManager.load(EFFECT, "squared", 11, 1);
	//Other
	public static final SpriteResource EYE_OF_SCHRODINGER_LAYER = SpriteManager.load(MODEL, "eye_of_schrodinger_layer");
	public static final FrameSpriteResource QUANTUM_MIRROR = SpriteManager.load(BLOCKS, "quantum_mirror", 9, 1);
	public static final SpriteResource Q_SQUARED = SpriteManager.load(BLOCKS, "q_squared");
	public static final SpriteResource THEOREMA = SpriteManager.load(EFFECT, "theorema");

	public static void init() {}
}
