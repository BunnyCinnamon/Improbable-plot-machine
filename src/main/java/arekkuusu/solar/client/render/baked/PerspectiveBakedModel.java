/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.Map;

/**
 * Created by <Arekkuusu> on 31/08/2017.
 * It's distributed as part of Solar.
 */
public abstract class PerspectiveBakedModel implements IBakedModel {

	private static final Map<ItemCameraTransforms.TransformType, TRSRTransformation> TRANSFORMS = ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
			.put(ItemCameraTransforms.TransformType.GUI, get(0F, 0F, 0F, 30F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(0F, 2.5F, 0F, 75F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(0F, 0F, 0F, 0F, 45F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(0F, 0F, 0F, 0F, 225F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.GROUND, get(0F, 3.5F, 0F, 0F, 0F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIXED, get(0F, 1F, 0F, 0F, 0F, 0F, 0.5F))
			.build();
	private static final TRSRTransformation DEFAULT_TRANSFORM = get(0, 0, 0, 0, 0, 0, 1);

	private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float size) {
		return new TRSRTransformation(
				new Vector3f(tx / 16, ty / 16, tz / 16),
				TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
				new Vector3f(size, size, size), null);
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		return Pair.of(this, TRANSFORMS.getOrDefault(cameraTransformType, DEFAULT_TRANSFORM).getMatrix());
	}
}
