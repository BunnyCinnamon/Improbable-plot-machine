package arekkuusu.implom.common.handler.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class ImbuingData {
	public static final Map<World, Map<BlockPos, Progress>> PROGRESS = new WeakHashMap<>();

	public static void increment(World world, BlockPos pos, BlockPos source) {
		increment(world, pos, source, 1);
	}

	public static void increment(World world, BlockPos pos, BlockPos source, int progress) {
		PROGRESS.computeIfAbsent(world, (ignored) -> new HashMap<>(1));
		PROGRESS.get(world).compute(pos, (no, p) -> {
			if(p == null) p = new Progress();
			if(progress > 0) {
				p.from.add(source);
				p.lastUpdated = 0;
				p.timer += progress;
			}
			return p;
		});
	}

	public static void setProgress(World world, BlockPos pos, int progress) {
		PROGRESS.computeIfAbsent(world, (ignored) -> new HashMap<>(1));
		PROGRESS.get(world).compute(pos, (no, p) -> {
			if(p == null) p = new Progress();
			if(progress > 0) {
				p.from.add(pos);
				p.lastUpdated = 0;
				p.timer = progress;
			} else {
				p.from.remove(pos);
				if(p.from.isEmpty()) p = null;
			}
			return p;
		});
	}

	public static Progress getProgress(World world, BlockPos pos) {
		return Optional.ofNullable(PROGRESS.get(world)).map(map -> map.get(pos)).orElse(new Progress());
	}

	public static final class Progress {
		public Set<BlockPos> from = new HashSet<>();
		public int lastUpdated;
		public int timer;

		public int getMultiplier() {
			return from.size();
		}
	}
}
