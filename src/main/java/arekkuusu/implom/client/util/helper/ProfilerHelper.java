/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;

/**
 * Created by <Arekkuusu> on 30/11/2017.
 * It's distributed as part of Improbable plot machine.
 */
public final class ProfilerHelper {

	public static final Profiler profiler = Minecraft.getMinecraft().mcProfiler;

	public static void flagSection(String section) {
		profiler.endStartSection(section);
	}

	public static void begin(String... sections) {
		for(String section : sections) {
			profiler.startSection(section);
		}
	}

	public static void interrupt(String section) {
		profiler.endSection();
		profiler.startSection(section);
	}

	public static void end() {
		profiler.endSection();
	}
}
