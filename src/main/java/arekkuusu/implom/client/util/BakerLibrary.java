package arekkuusu.implom.client.util;

import arekkuusu.implom.client.util.baker.BakerBlock;
import arekkuusu.implom.common.IPM;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Map;

public final class BakerLibrary {

	public static final Map<ResourceLocation, BakerBlock> BAKER_MAP = Maps.newHashMap();

	public static final BakerBlock[] HYPER_CONDUCTOR = Arrays.stream(ResourceLibrary.CONDUCTOR_PIECE.locations)
			.map(BakerLibrary::load).toArray(BakerBlock[]::new);
	public static final BakerBlock QELAION_FRAME = load(ResourceLibrary.QELAION_FRAME);
	public static final BakerBlock ELECTRON_RING = load(ResourceLibrary.ELECTRON_RING);
	public static final BakerBlock VACUUM_TOP = load(ResourceLibrary.VACUUM_TOP);
	public static final BakerBlock VACUUM_PLATE = load(ResourceLibrary.VACUUM_PLATE);
	public static final BakerBlock VACUUM_BOTTOM = load(ResourceLibrary.VACUUM_BOTTOM);
	public static final BakerBlock TRANSLOCATOR_FRAME = load(ResourceLibrary.TRANSLOCATOR_FRAME);
	public static final BakerBlock TRANSLOCATOR_PLATE = load(ResourceLibrary.TRANSLOCATOR_PLATE);
	public static final BakerBlock TRANSLOCATOR_RING = load(ResourceLibrary.TRANSLOCATOR_RING);
	public static final BakerBlock QIMRANUT_FRAME = load(ResourceLibrary.QIMRANUT_FRAME);
	public static final BakerBlock QIMRANUT_OVERLAY = load(ResourceLibrary.QIMRANUT_OVERLAY);
	public static final BakerBlock QIMRANUT_PLATE = load(ResourceLibrary.QIMRANUT_PLATE);
	public static final BakerBlock NEUTRON_BATTERY_FRAME = load(ResourceLibrary.NEUTRON_BATTERY_FRAME);
	public static final BakerBlock NEUTRON_BATTERY_CRYSTAL = load(ResourceLibrary.NEUTRON_BATTERY_CRYSTAL);
	public static final BakerBlock PHOLARIZER_FRAME = load(ResourceLibrary.PHOLARIZER_FRAME);
	public static final BakerBlock PHOLARIZER_CORE = load(ResourceLibrary.PHOLARIZER_CORE);
	public static final BakerBlock PHOLARIZER_CRYSTAL = load(ResourceLibrary.PHOLARIZER_CRYSTAL);
	public static final BakerBlock FISSION_INDUCER_TOP = load(ResourceLibrary.FISSION_INDUCER_TOP);
	public static final BakerBlock FISSION_INDUCER_FRAME = load(ResourceLibrary.FISSION_INDUCER_FRAME);
	public static final BakerBlock FISSION_INDUCER_CORE = load(ResourceLibrary.FISSION_INDUCER_CORE);
	public static final BakerBlock FISSION_INDUCER_BOTTOM = load(ResourceLibrary.FISSION_INDUCER_BOTTOM);
	public static final BakerBlock SYMMETRICAL_MACHINATION_FRAME = load(ResourceLibrary.SYMMETRICAL_MACHINATION_FRAME);
	public static final BakerBlock SYMMETRICAL_MACHINATION_CORE = load(ResourceLibrary.SYMMETRICAL_MACHINATION_CORE);
	public static final BakerBlock SYMMETRICAL_MACHINATION_RING = load(ResourceLibrary.SYMMETRICAL_MACHINATION_RING);
	public static final BakerBlock ASYMMETRICAL_MACHINATION_FRAME = load(ResourceLibrary.ASYMMETRICAL_MACHINATION_FRAME);
	public static final BakerBlock ASYMMETRICAL_MACHINATION_GEAR_TOP = load(ResourceLibrary.ASYMMETRICAL_MACHINATION_GEAR_TOP);
	public static final BakerBlock ASYMMETRICAL_MACHINATION_GEAR_CENTER = load(ResourceLibrary.ASYMMETRICAL_MACHINATION_GEAR_CENTER);
	public static final BakerBlock ASYMMETRICAL_MACHINATION_GEAR_BOTTOM = load(ResourceLibrary.ASYMMETRICAL_MACHINATION_GEAR_BOTTOM);
	public static final BakerBlock ASYMMETRICAL_MACHINATION_CRYSTAL = load(ResourceLibrary.ASYMMETRICAL_MACHINATION_CRYSTAL);
	public static final BakerBlock KONDENZATOR_FRAME = load(ResourceLibrary.KONDENZATOR_FRAME);
	public static final BakerBlock KONDENZATOR_CORE = load(ResourceLibrary.KONDENZATOR_CORE);
	public static final BakerBlock MUTATOR_FRAME = load(ResourceLibrary.MUTATOR_FRAME);
	public static final BakerBlock MUTATOR_FRAME_FRONT = load(ResourceLibrary.MUTATOR_FRAME_FRONT);
	public static final BakerBlock MUTATOR_FRAME_OVERLAY = load(ResourceLibrary.MUTATOR_FRAME_OVERLAY);
	public static final BakerBlock MUTATOR_PLATE = load(ResourceLibrary.MUTATOR_PLATE);
	public static final BakerBlock MUTATOR_BOLTS = load(ResourceLibrary.MUTATOR_BOLTS);

	public static BakerBlock load(ResourceLocation location) {
		BakerBlock block = new BakerBlock(location);
		BAKER_MAP.put(location, block);
		return block;
	}

	public static void stitchTextureModels() {
		BAKER_MAP.forEach((l, b) -> {
			b.loadModel();
			ResourceLibrary.ATLAS_SET.addAll(b.getTextures());
		});
		IPM.LOG.info("[GETTING PIE OUT OF THE OVEN!]");
	}

	public static void bakeModels() {
		BAKER_MAP.forEach((l, b) -> {
			b.bake();
		});
		IPM.LOG.info("[PIE HAS BEEN SUCCESSFULLY BAKED!]");
	}
}
