package arekkuusu.implom.api.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BiomeTemperatureHelper {

	public static final float BASE_TEMPERATURE = 15; // Minecraft temperature to degrees celsius

	public static float getCelsiusTemperature(World world, BlockPos pos) {
		float temp = world.getBiomeForCoordsBody(pos).getTemperature(pos) * BASE_TEMPERATURE;
		float angle = (180F - (world.getCelestialAngle(1.0F) % 180F)) / 180F;
		float cycleTemp = world.isDaytime() ? 15 : -15;
		return temp + (cycleTemp * angle);
	}
}
