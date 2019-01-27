package arekkuusu.implom.common.handler.data.capability;

import arekkuusu.implom.api.capability.ILumenCapability;
import arekkuusu.implom.common.entity.EntityLumen;
import net.minecraft.nbt.NBTTagCompound;

public class LumenEntityCapability implements ILumenCapability {

	private final EntityLumen entity;

	public LumenEntityCapability(EntityLumen entity) {
		this.entity = entity;
	}

	@Override
	public void set(int lumen) {
		entity.getDataManager().set(EntityLumen.NEUTRONS, lumen);
	}

	@Override
	public int get() {
		return entity.getDataManager().get(EntityLumen.NEUTRONS);
	}

	@Override
	public void setMax(int max) {
		//NO-OP
	}

	@Override
	public int getMax() {
		return 100;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("lumen", get());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		set(nbt.getInteger("lumen"));
	}
}
