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

			getKey(stack).ifPresent(uuid -> {
				quingentilliard.attractItems(quingentilliard.world, uuid);
				quingentilliard.collectItems(quingentilliard.world, stack, uuid);
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
		DummyBakedRegistry.register(this, (format, g) -> new RenderedBakedModel());
		ModelHandler.registerModel(this, 0);
	}

	public static class QuingentilliardStackWrapper extends QuantumStackWrapper {

		private boolean syncToClients;

		QuingentilliardStackWrapper(IQuantumStack quantum, ItemStack stack) {
			super(quantum, stack, 64);
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
				QuantumHandler.setQuantumAsync(getKey(), stack, slot);
			}
		}
	}
}
