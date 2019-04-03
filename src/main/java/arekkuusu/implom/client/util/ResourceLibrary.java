/*
 * Arekkuusu / solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util;

import arekkuusu.implom.common.lib.LibMod;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import net.katsstuff.teamnightclipse.mirror.client.helper.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/*
 * Created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public final class ResourceLibrary {

	//Atlas stitching textures
	public static final Set<ResourceLocation> ATLAS_SET = Sets.newHashSet();

	//Normie textures
	public static final ResourceLocation EMPTY = getBlockTexture("empty");
	public static final ResourceLocation QUANTA = getBlockTexture("quanta");
	public static final ResourceLocation ELECTRON = getBlockTexture("electron");
	public static final ResourceLocation QIMRANUT = getBlockTexture("qimranut/base");
	public static final ResourceLocation VACUUM_CONVEYOR = getBlockTexture("vacuum_conveyor");
	public static final ResourceLocation MECHANICAL_TRANSLOCATOR = getBlockTexture("mechanical_translocator");
	public static final ResourceLocation HYPER_CONDUCTOR = getBlockTexture("hyper_conductor");
	public static final ResourceLocation QELAION_BASE = getBlockTexture("qelaion/base");
	public static final ResourceLocation QELAION_ON = getBlockTexture("qelaion/on");
	public static final ResourceLocation QELAION_OFF = getBlockTexture("qelaion/off");
	public static final ResourceLocation NEUTRON_BATTERY = getBlockTexture("neutron_battery/base");
	public static final ResourceLocation PHOLARIZER = getBlockTexture("pholarizer");
	public static final ResourceLocation FISSION_INDUCER = getBlockTexture("fission_inducer/base");
	public static final ResourceLocation SYMMETRIC_SENDER = getBlockTexture("symmetric_negator/base");
	public static final ResourceLocation SYMMETRIC_RECEIVER = getBlockTexture("symmetric_extension/base");
	public static final ResourceLocation KONDENZATOR = getBlockTexture("kondenzator/base");
	public static final ResourceLocation MUTATOR = getBlockTexture("mutator/base");
	public static final ResourceLocation QUANTUM_MIRROR = getRawTexture(TextureLocation.Blocks(), "quantum_mirror");
	public static final ResourceLocation MUTATOR_SELECTION = getBlockTexture("mutator_selection");
	//Item Textures
	public static final ResourceLocation CLOCKWORK_INSIDES = getItemTexture("clockwork/insides");
	public static final ResourceLocation CLOCKWORK_SEALED = getItemTexture("clockwork/sealed");
	public static final ResourceLocation CLOCKWORK_UNSEALED = getItemTexture("clockwork/unsealed");
	//Raw Textures
	public static final ResourceLocation GLOW_PARTICLE = getTexture(TextureLocation.Effect(), "glow_particle");
	public static final ResourceLocation DULL_PARTICLE = getTexture(TextureLocation.Effect(), "dull_particle");
	public static final ResourceLocation SQUARE_PARTICLE = getTexture(TextureLocation.Effect(), "squared_particle");
	public static final ResourceLocation EYE_OF_SCHRODINGER = getRawTexture(TextureLocation.Model(), "eye_of_schrodinger");
	public static final ResourceLocation EYE_OF_SCHRODINGER_LAYER = getRawTexture(TextureLocation.Model(), "eye_of_schrodinger_layer");
	//Shader
	public static final ResourceLocation BLEND_SHADER = getShader(ShaderLocation.Program(), "blend");
	public static final ResourceLocation BRIGHT_SHADER = getShader(ShaderLocation.Program(), "bright");
	public static final ResourceLocation RECOLOR_SHADER = getShader(ShaderLocation.Program(), "recolor");
	//Model
	public static final ResourceLocation QELAION_FRAME = getModel(ModelLocation.Obj(), "qelaion");
	public static final ResourceLocation VACUUM_TOP = getModel(ModelLocation.Obj(), "vacuum/top");
	public static final ResourceLocation VACUUM_PLATE = getModel(ModelLocation.Obj(), "vacuum/plate");
	public static final ResourceLocation VACUUM_BOTTOM = getModel(ModelLocation.Obj(), "vacuum/bottom");
	public static final ResourceLocation TRANSLOCATOR_FRAME = getModel(ModelLocation.Obj(), "translocator/frame");
	public static final ResourceLocation TRANSLOCATOR_PLATE = getModel(ModelLocation.Obj(), "translocator/plate");
	public static final ResourceLocation TRANSLOCATOR_RING = getModel(ModelLocation.Obj(), "translocator/ring");
	public static final ResourceGroup CONDUCTOR_PIECE = ResourceGroup.of(
			getModel(ModelLocation.Obj(), "conductor/0"),
			getModel(ModelLocation.Obj(), "conductor/1"),
			getModel(ModelLocation.Obj(), "conductor/2"),
			getModel(ModelLocation.Obj(), "conductor/3"),
			getModel(ModelLocation.Obj(), "conductor/4")
	);
	public static final ResourceLocation QIMRANUT_FRAME = getModel(ModelLocation.Obj(), "qimranut/frame");
	public static final ResourceLocation QIMRANUT_OVERLAY = getModel(ModelLocation.Obj(), "qimranut/overlay");
	public static final ResourceLocation QIMRANUT_PLATE = getModel(ModelLocation.Obj(), "qimranut/plate");
	public static final ResourceLocation NEUTRON_BATTERY_FRAME = getModel(ModelLocation.Obj(), "neutron_battery/frame");
	public static final ResourceLocation NEUTRON_BATTERY_CRYSTAL = getModel(ModelLocation.Obj(), "neutron_battery/crystal");
	public static final ResourceLocation PHOLARIZER_FRAME = getModel(ModelLocation.Obj(), "pholarizer/frame");
	public static final ResourceLocation PHOLARIZER_CORE = getModel(ModelLocation.Obj(), "pholarizer/core");
	public static final ResourceLocation PHOLARIZER_CRYSTAL = getModel(ModelLocation.Obj(), "pholarizer/crystal");
	public static final ResourceLocation FISSION_INDUCER_TOP = getModel(ModelLocation.Obj(), "fission_inducer/top");
	public static final ResourceLocation FISSION_INDUCER_FRAME = getModel(ModelLocation.Obj(), "fission_inducer/frame");
	public static final ResourceLocation FISSION_INDUCER_CORE = getModel(ModelLocation.Obj(), "fission_inducer/core");
	public static final ResourceLocation FISSION_INDUCER_BOTTOM = getModel(ModelLocation.Obj(), "fission_inducer/bottom");
	public static final ResourceLocation ELECTRON_RING = getModel(ModelLocation.Obj(), "electron");
	public static final ResourceLocation SYMMETRIC_SENDER_FRAME = getModel(ModelLocation.Obj(), "symmetric_sender/frame");
	public static final ResourceLocation SYMMETRIC_SENDER_CORE = getModel(ModelLocation.Obj(), "symmetric_sender/core");
	public static final ResourceLocation SYMMETRIC_SENDER_RING = getModel(ModelLocation.Obj(), "symmetric_sender/ring");
	public static final ResourceLocation SYMMETRIC_RECEIVER_FRAME = getModel(ModelLocation.Obj(), "symmetric_receiver/frame");
	public static final ResourceLocation SYMMETRIC_RECEIVER_GEAR_TOP = getModel(ModelLocation.Obj(), "symmetric_receiver/gear_top");
	public static final ResourceLocation SYMMETRIC_RECEIVER_GEAR_CENTER = getModel(ModelLocation.Obj(), "symmetric_receiver/gear_center");
	public static final ResourceLocation SYMMETRIC_RECEIVER_GEAR_BOTTOM = getModel(ModelLocation.Obj(), "symmetric_receiver/gear_bottom");
	public static final ResourceLocation SYMMETRIC_RECEIVER_CRYSTAL = getModel(ModelLocation.Obj(), "symmetric_receiver/crystal");
	public static final ResourceLocation KONDENZATOR_FRAME = getModel(ModelLocation.Obj(), "kondenzator/frame");
	public static final ResourceLocation KONDENZATOR_CORE = getModel(ModelLocation.Obj(), "kondenzator/core");
	public static final ResourceLocation MUTATOR_FRAME = getModel(ModelLocation.Obj(), "mutator/frame");
	public static final ResourceLocation MUTATOR_FRAME_FRONT = getModel(ModelLocation.Obj(), "mutator/frame_front");
	public static final ResourceLocation MUTATOR_FRAME_OVERLAY = getModel(ModelLocation.Obj(), "mutator/frame_overlay");
	public static final ResourceLocation MUTATOR_PLATE = getModel(ModelLocation.Obj(), "mutator/plate");
	public static final ResourceLocation MUTATOR_BOLTS = getModel(ModelLocation.Obj(), "mutator/bolts");

	private static ResourceLocation getBlockTexture(String name) {
		return getTexture(TextureLocation.Blocks(), name);
	}

	private static ResourceLocation getItemTexture(String name) {
		return getTexture(TextureLocation.Items(), name);
	}

	private static ResourceLocation getTexture(Location location, String name) {
		return ResourceHelperStatic.getAtlas(LibMod.MOD_ID, location, name);
	}

	private static ResourceLocation getRawTexture(Location location, String name) {
		return ResourceHelperStatic.getTexture(LibMod.MOD_ID, location, name);
	}

	private static ResourceLocation getModel(Location location, String name) {
		return ResourceHelperStatic.getLocation(LibMod.MOD_ID, null, location, name, "");
	}

	private static ResourceLocation getShader(Location location, String name) {
		return ResourceHelperStatic.getLocation(LibMod.MOD_ID, AssetLocation.Shaders(), location, name, "");
	}

	public static final class ResourceGroup implements Iterable<ResourceLocation> {
		public final ResourceLocation[] locations;

		public ResourceGroup(ResourceLocation[] locations) {
			this.locations = locations;
		}

		public <R> R[] map(Function<ResourceLocation, ? extends R> function) {
			List<R> list = new ArrayList<>();
			for(int i = 0; i < locations.length; i++) {
				ResourceLocation location = locations[i];
				list.add(function.apply(location));
			}
			//noinspection unchecked
			return (R[]) list.toArray();
		}

		@Override
		@Nonnull
		public Iterator<ResourceLocation> iterator() {
			return Iterators.forArray(locations);
		}

		public static ResourceGroup of(ResourceLocation... dependencies) {
			return new ResourceGroup(dependencies);
		}
	}
}
