/*******************************************************************************
 * Arekkuusu / solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.api.state.MoonPhase;
import arekkuusu.solar.common.lib.LibMod;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.katsstuff.mirror.client.helper.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

/**
 * Created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public final class ResourceLibrary {

	//Block textures
	public static final Set<ResourceLocation> ATLAS_SET;
	static {
		ATLAS_SET = Sets.newHashSet();
		getBlockTexture("qimranut/overlay");
		getBlockTexture("qelaion/inside");
	}
	public static final ResourceLocation TRANSPARENT = getBlockTexture("null");
	public static final ResourceLocation[] MONOLITHIC_OVERLAY = ResourceHelperStatic.from(16, "monolithic/glyph_overlay_",
			ResourceLibrary::getBlockTexture
	);
	public static final ResourceLocation[] GRAVITY_HOPPER_OVERLAY = ResourceHelperStatic.from(3, "gravity_hopper/glyph_",
			ResourceLibrary::getBlockTexture
	);
	public static final ImmutableMap<MoonPhase, ResourceLocation> MOON_PHASES = ResourceHelperStatic.from(MoonPhase.class, "moon_phase/",
			ResourceLibrary::getBlockTexture
	);
	public static final ResourceLocation MONOLITHIC = getBlockTexture("monolithic/base");
	public static final ResourceLocation GRAVITY_HOPPER = getBlockTexture("gravity_hopper/side");
	public static final ResourceLocation PRIMAL_STONE = getBlockTexture("primal_stone");
	public static final ResourceLocation SCHRODINGER_GLYPH = getBlockTexture("schrodinger_glyph");
	public static final ResourceLocation BLINKER_BASE = getBlockTexture("blinker/base");
	public static final ResourceLocation BLINKER_TOP_ON = getBlockTexture("blinker/top_on");
	public static final ResourceLocation BLINKER_BOTTOM_ON = getBlockTexture("blinker/bottom_on");
	public static final ResourceLocation BLINKER_TOP_OFF = getBlockTexture("blinker/top_off");
	public static final ResourceLocation BLINKER_BOTTOM_OFF = getBlockTexture("blinker/bottom_off");
	public static final ResourceLocation Q_SQUARED = getBlockTexture("q_squared");
	public static final ResourceLocation ELECTRON_ON = getBlockTexture("electron/on");
	public static final ResourceLocation ELECTRON_OFF = getBlockTexture("electron/off");
	public static final ResourceLocation QIMRANUT_BASE = getBlockTexture("qimranut/base");
	public static final ResourceLocation VACUUM_CONVEYOR = getBlockTexture("vacuum_conveyor");
	public static final ResourceLocation MECHANICAL_TRANSLOCATOR = getBlockTexture("mechanical_translocator");
	public static final ResourceLocation ALTERNATOR_BASE = getBlockTexture("alternator/base");
	public static final ResourceLocation ALTERNATOR_ON = getBlockTexture("alternator/on");
	public static final ResourceLocation ALTERNATOR_OFF = getBlockTexture("alternator/off");
	public static final ResourceLocation HYPER_CONDUCTOR = getBlockTexture("hyper_conductor");
	public static final ResourceLocation QELAION_BASE = getBlockTexture("qelaion/base");
	public static final ResourceLocation QELAION_ON = getBlockTexture("qelaion/on");
	public static final ResourceLocation QELAION_OFF = getBlockTexture("qelaion/off");
	//Raw Textures
	public static final ResourceLocation GLOW_PARTICLE = getAtlas(TextureLocation.Effect(), "glow_particle");
	public static final ResourceLocation DULL_PARTICLE = getAtlas(TextureLocation.Effect(), "dull_particle");
	public static final ResourceLocation THEOREMA = getTexture(TextureLocation.Blocks(), "theorema");
	public static final ResourceLocation EYE_OF_SCHRODINGER = getTexture(TextureLocation.Model(), "eye_of_schrodinger");
	//Shader
	public static final ResourceLocation BLEND_SHADER = getShader(ShaderLocation.Program(), "blend");

	private static ResourceLocation getBlockTexture(String name) {
		ResourceLocation location = ResourceHelperStatic.getAtlas(LibMod.MOD_ID, TextureLocation.Blocks(), name);
		ATLAS_SET.add(location);
		return location;
	}

	private static ResourceLocation getTexture(Location location, String name) {
		return ResourceHelperStatic.getTexture(LibMod.MOD_ID, location, name);
	}

	private static ResourceLocation getAtlas(Location location, String name) {
		ResourceLocation rl = ResourceHelperStatic.getAtlas(LibMod.MOD_ID, location, name);
		ATLAS_SET.add(rl);
		return rl;
	}

	private static ResourceLocation getShader(Location location, String name) {
		return ResourceHelperStatic.getLocation(LibMod.MOD_ID, AssetLocation.Shaders(), location, name, "");
	}
}
