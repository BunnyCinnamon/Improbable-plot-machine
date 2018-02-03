/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.api.state.MoonPhase;
import arekkuusu.solar.client.util.resource.sprite.Location;
import arekkuusu.solar.common.lib.LibMod;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;
import java.util.function.Function;

import static arekkuusu.solar.client.util.ResourceLibrary.TextureLocation.BLOCKS;
import static arekkuusu.solar.client.util.ResourceLibrary.TextureLocation.MODEL;

/**
 * Created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public final class ResourceLibrary {

	public static final Set<ResourceLocation> ATLAS_SET = Sets.newHashSet();

	//Atlas
	public static final ResourceLocation MONOLITHIC = getAtlas(BLOCKS, "monolithic/base");
	public static final ResourceLocation[] MONOLITHIC_OVERLAY = from(16, "monolithic/glyph_overlay_", name ->
			getAtlas(BLOCKS, name)
	);
	public static final ResourceLocation GRAVITY_HOPPER = getAtlas(BLOCKS, "gravity_hopper/side");
	public static final ResourceLocation[] GRAVITY_HOPPER_OVERLAY = from(3, "gravity_hopper/glyph_", name ->
			getAtlas(BLOCKS, name)
	);
	public static final ResourceLocation PRIMAL_STONE = getAtlas(BLOCKS, "primal_stone");
	public static final ResourceLocation SCHRODINGER_GLYPH = getAtlas(BLOCKS, "schrodinger_glyph");
	public static final ResourceLocation BLINKER_BASE = getAtlas(BLOCKS, "blinker/blinker_base");
	public static final ResourceLocation BLINKER_TOP_ON = getAtlas(BLOCKS, "blinker/blinker_top_on");
	public static final ResourceLocation BLINKER_BOTTOM_ON = getAtlas(BLOCKS, "blinker/blinker_bottom_on");
	public static final ResourceLocation BLINKER_TOP_OFF = getAtlas(BLOCKS, "blinker/blinker_top_off");
	public static final ResourceLocation BLINKER_BOTTOM_OFF = getAtlas(BLOCKS, "blinker/blinker_bottom_off");
	public static final ResourceLocation Q_SQUARED = getAtlas(BLOCKS, "q_squared");
	public static final ResourceLocation ELECTRON_ON = getAtlas(BLOCKS, "electron/on");
	public static final ResourceLocation ELECTRON_OFF = getAtlas(BLOCKS, "electron/off");
	public static final ResourceLocation QIMRANUT_BASE = getAtlas(BLOCKS, "qimranut/base");
	public static final ResourceLocation QIMRANUT_OVERLAY_FRONT = getAtlas(BLOCKS, "qimranut/overlay_front");
	public static final ResourceLocation QIMRANUT_OVERLAY_BACK = getAtlas(BLOCKS, "qimranut/overlay_back");
	public static final ImmutableMap<MoonPhase, ResourceLocation> MOON_PHASES = from(MoonPhase.class, "moon_phase/", name ->
			getAtlas(BLOCKS, name)
	);
	public static final ResourceLocation VACUUM_CONVEYOR = getAtlas(BLOCKS, "vacuum_conveyor");
	public static final ResourceLocation MECHANICAL_TRANSLOCATOR = getAtlas(BLOCKS, "mechanical_translocator");
	public static final ResourceLocation ALTERNATOR_BASE = getAtlas(BLOCKS, "alternator/base");
	public static final ResourceLocation ALTERNATOR_OVERLAY_ON = getAtlas(BLOCKS, "alternator/overlay_on");
	public static final ResourceLocation ALTERNATOR_OVERLAY_OFF = getAtlas(BLOCKS, "alternator/overlay_off");
	public static final ResourceLocation HYPER_CONDUCTOR = getAtlas(BLOCKS, "hyper_conductor");
	//Textures
	public static final ResourceLocation THEOREMA = getTexture(BLOCKS, "theorema");
	public static final ResourceLocation EYE_OF_SCHRODINGER = getTexture(MODEL, "eye_of_schrodinger");

	public static ResourceLocation getLocation(AssetLocation asset, Location location, String name, String suffix) {
		StringBuilder builder = new StringBuilder();
		if(asset != null) builder.append(asset.getPath());
		if(location != null) builder.append(location.getPath());
		builder.append(name).append(suffix);
		return new ResourceLocation(LibMod.MOD_ID, builder.toString());
	}

	public static ModelResourceLocation getModel(String name, String variant) {
		ResourceLocation atlas = getLocation(null, null, name, "");
		return new ModelResourceLocation(atlas, variant);
	}

	public static ResourceLocation getAtlas(Location location, String name) {
		ResourceLocation rl = getLocation(null, location, name, "");
		ATLAS_SET.add(rl);
		return rl;
	}

	public static ResourceLocation getTexture(Location location, String name) {
		return getLocation(AssetLocation.TEXTURES, location, name, ".png");
	}

	public static ResourceLocation getSimpleLocation(String name) {
		return getLocation(null, null, name, "");
	}

	public static ResourceLocation[] from(int amount, String name, Function<String, ResourceLocation> function) {
		ResourceLocation[] locations = new ResourceLocation[amount];
		for(int i = 0; i < amount; i++) {
			locations[i] = function.apply(name + i);
		}
		return locations;
	}

	public static <T extends Enum<T> & IStringSerializable> ImmutableMap<T, ResourceLocation> from(Class<T> clazz, String name, Function<String, ResourceLocation> function) {
		ImmutableMap.Builder<T, ResourceLocation> builder = ImmutableMap.builder();
		T[] enums = clazz.getEnumConstants();
		for(T enu : enums) {
			builder.put(enu, function.apply(name + enu.getName()));
		}
		return builder.build();
	}

	public enum ModelLocation implements Location {
		BLOCK("block"),
		ITEM("item"),
		OTHER("other"),
		OBJ("obj");

		private final String path;

		ModelLocation(String path) {
			this.path = path;
		}

		@Override
		public String getPath() {
			return path + "/";
		}
	}

	public enum TextureLocation implements Location {
		BLOCKS("blocks"),
		ITEMS("items"),
		EFFECT("effect"),
		GUI("gui"),
		MODEL("model");

		private final String path;

		TextureLocation(String path) {
			this.path = path;
		}

		@Override
		public String getPath() {
			return path + "/";
		}
	}

	public enum ShaderLocation implements Location {
		POST("post"),
		PROGRAM("program");

		private final String path;

		ShaderLocation(String path) {
			this.path = path;
		}

		@Override
		public String getPath() {
			return path + "/";
		}
	}

	public enum AssetLocation {
		MODELS("models"),
		TEXTURES("textures"),
		SHADERS("shaders");

		private final String path;

		AssetLocation(String path) {
			this.path = path;
		}

		public String getPath() {
			return path + "/";
		}
	}
}
