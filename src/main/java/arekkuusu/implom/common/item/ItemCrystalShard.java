/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.api.helper.LumenHelper;
import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.common.handler.data.capability.LumenShardCapability;
import arekkuusu.implom.common.handler.data.capability.provider.LumenProvider;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 14/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class ItemCrystalShard extends ItemBase {

	public ItemCrystalShard() {
		super(LibNames.CRYSTAL_SHARD);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(this.isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			LumenHelper.getCapability(stack).ifPresent(data -> data.set(10));
			items.add(stack);
		}
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		if(!entityItem.world.isRemote && entityItem.world.getBlockState(entityItem.getPosition()).getBlock() == Blocks.WATER) {
			entityItem.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityItem.getPosition()), i -> {
				assert i != null;
				return i.getItem().getItem() == ModItems.IMBUED_QUARTZ;
			}).stream().findAny().ifPresent(i -> {
				i.getItem().shrink(1);
				entityItem.getItem().shrink(1);
				ItemStack stack = NBTHelper.setEnum(new ItemStack(ModItems.QUARTZ), ItemQuartz.Quartz.BLUE_SMALL, ItemQuartz.Constants.NBT_QUARTZ);
				EntityItem entity = new EntityItem(
						entityItem.world,
						entityItem.posX,
						entityItem.posY,
						entityItem.posZ,
						stack
				);
				entityItem.world.setBlockToAir(entityItem.getPosition());
				entityItem.world.spawnEntity(entity);
				double d3 = (double) entity.getPosition().getX() + itemRand.nextDouble() * 0.10000000149011612D;
				double d8 = (double) entity.getPosition().getY() + itemRand.nextDouble();
				double d13 = (double) entity.getPosition().getZ() + itemRand.nextDouble();
				((WorldServer) entity.world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, false, d3, d8, d13, 15, 0.0D, 0.0D, 0.0D, 0.1D);
				entity.world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1F, 1F);
			});
		}
		return false;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new LumenProvider(new LumenShardCapability(stack));
	}
}
