/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * Created by <Arekkuusu> on 27/08/2017.
 * It's distributed as part of Solar.
 */
public class TileCrystalVoid extends TileBase implements ITickable {

	private static final Field capability = ReflectionHelper.findField(ItemStack.class, "capabilities");
	private ItemStack stack = ItemStack.EMPTY;
	public int tick;

	@Override
	public void update() {
		tick++;
	}

	public void handleItemTransfer(EntityPlayer player, EnumHand hand) {
		ItemStack inserted = player.getHeldItem(hand);
		if(!inserted.isEmpty()) {
			if(stack.isEmpty() && hasCapability(inserted)) {
				player.setHeldItem(hand, ItemStack.EMPTY);
				setStack(inserted);
			}
		} else {
			if(!stack.isEmpty()) {
				player.setHeldItem(hand, stack);
				setStack(ItemStack.EMPTY);
			}
		}
	}

	private boolean hasCapability(ItemStack stack) {
		try {
			return capability.get(stack) != null; //Riddle me this, riddle me that, does this stack have a cap?
		} catch(IllegalAccessException ignored) {}
		return false;
	}

	public void takeItem(EntityPlayer player, ItemStack stack) {
		ItemStack contained = this.stack.copy();
		if(stack.isEmpty()) {
			player.setHeldItem(EnumHand.MAIN_HAND, contained);
		} else {
			ItemHandlerHelper.giveItemToPlayer(player, contained);
		}
		setStack(ItemStack.EMPTY);
	}

	public void remove() {
		if(!world.isRemote && !stack.isEmpty()) {
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			world.spawnEntity(item);
		}
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 2);
		markDirty();
	}

	public ItemStack getStack() {
		return stack;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return stack.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return stack.getCapability(capability, facing);
	}

	@Override
	void readNBT(NBTTagCompound cmp) {
		if(cmp.hasKey("item")) {
			NBTTagCompound sub = cmp.getCompoundTag("item");
			stack = new ItemStack(sub);
		} else {
			stack = ItemStack.EMPTY;
		}
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
		if(!stack.isEmpty()) {
			NBTTagCompound sub = new NBTTagCompound();
			cmp.setTag("item", stack.writeToNBT(sub));
		}
	}
}
