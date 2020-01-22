/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.helper.ProfilerHelper;
import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.mirror.data.MutableVector3;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/*
 * Created by <Arekkuusu> on 28/10/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public class ParticleArcDischarge extends ParticleBase {

	private List<VoltSegment> segments = Lists.newArrayList();
	private final int generations;
	private final boolean branch;
	private float offset;

	ParticleArcDischarge(World world, Vector3 from, Vector3 to, int generations, float offset, int age, int rgb, boolean branch) {
		super(world, from.add(to).divide(2D), Vector3.Zero(), 0, age, rgb, Light.GLOW, ResourceLibrary.EMPTY);
		this.segments.add(new VoltSegment(from, to));
		this.generations = generations;
		this.branch = branch;
		this.offset = offset;
		setParticleTexture(ResourceLibrary.VOLT_PARTICLE);
		setRBGColorF(166F / 255F,  166F / 255F, 205F / 255F);
		calculateBolts();
	}

	private void calculateBolts() { //TODO: Maybe use textures instead of raw gl lines?
		ProfilerHelper.begin("[Particle Bolt] Calculating Bolts");
		List<VoltSegment> branched = Lists.newArrayList();
		for(int i = 0; i < generations; i++) {
			List<VoltSegment> temp = Lists.newArrayList();
			for(VoltSegment segment : segments) {
				Vector3 from = segment.from;
				Vector3 to = segment.to;
				MutableVector3 mid = average(from, to).asMutable();
				Vector3 midOffset = to.subtract(from);
				mid = mid.add(midOffset.normalize()
						.cross(Vector3.One())
						.multiply(Vector3.rotateRandom().multiply(offset))
				);
				if(branch && rand.nextDouble() > 0.6D) {
					MutableVector3 direction = mid.subtract(from);
					float xAngle = (25.0F + 12.5F * rand.nextFloat()) * (rand.nextBoolean() ? 1 : -1);
					float zAngle = (25.0F + 12.5F * rand.nextFloat()) * (rand.nextBoolean() ? 1 : -1);
					Quat x = Quat.fromAxisAngle(Vector3.Forward(), xAngle);
					Quat z = Quat.fromAxisAngle(Vector3.Right(), zAngle);
					Vector3 splitEnd = direction
							.rotate(x.multiply(z))
							.multiply(0.7D)
							.add(mid)
							.asImmutable();
					VoltSegment sub = new VoltSegment(mid.asImmutable(), splitEnd);
					temp.add(sub);
				}
				VoltSegment one = new VoltSegment(from, mid.asImmutable());
				VoltSegment two = new VoltSegment(mid.asImmutable(), to);
				temp.add(one);
				temp.add(two);
				if(branched.isEmpty() || branched.contains(segment)) {
					branched.add(two);
				}
			}
			segments = temp;
			offset /= 2;
		}
		ProfilerHelper.end();
	}

	private Vector3 average(Vector3 one, Vector3 two) {
		return one.add(two).divide(2D);
	}

	@Override
	public void renderParticleGlow(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		Tessellator.getInstance().draw();

		GlStateManager.pushMatrix();
		Entity rView = Minecraft.getMinecraft().getRenderViewEntity();
		if(rView == null) rView = Minecraft.getMinecraft().player;
		Entity entity = rView;
		double tx = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
		double ty = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
		double tz = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);
		GlStateManager.translate(-tx, -ty, -tz);

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

		for (VoltSegment s : segments) {
			s.render(buffer, partialTicks);
		}

		tes.draw();
		GlStateManager.popMatrix();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
	}

	@Override
	public void onUpdateGlow() {
		this.onUpdate();
		segments.forEach(r -> r.alpha *= 0.9F);
		float life = (float) particleAge / (float) particleMaxAge;
		this.particleScale = initScale - initScale * life;
		this.particleAlpha = 1.0f - life;
		if(this.particleAngle != 0.0F) {
			this.prevParticleAngle = particleAngle;
			this.particleAngle += 1.0F;
		}
	}

	@Override
	public boolean isAdditive() {
		return true;
	}

	private class VoltSegment {

		private final Vector3 from;
		private final Vector3 to;
		private float alpha = 1F;

		VoltSegment(Vector3 from, Vector3 to) {
			this.from = from;
			this.to = to;
		}

		void render(BufferBuilder buff, float partialTicks) {
			renderCurrentTextureAroundAxis(buff, from, to, 0F, partialTicks);
			renderCurrentTextureAroundAxis(buff, from, to, 90F, partialTicks);
		}

		private void renderCurrentTextureAroundAxis(BufferBuilder buf, Vector3 from, Vector3 to, double angle, float partialTicks) {
			Vector3 distance = to.subtract(from);
			from = from.offset(distance, -0.1);
			to = to.offset(distance, 0.1);
			Vector3 aim = to.subtract(from).normalize();
			Vector3 aimPerp = perpendicular(aim).normalize();
			Vector3 perp = aimPerp.rotate(Quat.fromAxisAngle(aim, angle)).normalize();
			Vector3 perpFrom = perp.multiply(0.035);
			Vector3 perpTo = perp.multiply(0.035);

			double uMin = 0F;
			double uMax = 1F;
			double vMin = 0F;
			double vMax = 1F;
			int light = getBrightnessForRender(partialTicks);
			if(particleTexture != null) {
				uMin = particleTexture.getMinU();
				uMax = particleTexture.getMaxU();
				vMin = particleTexture.getMinV();
				vMax = particleTexture.getMaxV();
			}

			Vector3 vec = from.add(perpFrom.multiply(-1));
			buf.pos(vec.getX(), vec.getY(), vec.getZ()).tex(uMax, vMax).color(particleRed, particleGreen, particleBlue, 1).lightmap(light, light).endVertex();
			vec = from.add(perpFrom);
			buf.pos(vec.getX(), vec.getY(), vec.getZ()).tex(uMax, vMin).color(particleRed, particleGreen, particleBlue, 1).lightmap(light, light).endVertex();
			vec = to.add(perpTo);
			buf.pos(vec.getX(), vec.getY(), vec.getZ()).tex(uMin, vMin).color(particleRed, particleGreen, particleBlue, 1).lightmap(light, light).endVertex();
			vec = to.add(perpTo.multiply(-1));
			buf.pos(vec.getX(), vec.getY(), vec.getZ()).tex(uMin, vMax).color(particleRed, particleGreen, particleBlue, 1).lightmap(light, light).endVertex();
		}

		public Vector3 perpendicular(Vector3 vec) {
			if (vec.z() == 0.0D) {
				return zCrossProduct(vec);
			}
			return xCrossProduct(vec);
		}

		public Vector3 xCrossProduct(Vector3 vec) {
			double x = 0.0D;
			double y = vec.z();
			double z = -vec.y();
			return vec.create(x, y, z);
		}

		public Vector3 zCrossProduct(Vector3 vec) {
			double x = vec.y();
			double y = -vec.x();
			double z = 0.0D;
			return vec.create(x, y, z);
		}
	}
}
