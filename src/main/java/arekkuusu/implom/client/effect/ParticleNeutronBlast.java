/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * Created by <Arekkuusu> on 26/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class ParticleNeutronBlast extends ParticleBase {

	private final boolean collide;
	private final int rgb;

	ParticleNeutronBlast(World world, Vector3 pos, Vector3 speed, float scale, int age, int rgb, boolean collide) {
		super(world, pos, speed, scale, age, rgb);
		this.rgb = rgb;
		this.collide = collide;
		this.canCollide = !this.collide;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		//NO-OP
	}

	@Override
	public void onUpdateGlow() {
		this.onUpdate();
	}

	@Override
	public void onUpdate() {
		if(particleAge++ >= particleMaxAge) {
			setExpired();
		}
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.move(this.motionX, this.motionY, this.motionZ);

		spawnTrail(-motionX * 0.35, -motionY * 0.35, -motionZ * 0.35);
		spawnTrail(0, 0, 0);
		spawnTrail(motionX * 0.35, motionY * 0.35, motionZ * 0.35);

		if(collide) {
			BlockPos pos = new BlockPos(posX, posY, posZ);
			IBlockState state = world.getBlockState(pos);
			//noinspection deprecation
			AxisAlignedBB bounding = state.getCollisionBoundingBox(world, pos);
			if(bounding != null && !world.getCollisionBoxes(null, this.getBoundingBox().shrink(0.1D)).isEmpty()) {
				Vector3 vecPos = new Vector3(prevPosX, prevPosY, prevPosZ);
				for(int i = 0; i < 4 + rand.nextInt(5); i++) {
					double speed = world.rand.nextDouble() * 0.015D;
					Vector3 speedVec = Vector3.rotateRandom().multiply(speed);
					FXUtil.spawnSpeck(world, vecPos, speedVec, 75, 1.75F, rgb, GlowTexture.GLINT);
				}
				setExpired();
			}
		}
	}

	private void spawnTrail(double xOffset, double yOffset, double zOffset) {
		xOffset += posX;
		yOffset += posY;
		zOffset += posZ;
		FXUtil.spawnSpeck(world, Vector3.apply(xOffset, yOffset, zOffset), Vector3.Zero(),
				60, particleScale, rgb, GlowTexture.GLOW);
	}

	@Override
	public boolean shouldRender() {
		return false;
	}
}
