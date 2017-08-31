/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.quantum.IQuantumItem;
import arekkuusu.solar.client.render.baked.RenderedBakedModel;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.client.util.helper.TooltipHelper;
import arekkuusu.solar.common.entity.EntityQuingentilliardItem;
import arekkuusu.solar.common.handler.data.QuantumProvider;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.Iterables;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.CONTROL_KEY_DOWN;
import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.SHIFT_KEY_DOWN;

/**
 * Created by <Arekkuusu> on 08/08/2017.
 * It's distributed as part of Solar.
 */
public class ItemQuingentilliard extends ItemBase implements IQuantumItem {

	public ItemQuingentilliard() {
		super(LibNames.QUINGENTILLIARD);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		getKey(stack).ifPresent(uuid -> TooltipHelper.inline()
				.condition(SHIFT_KEY_DOWN)
				.ifAgrees(builder -> {
					builder.addI18("quantum", TooltipHelper.DARK_GRAY_ITALIC).add(":").end();
					List<ItemStack> items = SolarApi.getQuantumStacks(uuid).stream()
							.map(ItemStack::copy).collect(Collectors.toList());
					List<ItemStack> removed = new ArrayList<>();

					items.forEach(i -> {
						if(!removed.contains(i)) {
							items.stream().filter(j -> j != i
									&& ItemHandlerHelper.canItemStacksStack(j, i)).forEach(j -> {
								if(!removed.contains(j)) {
									i.grow(j.getCount());
									removed.add(j);
								}
							});
						}
					});
					items.removeAll(removed);

					items.forEach(item -> builder
							.add("    - ", TextFormatting.DARK_GRAY)
							.add(item.getDisplayName(), TooltipHelper.GRAY_ITALIC)
							.add(" x " + item.getCount()).end()
					);

					int size = items.size();
					if(size > 16) {
						builder.add("    - . . . + ", TooltipHelper.DARK_GRAY_ITALIC)
								.add(size - 15).add(" ").addI18("list_more").end();
					}

					builder.condition(CONTROL_KEY_DOWN).ifAgrees(sub -> {
						sub.addI18("uuid_key", TooltipHelper.DARK_GRAY_ITALIC).add(": ").end();
						String key = uuid.toString();
						sub.add(" > " + key.substring(0, 18)).end();
						sub.add(" > " + key.substring(18)).end();
					});
				}).build(tooltip));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new QuantumProvider(new QuingentilliardItemHandler(this, stack));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack original = player.getHeldItem(hand);
		if(!world.isRemote) {
			EnumHand otherHand = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

			handleItemTransfer(player, world, original, otherHand);
		}

		return ActionResult.newResult(EnumActionResult.FAIL, original);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		EnumHand off = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		if(!world.isRemote && player.getHeldItem(off).isEmpty()) {
			getKey(player.getHeldItem(hand)).ifPresent(
					uuid -> {
						List<ItemStack> stacks = SolarApi.getQuantumStacks(uuid);

						if(!stacks.isEmpty()) {
							ItemStack stack = Iterables.getLast(stacks);

							ItemStack toGiv = stack.copy();
							toGiv.setCount(1);

							stack.shrink(1);
							SolarApi.setQuantumStack(uuid, stack, stacks.size() - 1);

							player.setHeldItem(off, toGiv);
							toGiv.getItem().onItemUse(player, world, pos, off, facing, hitX, hitY, hitZ);

							WorldQuantumData.get(world).markDirty();
						}
					}
			);
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		if(!world.isRemote && !NBTHelper.hasTag(stack, SolarApi.QUANTUM_DATA)) {
			setKey(stack, UUID.randomUUID());
		}
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public boolean onEntityItemUpdate(EntityItem entity) {
		if(entity.world.isRemote || makeVortex(entity)) return false;

		ItemStack container = entity.getItem();

		if(!container.isEmpty() && container.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			QuingentilliardItemHandler handler = (QuingentilliardItemHandler) container.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			List<EntityItem> list = entity.world.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox().grow(6))
					.stream().filter(item -> item.isEntityAlive() && !(item.getItem().getItem() instanceof IQuantumItem))
					.collect(Collectors.toList());

			if(!list.isEmpty()) {
				boolean update = false;

				for(EntityItem item : list) {
					ItemStack inserted = item.getItem();

					for(int i = 0; i < handler.getSlots(); i++) {
						ItemStack test = handler.insertItemAsync(i, inserted);
						if(test != inserted) {
							item.setItem(test);
							update = true;
							break;
						}
					}
				}

				if(update) {
					WorldQuantumData.get(entity.world).markDirty();
					WorldQuantumData.syncToAll();
				}
			}
		}

		entity.setNoGravity(true);
		entity.setNoDespawn();
		return false;
	}

	private boolean makeVortex(EntityItem entity) {
		if(entity.isEntityAlive() && entity.getClass().equals(EntityItem.class)) {
			EntityQuingentilliardItem item = new EntityQuingentilliardItem(entity.world, entity.posX, entity.posY, entity.posZ, entity.getItem());
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
		DummyBakedRegistry.register(this, pair -> new RenderedBakedModel());
		ModelHandler.registerModel(this, 0);
	}

	@Override
	public int getSlots() {
		return 64;
	}

	private static class QuingentilliardItemHandler extends QuantumProvider.QuantumItemHandler {

		private boolean syncToClients;

		QuingentilliardItemHandler(IQuantumItem quantum, ItemStack stack) {
			super(quantum, stack);
		}

		ItemStack insertItemAsync(int slot, ItemStack stack) {
			syncToClients = false;
			return super.insertItem(slot, stack, false);
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			syncToClients = true;
			return super.insertItem(slot, stack, simulate);
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			syncToClients = true;
			return super.extractItem(slot, amount, simulate);
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			if(syncToClients) {
				super.setStackInSlot(slot, stack);
			} else {
				SolarApi.setQuantumAsync(getKey(), stack, slot);
			}
		}
	}
}
