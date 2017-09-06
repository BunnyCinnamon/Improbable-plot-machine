/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.client.proxy.ClientProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 26/07/2017.
 * It's distributed as part of Solar.
 */
public class ParticleNeutronBlast extends ParticleBase {

	private final Vec3d point;
	private final double speed;
	private final boolean collide;
	private final int rgb;

	ParticleNeutronBlast(World world, double xCoord, double yCoord, double zCoord, double speed, double xPoint, double yPoint, double zPoint, int rgb, float scale, boolean collide) {
		super(world, xCoord, yCoord, zCoord, 0, 0, 0);
		this.rgb = rgb;

		this.point = new Vec3d(xPoint, yPoint, zPoint);
		this.speed = speed;

		double x = posX - point.x;
		double y = posY - point.y;
		double z = posZ - point.z;

		double square = x * x + y * y + z * z;
		double distance = Math.sqrt(square);

		particleMaxAge = (int) ((distance / speed) * 0.5D);
		particleScale = scale;
		this.collide = collide;
		canCollide = !this.collide;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		//No-OP
	}

	@SuppressWarnings("deprecation")
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

		ParticleNeutron particle = new ParticleNeutron(world, xOffset, yOffset, zOffset, 0, 0, 0, 60, particleScale, rgb);
		ClientProxy.PARTICLE_RENDERER.add(particle);
	}

	@Override
	public boolean shouldRender() {
		return false;
	}
}
