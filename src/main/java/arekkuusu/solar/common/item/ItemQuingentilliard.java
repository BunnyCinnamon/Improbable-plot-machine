/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.quantum.IQuantumStack;
import arekkuusu.solar.api.entanglement.quantum.QuantumHandler;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.client.render.baked.BakedRender;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.client.util.helper.TooltipBuilder;
import arekkuusu.solar.common.entity.EntityQuingentilliardItem;
import arekkuusu.solar.common.handler.data.QuantumStackProvider;
import arekkuusu.solar.common.handler.data.QuantumStackWrapper;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.Iterables;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static arekkuusu.solar.client.util.helper.TooltipBuilder.Condition.SHIFT_KEY_DOWN;

/**
 * Created by <Arekkuusu> on 08/08/2017.
 * It's distributed as part of Solar.
 */
public class ItemQuingentilliard extends ItemBase implements IQuantumStack {

	public static final int SLOTS = 16;
	public static final String FILTER_TAG = "quingentilliard_filter";

	public ItemQuingentilliard() {
		super(LibNames.QUINGENTILLIARD);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		ItemStack lookup = new ItemStack(stack.getOrCreateSubCompound(FILTER_TAG));
		getKey(stack).ifPresent(uuid -> TooltipBuilder.inline()
				.addI18("quingentilliard_filter", TextFormatting.DARK_GRAY).add(": ")
				.add(lookup.isEmpty() ?  " - " : lookup.getDisplayName(), TooltipBuilder.DARK_GRAY_ITALIC)
				.end()
				.skip()
				.condition(SHIFT_KEY_DOWN)
				.ifAgrees(builder -> {
					//Filter
					List<ItemStack> items = QuantumHandler.getQuantumStacks(uuid).stream()
							.map(ItemStack::copy).collect(Collectors.toList());
					List<ItemStack> removed = new ArrayList<>();
					items.forEach(i -> {
						if(!removed.contains(i)) {
							for(ItemStack j : items) {
								if(j == i) continue;
								if(ItemHandlerHelper.canItemStacksStack(j, i)) {
									i.grow(j.getCount());
									removed.add(j);
								} else {
									break;
								}
							}
						}
					});
					items.removeAll(removed);
					//Populate Tooltip
					getDetailedInfo(builder, items, uuid);
				}).build(tooltip));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new QuantumStackProvider(new QuingentilliardStackWrapper(this, stack));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote) {
			setKey(stack, UUID.randomUUID());
			if(player.isSneaking()) {
				ItemStack loopuk = player.getHeldItem(hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
				NBTHelper.setNBT(stack, FILTER_TAG, loopuk.serializeNBT());
			}
		}
		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		EnumHand off = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		if(!world.isRemote) {
			getKey(player.getHeldItem(hand)).ifPresent(uuid -> {
				List<ItemStack> stacks = QuantumHandler.getQuantumStacks(uuid);
				if(!stacks.isEmpty() && Iterables.getLast(stacks).getItem() instanceof ItemBlock) {
					ItemStack stack = Iterables.getLast(stacks);
					ItemStack toGiv = stack.copy();
					toGiv.setCount(1);
					stack.shrink(1);
					QuantumHandler.setQuantumStack(uuid, stack, stacks.size() - 1);
					player.setHeldItem(off, toGiv);
					toGiv.getItem().onItemUse(player, world, pos, off, facing, hitX, hitY, hitZ);
					WorldQuantumData.get(world).markDirty();
				}
			});
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public boolean onEntityItemUpdate(EntityItem entity) {
		if(entity.world.isRemote || makeVortex(entity)) return false;
		if(entity instanceof EntityQuingentilliardItem) {
			EntityQuingentilliardItem quingentilliard = (EntityQuingentilliardItem) entity;
			ItemStack stack = entity.getItem();
			getKey(stack).ifPresent(uuid -> {
				ItemStack lookup = new ItemStack(stack.getOrCreateSubCompound(FILTER_TAG));
				quingentilliard.attractItems(quingentilliard.world, lookup);
				quingentilliard.collectItems(quingentilliard.world, stack, uuid, lookup);
			});
		}
		entity.setNoGravity(true);
		entity.setNoDespawn();
		return false;
	}

	private boolean makeVortex(EntityItem entity) {
		if(entity.isEntityAlive() && entity.getClass().equals(EntityItem.class)) {
			EntityQuingentilliardItem item = new EntityQuingentilliardItem(entity);
			item.setMotion(entity.motionX, entity.motionY, entity.motionZ);
			entity.world.spawnEntity(item);
			entity.setDead();
			return true;
		}
		return !entity.isEntityAlive();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, (format, g) -> new BakedRender());
		ModelHandler.registerModel(this, 0);
	}

	public static class QuingentilliardStackWrapper extends QuantumStackWrapper {

		private boolean syncToClients;

		QuingentilliardStackWrapper(IQuantumStack quantum, ItemStack stack) {
			super(quantum, stack, SLOTS);
		}

		public ItemStack insertItemAsync(int slot, ItemStack stack) {
			syncToClients = false;
			return super.insertItem(slot, stack, false);
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			syncToClients = !simulate;
			return super.insertItem(slot, stack, simulate);
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			syncToClients = !simulate;
			return super.extractItem(slot, amount, simulate);
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			if(syncToClients) {
				super.setStackInSlot(slot, stack);
			} else {
				getKey().ifPresent(uuid -> QuantumHandler.setQuantumAsync(uuid, stack, slot));
			}
		}
	}
}
