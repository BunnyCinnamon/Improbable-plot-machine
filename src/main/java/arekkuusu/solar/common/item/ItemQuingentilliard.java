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
import arekkuusu.solar.client.render.baked.RenderedBakedModel;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.client.util.helper.TooltipHelper;
import arekkuusu.solar.common.entity.EntityQuingentilliardItem;
import arekkuusu.solar.common.handler.data.QuantumStackProvider;
import arekkuusu.solar.common.handler.data.QuantumStackWrapper;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.Iterables;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
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
public class ItemQuingentilliard extends ItemBase implements IQuantumStack {

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
					//Filter
					List<ItemStack> items = QuantumHandler.getQuantumStacks(uuid).stream()
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
					//Populate Tooltip
					builder.addI18("quantum_data", TooltipHelper.DARK_GRAY_ITALIC).end();
					items.forEach(item -> builder
							.add("    - ", TextFormatting.DARK_GRAY)
							.add(item.getDisplayName(), TooltipHelper.GRAY_ITALIC)
							.add(" x " + item.getCount()).end()
					);
					builder.skip();

					builder.condition(CONTROL_KEY_DOWN).ifAgrees(sub -> {
						sub.addI18("uuid_key", TooltipHelper.DARK_GRAY_ITALIC).add(": ").end();
						String key = uuid.toString();
						sub.add(" > ").add(key.substring(0, 18)).end();
						sub.add(" > ").add(key.substring(18)).end();
					});
				}).build(tooltip));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new QuantumStackProvider(new QuingentilliardStackWrapper(this, stack));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote && player.isSneaking()) {
			setKey(stack, UUID.randomUUID());
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

			dragItems(quingentilliard, quingentilliard.world, stack);
			collectItems(quingentilliard, quingentilliard.world, stack);
		}

		entity.setNoGravity(true);
		entity.setNoDespawn();
		return false;
	}

	private void dragItems(EntityQuingentilliardItem entity, World world, ItemStack stack) {
		getKey(stack).ifPresent(uuid -> {
			List<EntityItem> list = getItemsFiltered(world, entity.getEntityBoundingBox().grow(9), uuid);
			for(EntityItem item : list) {
				applyGravity(entity.posX, entity.posY, entity.posZ, item);
			}
		});
	}

	private void applyGravity(double x, double y, double z, Entity sucked) {
		x += 0.5D - sucked.posX;
		y += 0.5D - sucked.posY;
		z += 0.5D - sucked.posZ;

		double sqrt = Math.sqrt(x * x + y * y + z * z);
		double v = sqrt / 9;

		if(sqrt <= 9) {
			double strength = (1 - v) * (1 - v);
			double power = 0.075D * 1.5D;

			sucked.motionX += (x / sqrt) * strength * power;
			sucked.motionY += (y / sqrt) * strength * power;
			sucked.motionZ += (z / sqrt) * strength * power;
		}
	}

	private void collectItems(EntityQuingentilliardItem entity, World world, ItemStack stack) {
		if(stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			QuingentilliardStackWrapper handler = (QuingentilliardStackWrapper) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(handler == null) return;

			getKey(stack).ifPresent(uuid -> {
				List<EntityItem> list = getItemsFiltered(world, entity.getEntityBoundingBox().grow(0.5F), uuid);

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
						WorldQuantumData.syncChanges(uuid);
					}
				}
			});
		}
	}

	private List<EntityItem> getItemsFiltered(World world, AxisAlignedBB box, UUID uuid) {
		List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, box, Entity::isEntityAlive);
		List<ItemStack> stacks = QuantumHandler.getQuantumStacks(uuid);

		return list.stream().filter(item -> {
			ItemStack stack = item.getItem();
			item.setAgeToCreativeDespawnTime();
			item.setNoGravity(true);

			return !(stack.getItem() instanceof IQuantumStack)
					&& stacks.stream().anyMatch(match -> ItemHandlerHelper.canItemStacksStack(match, stack));
		}).collect(Collectors.toList());
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
		DummyBakedRegistry.register(this, (format, g) -> new RenderedBakedModel());
		ModelHandler.registerModel(this, 0);
	}

	private static class QuingentilliardStackWrapper extends QuantumStackWrapper {

		private boolean syncToClients;

		QuingentilliardStackWrapper(IQuantumStack quantum, ItemStack stack) {
			super(quantum, stack, 64);
		}

		ItemStack insertItemAsync(int slot, ItemStack stack) {
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
				QuantumHandler.setQuantumAsync(getKey(), stack, slot);
			}
		}
	}
}
