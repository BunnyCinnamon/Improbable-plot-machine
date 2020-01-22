package arekkuusu.implom.common.entity;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.handler.data.capability.LumenEntityCapability;
import arekkuusu.implom.common.handler.data.capability.provider.CapabilityProvider;
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

public class EntityLumen extends Entity {

	public static final DataParameter<Integer> NEUTRONS = EntityDataManager.createKey(EntityLumen.class, DataSerializers.VARINT);
	public final LumenEntityCapability lumenEntityCapability = new LumenEntityCapability(this);
	public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
			.put(Capabilities.LUMEN, lumenEntityCapability)
			.build();

	public static EntityLumen spawn(World world, Vector3 pos, int neutrons) {
		EntityLumen lumen = new EntityLumen(world);
		lumen.setPosition(pos.x(), pos.y(), pos.z());
		world.spawnEntity(lumen);
		return lumen;
	}

	private int tick;

	public EntityLumen(World world) {
		super(world);
		this.setNoGravity(true);
		this.setSize(0.2F, 0.2F);
	}

	@Override
	protected void entityInit() {
		dataManager.register(NEUTRONS, 0);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(world.isRemote) {
			int lumen = lumenEntityCapability.get();
			float scale = 1.5F * ((float) lumen / lumenEntityCapability.getMax());
			Vector3 pos = Vector3.apply(posX, posY, posZ);
			for(int i = 0; i < MathHelper.clamp(lumen, 0, 4); i++) {
				Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 25F);
				Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 25F);
				Vector3 vec = Vector3.apply(motionX, motionY, motionZ).rotate(x.multiply(z)).multiply(0.1D);
				IPM.getProxy().spawnSpeck(world, pos, vec, 30 + world.rand.nextInt(40), scale, 0xFFE077, Light.GLOW, GlowTexture.GLINT.getTexture());
			}
		} else {
			double drag = 0.128D;
			motionX = MathHelper.clamp(motionX, -drag, drag);
			motionY = MathHelper.clamp(motionY, -drag, drag);
			motionZ = MathHelper.clamp(motionZ, -drag, drag);
			motionX += motionX * rand.nextFloat() * 0.01;
			motionY += motionY * rand.nextFloat() * 0.01;
			motionZ += motionZ * rand.nextFloat() * 0.01;
			if(lumenEntityCapability.get() <= 0) setDead();
			else {
				double weight = (double) lumenEntityCapability.get() / (double) lumenEntityCapability.getMax();
				if(tick++ % (int) (45 - 20 * weight) == 0) lumenEntityCapability.drain(1, true);
			}
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

	public void setMotion(Vector3 vec) {
		this.motionX = vec.x();
		this.motionY = vec.y();
		this.motionZ = vec.z();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing)
				? provider.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	/* NBT */
	public static final String NBT_PROVIDER = "provider";

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		compound.setTag(NBT_PROVIDER, provider.serializeNBT());
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		provider.deserializeNBT(compound.getCompoundTag(NBT_PROVIDER));
	}
}
