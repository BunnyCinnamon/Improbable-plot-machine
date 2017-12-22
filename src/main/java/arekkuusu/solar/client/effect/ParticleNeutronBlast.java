/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.proxy.ClientProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 26/07/2017.
 * It's distributed as part of Solar.
 */
public class ParticleNeutronBlast extends ParticleBase {

	private final Vector3 point;
	private final double speed;
	private final boolean collide;
	private final int rgb;

	ParticleNeutronBlast(World world, Vector3 from, double speed, Vector3 to, int rgb, float scale, boolean collide) {
		super(world, from.x, from.y, from.z, 0, 0, 0);
		this.rgb = rgb;
		this.point = to;
		this.speed = speed;

		double distance = from.distanceTo(to);

		this.particleMaxAge = (int) (distance / speed);
		this.particleScale = scale;
		this.collide = collide;
		this.canCollide = !this.collide;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		//No-OP
	}

	@Override
	public void onUpdate() {
		if((particleAge++ >= particleMaxAge)) {
			setExpired();
		}

		double x = posX - point.x;
		double y = posY - point.y;
		double z = posZ - point.z;

		double square = x * x + y * y + z * z;
		double distance = Math.sqrt(square);

		motionX = speed * (x / distance);
		motionY = speed * (y / distance);
		motionZ = speed * (z / distance);

		prevPosX = posX += motionX;
		prevPosY = posY += motionY;
		prevPosZ = posZ += motionZ;
		move(motionX, motionY, motionZ);

		spawnNeutron(-motionX * 0.35, -motionY * 0.35, -motionZ * 0.35);
		spawnNeutron(0, 0, 0);
		spawnNeutron(motionX * 0.35, motionY * 0.35, motionZ * 0.35);

		if(collide) {
			BlockPos pos = new BlockPos(posX, posY, posZ);
			IBlockState state = world.getBlockState(pos);
			//noinspection deprecation
			AxisAlignedBB bounding = state.getBlock().getCollisionBoundingBox(state, world, pos);

			if(bounding != null && !world.getCollisionBoxes(null, this.getBoundingBox().shrink(0.1D)).isEmpty()) {
				setExpired();
			}
		}
	}

	private void spawnNeutron(double xOffset, double yOffset, double zOffset) {
		xOffset += posX;
		yOffset += posY;
		zOffset += posZ;

		ParticleNeutron particle = new ParticleNeutron(world, Vector3.create(xOffset, yOffset, zOffset),
				Vector3.ImmutableVector3.NULL, 60, particleScale, rgb);
		ClientProxy.PARTICLE_RENDERER.add(particle);
	}

	@Override
	public boolean shouldRender() {
		return false;
	}
}
