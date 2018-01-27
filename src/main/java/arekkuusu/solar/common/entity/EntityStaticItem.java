/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 08/07/2017.
 * It's distributed as part of Solar.
 */
public class EntityStaticItem extends EntityItem {

	private float rest = 0.98F;
	private int despawn;
	private int pickup;

	public EntityStaticItem(World world, double x, double y, double z, ItemStack stack) {
		super(world, x, y, z, stack);
		despawn = stack.getItem().getEntityLifespan(stack, world);
		setEntityInvulnerable(true);
		setDefaultPickupDelay();
	}

	public EntityStaticItem(EntityItem item) {
		this(item.world, item.posX, item.posY, item.posZ, item.getItem());
	}

	public EntityStaticItem(World worldIn) {
		super(worldIn);
		setAgeToCreativeDespawnTime();
		setEntityInvulnerable(true);
	}

	@Override
	public void onUpdate() {
		if(getItem().getItem().onEntityItemUpdate(this)) return;

		if(getItem().isEmpty() || (!world.isRemote && despawn != -1 && despawn-- <= 0)) {
			setDead();
		} else {
			onEntityUpdate();

			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;

			if(world.isRemote) noClip = true;
			move(MoverType.SELF, motionX, motionY, motionZ);

			float rest = this.rest;
			if (!noClip && onGround) {
				BlockPos pos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
				IBlockState state = world.getBlockState(pos);
				rest = state.getBlock().getSlipperiness(state, world, pos, this) * 0.98F;
			}

			if (!hasNoGravity() && !onGround) {
				motionY -= 0.03999999910593033D;
			}
			motionX *= rest;
			motionY *= rest;
			motionZ *= rest;
			updateLogic();
		}
	}

	public void updateLogic() {
		//noinspection ConstantConditions
		if(!world.isRemote && pickup > 0 && pickup != -1) {
			pickup--;
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player) {
		if(!this.world.isRemote) {
			if(cannotPickup()) return;
			ItemStack itemstack = this.getItem();
			Item item = itemstack.getItem();
			int i = itemstack.getCount();

			int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(this, player);
			if(hook < 0) return;

			if(hook == 1 || i <= 0 || player.inventory.addItemStackToInventory(itemstack)) {
				net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerItemPickupEvent(player, this);
				player.onItemPickup(this, i);

				if(itemstack.isEmpty()) {
					this.setDead();
					itemstack.setCount(i);
				}

				player.addStat(StatList.getObjectsPickedUpStats(item), i);
			}
		}
	}

	@Override
	public boolean cannotPickup() {
		return pickup > 0 || pickup == -1;
	}

	@Override
	public void setAgeToCreativeDespawnTime() {
		despawn = 6000;
	}

	@Override
	public void setNoDespawn() {
		despawn = -1;
	}

	@Override
	public void setInfinitePickupDelay() {
		pickup = -1;
	}

	@Override
	public void setPickupDelay(int ticks) {
		pickup = ticks;
	}

	@Override
	public void setNoPickupDelay() {
		pickup = 0;
	}

	@Override
	public void setDefaultPickupDelay() {
		pickup = 20;
	}

	public int getPickupDelay() {
		return pickup;
	}

	public void setMotionRest(float rest) {
		this.rest = rest;
	}

	public void setMotion(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("clip", noClip);
		compound.setFloat("rest", rest);
		compound.setInteger("despawn", despawn);
		compound.setInteger("pickup", getPickupDelay());
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		noClip = compound.getBoolean("clip");
		rest = compound.getFloat("rest");
		despawn = compound.getInteger("despawn");
		setPickupDelay(compound.getInteger("pickup"));
	}
}
