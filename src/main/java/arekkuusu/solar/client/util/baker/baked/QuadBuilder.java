/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.api.util.Vector3;
import arekkuusu.solar.client.proxy.ClientProxy;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector4f;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by <Arekkuusu> on 13/12/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class QuadBuilder {

	private final Map<EnumFacing, QuadHolder> facingMap = Maps.newEnumMap(EnumFacing.class);
	private final VertexFormat format;
	private boolean hasBrightness;
	private Vector3 from;
	private Vector3 to;
	private EnumFacing last;

	private QuadBuilder(VertexFormat format) {
		this.format = format;
	}

	public static QuadBuilder withFormat(VertexFormat format) {
		return new QuadBuilder(format);
	}

	public QuadBuilder setFrom(double x, double y, double z) {
		this.from = Vector3.create(x, y, z);
		return this;
	}

	public QuadBuilder setTo(double x, double y, double z) {
		this.to = Vector3.create(x, y, z);
		return this;
	}

	public QuadBuilder setHasBrightness(boolean hasBrightness) {
		this.hasBrightness = hasBrightness;
		return this;
	}

	public QuadBuilder addAll(TextureAtlasSprite sprite) {
		for(EnumFacing facing : EnumFacing.values()) {
			addFace(facing, 0F, 16F, 0F, 16F, sprite);
		}
		return this;
	}

	public QuadBuilder addAll(float uMin, float uMax, float vMin, float vMax, TextureAtlasSprite sprite) {
		for(EnumFacing facing : EnumFacing.values()) {
			addFace(facing, uMin, uMax, vMin, vMax, sprite);
		}
		return this;
	}

	public QuadBuilder addFace(EnumFacing facing, float uMin, float uMax, float vMin, float vMax, TextureAtlasSprite sprite) {
		QuadHolder holder = new QuadHolder();
		holder.uv = new Vector4f(uMin, uMax, vMin, vMax);
		Vector3 a = Vector3.create(0, 0, 0);
		Vector3 b = Vector3.create(0, 0, 0);
		Vector3 c = Vector3.create(0, 0, 0);
		Vector3 d = Vector3.create(0, 0, 0);
		holder.sprite = sprite;
		switch(facing) {
			case DOWN:
				a.x = to.x;
				a.y = from.y;
				a.z = from.z;

				b.x = to.x;
				b.y = from.y;
				b.z = to.z;

				c.x = from.x;
				c.y = from.y;
				c.z = to.z;

				d.x = from.x;
				d.y = from.y;
				d.z = from.z;
				break;
			case UP:
				a.x = from.x;
				a.y = to.y;
				a.z = from.z;

				b.x = from.x;
				b.y = to.y;
				b.z = to.z;

				c.x = to.x;
				c.y = to.y;
				c.z = to.z;

				d.x = to.x;
				d.y = to.y;
				d.z = from.z;
				break;
			case NORTH:
				a.x = to.x;
				a.y = from.y;
				a.z = to.z;

				b.x = to.x;
				b.y = to.y;
				b.z = to.z;

				c.x = from.x;
				c.y = to.y;
				c.z = to.z;

				d.x = from.x;
				d.y = from.y;
				d.z = to.z;
				break;
			case SOUTH:
				a.x = from.x;
				a.y = from.y;
				a.z = from.z;

				b.x = from.x;
				b.y = to.y;
				b.z = from.z;

				c.x = to.x;
				c.y = to.y;
				c.z = from.z;

				d.x = to.x;
				d.y = from.y;
				d.z = from.z;
				break;
			case WEST:
				a.x = from.x;
				a.y = from.y;
				a.z = to.z;

				b.x = from.x;
				b.y = to.y;
				b.z = to.z;

				c.x = from.x;
				c.y = to.y;
				c.z = from.z;

				d.x = from.x;
				d.y = from.y;
				d.z = from.z;
				break;
			case EAST:
				a.x = to.x;
				a.y = from.y;
				a.z = from.z;

				b.x = to.x;
				b.y = to.y;
				b.z = from.z;

				c.x = to.x;
				c.y = to.y;
				c.z = to.z;

				d.x = to.x;
				d.y = from.y;
				d.z = to.z;
				break;
		}
		holder.a = a.divide(16D);
		holder.b = b.divide(16D);
		holder.c = c.divide(16D);
		holder.d = d.divide(16D);
		facingMap.put(facing, holder);
		last = facing;
		return this;
	}

	public QuadBuilder mirror() {
		if(facingMap.containsKey(last)) {
			Stream<Vector3> stream = Arrays.stream(facingMap.get(last).getVectors());
			switch(last) {
				case DOWN:
				case UP:
					stream.forEach(vec -> {
						vec.subtract(0.5D).rotate(EnumFacing.Axis.Y, 180).add(0.5D);
					});
					break;
				case NORTH:
				case SOUTH:
					stream.forEach(vec -> {
						vec.subtract(0.5D).rotate(EnumFacing.Axis.X, 180).add(0.5D);
					});
					break;
				case EAST:
				case WEST:
					stream.forEach(vec -> {
						vec.subtract(0.5D).rotate(EnumFacing.Axis.Z, 180).add(0.5D);
					});
					break;
			}
		}
		return this;
	}

	public QuadBuilder clear() {
		facingMap.clear();
		return this;
	}

	public QuadBuilder rotate(EnumFacing facing, EnumFacing... exclude) {
		//noinspection ConstantConditions
		if(exclude == null || !Arrays.asList(exclude).contains(facing)) {
			switch(facing) {
				case DOWN:
					rotate(EnumFacing.Axis.X, 180F);
					break;
				case UP:
					rotate(EnumFacing.Axis.X, 180F);
					break;
				default:
					rotate(EnumFacing.Axis.X, 90F);
					rotate(EnumFacing.Axis.Y, -facing.getHorizontalAngle());
					rotate(EnumFacing.Axis.Y, -90F);
			}
		}
		return this;
	}

	public QuadBuilder rotate(EnumFacing.Axis axis, float angle) {
		facingMap.values().forEach(holder -> {
			Arrays.stream(holder.getVectors()).forEach(vec -> {
				vec.subtract(0.5D).rotate(axis, angle).add(0.5D);
			});
		});
		return this;
	}

	public List<BakedQuad> bake() {
		return facingMap.entrySet().stream().map(e -> createQuad(e.getValue(), e.getKey())).collect(Collectors.toList());
	}

	private BakedQuad createQuad(QuadHolder holder, EnumFacing facing) {
		Vector4f uv = holder.uv;
		Vector3 a = holder.a;
		Vector3 b = holder.b;
		Vector3 c = holder.c;
		Vector3 d = holder.d;

		Vector3 normal = c.copy().subtract(b).cross(a.copy().subtract(b)).normalize();
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		putVertex(builder, normal, a.x, a.y, a.z, holder.sprite, uv.y, uv.w, hasBrightness);
		putVertex(builder, normal, b.x, b.y, b.z, holder.sprite, uv.y, uv.z, hasBrightness);
		putVertex(builder, normal, c.x, c.y, c.z, holder.sprite, uv.x, uv.z, hasBrightness);
		putVertex(builder, normal, d.x, d.y, d.z, holder.sprite, uv.x, uv.w, hasBrightness);
		builder.setQuadOrientation(facing);
		builder.setTexture(holder.sprite);
		return builder.build();
	}

	private void putVertex(UnpackedBakedQuad.Builder builder, Vector3 normal, double x, double y, double z, TextureAtlasSprite sprite, float u, float v, boolean hasBrightness) {
		for(int e = 0; e < format.getElementCount(); e++) {
			switch(format.getElement(e).getUsage()) {
				case POSITION:
					builder.put(e, (float) x, (float) y, (float) z, 1F);
					break;
				case COLOR:
					builder.put(e, 1F, 1F, 1F, 1F);
					break;
				case UV:
					if(!ClientProxy.isOptifineInstalled() && format.getElement(e).getIndex() == 1) {
						if(hasBrightness) {
							builder.put(e, 1F, 1F);
						} else {
							builder.put(e, 0F, 0F);
						}
						break;
					} else if(format.getElement(e).getIndex() == 0) {
						u = sprite.getInterpolatedU(u);
						v = sprite.getInterpolatedV(v);
						builder.put(e, u, v, 0F, 1F);
						break;
					}
				case NORMAL:
					if(!ClientProxy.isOptifineInstalled() && hasBrightness) {
						builder.put(e, 0F, 1F, 0F);
					} else {
						builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z);
					}
					break;
				default:
					builder.put(e);
					break;
			}
		}
	}

	private class QuadHolder {
		TextureAtlasSprite sprite;
		Vector3 a;
		Vector3 b;
		Vector3 c;
		Vector3 d;
		Vector4f uv;

		Vector3[] getVectors() {
			return new Vector3[]{a, b, c, d};
		}
	}
}
