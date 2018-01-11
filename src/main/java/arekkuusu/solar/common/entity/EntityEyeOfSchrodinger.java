/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.entity.ai.FlightMoveHelper;
import arekkuusu.solar.common.entity.ai.FlightPathNavigate;
import arekkuusu.solar.common.handler.gen.ModGen;
import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;

/**
 * Created by <Arekkuusu> on 04/08/2017.
 * It's distributed as part of Solar.
 */
public class EntityEyeOfSchrodinger extends EntityMob {

	private static final DataParameter<Boolean> HAS_TARGET = EntityDataManager.createKey(EntityEyeOfSchrodinger.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntityEyeOfSchrodinger.class, DataSerializers.VARINT);
	public static final int BLUE = 0x1EF2FF;
	public static final int RED = 0xFF1000;

	public EntityEyeOfSchrodinger(World worldIn) {
		super(worldIn);
		this.moveHelper = new FlightMoveHelper(this);
		this.setSize(0.5F,0.5F);
		this.experienceValue = 10;
		this.isImmuneToFire = true;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(HAS_TARGET, false);
		this.dataManager.register(TARGET, -1);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.onGround = false;
		this.isJumping = false;
		this.onGroundSpeedFactor = 0;
		this.prevOnGroundSpeedFactor = 0;
		//Spawn Particles
		if(world.isRemote) {
			int rgb = hasTargetedEntity() ? RED : BLUE;
			ParticleUtil.spawnTunnelingPhoton(world
					, Vector3.create(posX, posY + 0.25D, posZ)
					, Vector3.ImmutableVector3.NULL, rgb, 10, 1.5F);
			Entity entity = getTargetedEntity();
			if(entity != null) {
				Vector3 speed = Vector3.create(posX, posY + 0.25D, posZ)
						.subtract(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ)
						.multiply(-0.1D)
						.limit(0.25D);
				ParticleUtil.spawnSquared(world, Vector3.create(posX, posY + 0.25D, posZ), speed, RED, 10, 4F);
			}
		}
	}

	private void setTargetedEntity(boolean hasTarget) {
		dataManager.set(HAS_TARGET, hasTarget);
		dataManager.setDirty(HAS_TARGET);
		if(!hasTarget || getAttackTarget() == null) {
			dataManager.set(TARGET, -1);
		} else {
			dataManager.set(TARGET, getAttackTarget().getEntityId());
		}
	}

	public boolean hasTargetedEntity() {
		return dataManager.get(HAS_TARGET);
	}

	@Nullable
	public Entity getTargetedEntity() {
		return world.getEntityByID(dataManager.get(TARGET));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initEntityAI() {
		EntityAINearestAttackableTarget nearestTarget = new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 2, true, false, new AITargetSelector());
		this.targetTasks.addTask(1, nearestTarget);
		EntityAIBase attack = new EyeAIAttack(this);
		this.tasks.addTask(2, new EyeAIRunAway(this, attack));
		this.tasks.addTask(3, attack);
		this.tasks.addTask(4, new EntityAIWander(this, 1D, 50));
		this.tasks.addTask(5, new EntityAISwimming(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, Entity.class, 8));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);

		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new FlightPathNavigate(this, worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ModGen.SCHRODINGER_LOOT;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public void setNoGravity(boolean noGravity) {
		//Yoink!
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}

	@Override
	public void fall(float distance, float damageMultiplier) {}

	@Override
	public int getMaxFallHeight() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		//Yoink!
	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.RIGHT;
	}

	@Override
	public float getEyeHeight() {
		return height / 2;
	}

	@Override
	public int getVerticalFaceSpeed() {
		return 360;
	}

	@Override
	public int getHorizontalFaceSpeed() {
		return 360;
	}

	private static class AITargetSelector implements Predicate<Entity> {

		@Override
		public boolean apply(@Nullable Entity entity) {
			return entity instanceof EntityLivingBase && (entity instanceof EntityPlayer || ((EntityLivingBase) entity).isEntityUndead());
		}
	}

	private static class EyeAIRunAway extends EntityAIHurtByTarget {

		private final EntityAIBase attack;

		EyeAIRunAway(EntityCreature creatureIn, EntityAIBase attack) {
			super(creatureIn, true);
			this.attack = attack;
		}

		@Override
		public void startExecuting() {
			super.startExecuting();
			runForYourLife();
		}

		private void runForYourLife() {
			if(taskOwner.getAttackTarget() != null) {
				Vec3d vec = RandomPositionGenerator.findRandomTarget(taskOwner, 5, 5);
				if(vec != null) {
					taskOwner.getNavigator().clearPathEntity();
					taskOwner.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, 1D);
					attack.resetTask();
				}
			}
			taskOwner.setRevengeTarget(null);
		}

		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}
	}

	private static class EyeAIAttack extends EntityAIBase {

		private final EntityEyeOfSchrodinger eye;
		private int tickCounter;

		EyeAIAttack(EntityEyeOfSchrodinger eye) {
			this.eye = eye;
			this.setMutexBits(1);
		}

		@Override
		public boolean shouldExecute() {
			EntityLivingBase living = eye.getAttackTarget();
			return living != null && living.isEntityAlive();
		}

		@Override
		@SuppressWarnings("ConstantConditions")
		public void startExecuting() {
			if(eye.getDistanceToEntity(eye.getAttackTarget()) >= 5) {
				eye.getNavigator().clearPathEntity();
				eye.getNavigator().tryMoveToEntityLiving(eye.getAttackTarget(), 0.25D);
			}
			eye.setTargetedEntity(true);
			eye.isAirBorne = true;
			tickCounter = 0;
		}

		public void resetTask() {
			eye.setTargetedEntity(false);
			eye.setAttackTarget(null);
		}

		@Override
		@SuppressWarnings("ConstantConditions")
		public void updateTask() {
			EntityLivingBase target = eye.getAttackTarget();

			if(!eye.canEntityBeSeen(target)) {
				eye.setAttackTarget(null);
			} else if(tickCounter++ >= 10) {
				float f = 1.0F;

				if(eye.world.getDifficulty() == EnumDifficulty.HARD) {
					f += 2.0F;
				}

				target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(eye, eye), f);
				target.attackEntityFrom(DamageSource.causeMobDamage(eye), (float) eye.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
				eye.setAttackTarget(null);
			}
			eye.getLookHelper().setLookPosition(target.posX, target.posY + (double) target.getEyeHeight(), target.posZ, (float) eye.getHorizontalFaceSpeed(), (float) eye.getVerticalFaceSpeed());
		}
	}
}
