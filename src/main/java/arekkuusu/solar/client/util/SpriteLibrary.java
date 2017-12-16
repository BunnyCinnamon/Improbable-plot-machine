/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.client.util.resource.FrameSpriteResource;
import arekkuusu.solar.client.util.resource.SpriteLoader;
import arekkuusu.solar.client.util.resource.SpriteResource;
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
	public static final FrameSpriteResource QUORN_PARTICLE = SpriteLoader.load(EFFECT, "quorn_particle", 7, 1);
	public static final SpriteResource NEUTRON_PARTICLE = SpriteLoader.load(EFFECT, "neutron_particle");
	public static final SpriteResource LIGHT_PARTICLE = SpriteLoader.load(EFFECT, "light_particle");
	public static final SpriteResource CHARGED_ICE = SpriteLoader.load(EFFECT, "charged_ice");
	//Other
	public static final FrameSpriteResource QUANTUM_MIRROR = SpriteLoader.load(BLOCKS, "quantum_mirror", 9, 1);
	public static final SpriteResource PRISM_PETAL = SpriteLoader.load(BLOCKS, "prism_flower/petal");
	public static final SpriteResource EYE_OF_SCHRODINGER_LAYER = SpriteLoader.load(MODEL, "eye_of_schrodinger_layer");
	public static final FrameSpriteResource QUINGENTILLIARD = SpriteLoader.load(ITEMS, "quingentilliard", 8, 1);
	public static final SpriteResource Q_SQUARED = SpriteLoader.load(BLOCKS, "q_squared");
	public static final FrameSpriteResource GRAVITY_INHIBITOR_OVERLAY = SpriteLoader.load(BLOCKS, "gravity_inhibitor_overlay", 4, 2);
	public static final SpriteResource GRAVITY_INHIBITOR = SpriteLoader.load(BLOCKS, "gravity_inhibitor");

	public static void init() {}
}
