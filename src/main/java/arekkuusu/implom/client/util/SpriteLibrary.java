/*
 * Arekkuusu / solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util;

import arekkuusu.implom.client.util.resource.SpriteManager;
import arekkuusu.implom.client.util.resource.sprite.FrameSpriteResource;
import arekkuusu.implom.client.util.resource.sprite.SpriteResource;
import arekkuusu.implom.common.IPM;
import net.katsstuff.teamnightclipse.mirror.client.helper.TextureLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by <Arekkuusu> on 03/07/2017.
 * It's distributed as part of solar.
 */
@SideOnly(Side.CLIENT)
public final class SpriteLibrary {

	public static final FrameSpriteResource SQUARED_PARTICLE = SpriteManager.load(
			TextureLocation.Effect(), "squared_particle", 12, 1
	);
	public static final FrameSpriteResource QUANTUM_MIRROR = SpriteManager.load(
			TextureLocation.Blocks(), "quantum_mirror", 9, 1
	);
	public static final SpriteResource QUANTA = SpriteManager.load(
			TextureLocation.Blocks(), "quanta"
	);
	public static final SpriteResource EYE_OF_SCHRODINGER_LAYER = SpriteManager.load(
			TextureLocation.Model(), "eye_of_schrodinger_layer"
	);
	public static final FrameSpriteResource MUTATOR_SELECTION = SpriteManager.load(
			TextureLocation.Blocks(), "mutator_selection", 4, 1
	);

	public static void init() {
		IPM.LOG.info("[MAKING PIE!]");
	}
}
