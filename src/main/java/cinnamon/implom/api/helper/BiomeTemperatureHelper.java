package cinnamon.implom.api.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class BiomeTemperatureHelper {

	public static final float BASE_TEMPERATURE = 15; // Minecraft temperature to degrees celsius

	public static float getCelsiusTemperature(Level world, BlockPos pos) {
		float temp = world.getBiome(pos).value().getTemperature(pos) * BASE_TEMPERATURE;
		float angle = (180F - (world.getSunAngle(1.0F) % 180F)) / 180F;
		float cycleTemp = world.isDay() ? 15 : -15;
		return temp + (cycleTemp * angle);
	}
}
