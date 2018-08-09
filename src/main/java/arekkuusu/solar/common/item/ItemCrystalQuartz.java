/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import arekkuusu.solar.api.entanglement.energy.data.LumenStackProvider;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.common.entity.EntityStaticItem;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 14/09/2017.
 * It's distributed as part of Solar.
 */
public class ItemCrystalQuartz extends ItemBase {

	public static final int MAX_LUMEN = 100;

	public ItemCrystalQuartz() {
		super(LibNames.CRYSTAL_QUARTZ);
		setMaxStackSize(1);
		setMaxDamage(100);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - (double) getLumen(stack) / (double) MAX_LUMEN;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return 0x49FFFF;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stackIn, @Nullable NBTTagCompound nbt) {
		return LumenStackProvider.create(new ILumen() {
			{
				setLumen(stackIn, MAX_LUMEN);
			}

			private final ItemStack stack = stackIn;

			@Override
			public int get() {
				return getLumen(stack);
			}

			@Override
			public void set(int neutrons) {
				if(neutrons <= MAX_LUMEN) {
					setLumen(stack, neutrons);
				}
			}

			@Override
			public int getMax() {
				return MAX_LUMEN;
			}
		});
	}

	private int getLumen(ItemStack stack) {
		return NBTHelper.getInteger(stack, ILumen.NBT_TAG);
	}

	private void setLumen(ItemStack stack, int neutrons) {
		NBTHelper.setInteger(stack, ILumen.NBT_TAG, neutrons);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entity) {
		if(!entity.world.isRemote) {
			entity = makeQuantum(entity);
			if(itemRand.nextFloat() < 0.1F) {
				Vector3 from = new Vector3.WrappedVec3d(entity.getPositionVector()).asImmutable();
				Vector3 to = Vector3.rotateRandom().multiply(2).add(from);
				if(isValidSpawn(entity.world, to)) {
					entity.setPositionAndUpdate(to.x(), to.y(), to.z());
					entity.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 0.25F, 0.5F);
				}
			}
		} else {
			Vector3 pos = new Vector3(entity).add(0D, entity.height * 1.75D, 0D);
			Vector3 speedVec = Vector3.rotateRandom().multiply(0.02D);
			FXUtil.spawnLight(entity.world, pos, speedVec, 45, 1F, 0x1BE564, Light.GLOW);
		}
		return false;
	}

	private EntityItem makeQuantum(EntityItem entity) {
		if(entity.isEntityAlive() && entity.getClass().equals(EntityItem.class)) {
			EntityStaticItem item = new EntityStaticItem(entity);
			item.setMotion(entity.motionX, entity.motionY, entity.motionZ);
			item.setNoGravity(true);
			entity.world.spawnEntity(item);
			entity.setDead();
			return item;
		}
		return entity;
	}

	private boolean isValidSpawn(World world, Vector3 to) {
		BlockPos pos = to.toBlockPos();
		return world.isValid(pos) && world.isBlockLoaded(pos) && world.isAirBlock(pos);
	}
}
