/*
 * Arekkuusu / solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.client.util;

import arekkuusu.solar.common.lib.LibMod;
import com.google.common.collect.Sets;
import net.katsstuff.teamnightclipse.mirror.client.helper.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

/*
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
		getBlockTexture("neutron_battery/white");
		getBlockTexture("fission_inducer/center");
		getBlockTexture("differentiator/base_1");
		getBlockTexture("differentiator/base_2");
		getBlockTexture("differentiator/glass");
		getBlockTexture("differentiator_interceptor/glass");
		getBlockTexture("differentiator_interceptor/overlay");
		getBlockTexture("kondenzator/center");
		getBlockTexture("crystallic_synthesizer/base_overlay");
		getBlockTexture("crystallic_synthesizer/crystal");
	}
	public static final ResourceLocation TRANSPARENT = getBlockTexture("null");
	public static final ResourceLocation Q_SQUARED = getBlockTexture("q_squared");
	public static final ResourceLocation ELECTRON = getBlockTexture("electron");
	public static final ResourceLocation QIMRANUT_BASE = getBlockTexture("qimranut/base");
	public static final ResourceLocation VACUUM_CONVEYOR = getBlockTexture("vacuum_conveyor");
	public static final ResourceLocation MECHANICAL_TRANSLOCATOR = getBlockTexture("mechanical_translocator");
	public static final ResourceLocation HYPER_CONDUCTOR = getBlockTexture("hyper_conductor");
	public static final ResourceLocation QELAION_BASE = getBlockTexture("qelaion/base");
	public static final ResourceLocation QELAION_ON = getBlockTexture("qelaion/on");
	public static final ResourceLocation QELAION_OFF = getBlockTexture("qelaion/off");
	public static final ResourceLocation NEUTRON_BATTERY = getBlockTexture("neutron_battery/base");
	public static final ResourceLocation PHOLARIZER = getBlockTexture("pholarizer");
	public static final ResourceLocation FISSION_INDUCER = getBlockTexture("fission_inducer/base");
	public static final ResourceLocation DIFFERENTIATOR_BASE = getBlockTexture("differentiator/base_0");
	public static final ResourceLocation DIFFERENTIATOR_INTERCEPTOR_BASE = getBlockTexture("differentiator_interceptor/base");
	public static final ResourceLocation KONDENZATOR = getBlockTexture("kondenzator/base");
	public static final ResourceLocation CRYSTALLIC = getBlockTexture("crystallic_synthesizer/base");
	//Raw Textures
	public static final ResourceLocation GLOW_PARTICLE = getAtlas(TextureLocation.Effect(), "glow_particle");
	public static final ResourceLocation DULL_PARTICLE = getAtlas(TextureLocation.Effect(), "dull_particle");
	public static final ResourceLocation EYE_OF_SCHRODINGER = getTexture(TextureLocation.Model(), "eye_of_schrodinger");
	//Shader
	public static final ResourceLocation BLEND_SHADER = getShader(ShaderLocation.Program(), "blend");
	public static final ResourceLocation BRIGHT_SHADER = getShader(ShaderLocation.Program(), "bright");
	public static final ResourceLocation RECOLOR_SHADER = getShader(ShaderLocation.Program(), "recolor");

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
