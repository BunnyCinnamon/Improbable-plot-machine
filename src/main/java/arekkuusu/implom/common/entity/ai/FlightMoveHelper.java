/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

/**
 * Created by <Arekkuusu> on 06/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class FlightMoveHelper extends EntityMoveHelper {

	public FlightMoveHelper(EntityLiving living) {
		super(living);
	}

	public void onUpdateMoveHelper() {
		if(action == EntityMoveHelper.Action.MOVE_TO && !entity.getNavigator().noPath()) {
			double d0 = posX - entity.posX;
			double d1 = posY - entity.posY;
			double d2 = posZ - entity.posZ;
			double speed = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

			speed = speed * entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue();
			float flyingSpeed = (float) (this.entity.getAIMoveSpeed() + (speed - this.entity.getAIMoveSpeed()) * 0.125F);

			if(flyingSpeed > 1F) {
				flyingSpeed = 1F;
			}

			entity.setAIMoveSpeed(flyingSpeed);
			entity.setMoveVertical(d1 > 0.0D ? flyingSpeed : -flyingSpeed);

			float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
			this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 180F);
			this.entity.renderYawOffset = this.entity.rotationYaw;

			double d4 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
			float f2 = (float)(-(MathHelper.atan2(d1, d4) * (180D / Math.PI)));
			this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, f2, 180F);
		} else {
			entity.setAIMoveSpeed(0F);
			entity.setMoveForward(0F);
			entity.setMoveVertical(0F);
			entity.motionX *= 0.75;
			entity.motionY *= 0.75;
			entity.motionZ *= 0.75;
		}
	}
}
