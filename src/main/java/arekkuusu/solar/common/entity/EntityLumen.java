package arekkuusu.solar.common.entity;

import arekkuusu.solar.api.capability.energy.data.ILumen;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Optional;

public class EntityLumen extends Entity {

	private static final DataParameter<Integer> LUMEN = EntityDataManager.createKey(EntityLumen.class, DataSerializers.VARINT);

	public static EntityLumen spawn(World world, Vector3 pos, int neutrons) {
		EntityLumen lumen = new EntityLumen(world);
		Optional.ofNullable(lumen.getCapability(ModCapability.NEUTRON_CAPABILITY, null)).ifPresent(l -> l.set(neutrons));
		lumen.setPosition(pos.x(), pos.y(), pos.z());
		world.spawnEntity(lumen);
		return lumen;
	}

	private final ILumen handler;
	private int tick;

	public EntityLumen(World world) {
		super(world);
		this.handler = new LumenEntityWrapper(this);
		this.setNoGravity(true);
		this.setSize(0.2F, 0.2F);
		this.noClip = true;
	}

	@Override
	protected void entityInit() {
		dataManager.register(LUMEN, 0);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(world.isRemote) {
			int lumen = MathHelper.clamp(handler.get(), 0, 4);
			float scale = 2F * ((float) lumen / 4F);
			Vector3 pos = Vector3.apply(posX, posY, posZ);
			for(int i = 0; i < lumen; i++) {
				Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 25F);
				Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 25F);
				Vector3 vec = Vector3.apply(motionX, motionY, motionZ).rotate(x.multiply(z)).multiply(0.1D);
				Solar.getProxy().spawnLuminescence(world, pos, vec, 30 + world.rand.nextInt(40), scale, GlowTexture.GLINT);
			}
		} else {
			double drag = 0.128D;
			motionX = MathHelper.clamp(motionX, -drag, drag);
			motionY = MathHelper.clamp(motionY, -drag, drag);
			motionZ = MathHelper.clamp(motionZ, -drag, drag);
			double weightDiff = 10 * Math.log10(handler.get()) / 10 * Math.log10(handler.getMax());
			if(tick++ % (int)(40 * (1 - weightDiff)) == 0 && tick > 1) {
				handler.set((int) ((float) handler.get() * 0.75F));
			}
			if(handler.get() <= 0) setDead();
		}
		move(MoverType.SELF, motionX, motionY, motionZ);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return ModCapability.NEUTRON_CAPABILITY == capability || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return ModCapability.NEUTRON_CAPABILITY == capability
				? ModCapability.NEUTRON_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	public void setMotion(Vector3 vec) {
		this.motionX = vec.x();
		this.motionY = vec.y();
		this.motionZ = vec.z();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		handler.set(compound.getInteger("lumen"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("lumen", handler.get());
	}

	public static class LumenEntityWrapper implements ILumen {

		private final EntityLumen lumen;

		LumenEntityWrapper(EntityLumen lumen) {
			this.lumen = lumen;
		}

		@Override
		public int get() {
			return lumen.dataManager.get(LUMEN);
		}

		@Override
		public void set(int neutrons) {
			if(neutrons < 0) neutrons = 0;
			lumen.dataManager.set(LUMEN, neutrons);
		}

		@Override
		public int getMax() {
			return Integer.MAX_VALUE;
		}
	}
}
