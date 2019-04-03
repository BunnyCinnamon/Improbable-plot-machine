package arekkuusu.implom.api.helper;

public final class MathHelper {

	public static double getInterpolated(float tick, float max, float speed) {
		float angle = 0;
		double toDegrees = Math.PI / 180D;
		angle += speed * tick;
		if(angle > 360) angle -= 360;
		return max * Math.sin(angle * toDegrees);
	}
}
