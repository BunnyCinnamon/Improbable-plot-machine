package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.api.helper.BiomeTemperatureHelper;
import arekkuusu.implom.api.multiblock.MultiblockRectanguloid.WallType;
import arekkuusu.implom.api.recipe.*;
import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.fluid.ModFluids;
import arekkuusu.implom.common.handler.data.capability.FluidDefaultTank;
import arekkuusu.implom.common.handler.data.capability.FluidMultipleTank;
import arekkuusu.implom.common.handler.data.capability.provider.CapabilityProvider;
import arekkuusu.implom.common.handler.multiblock.MultiblockBlastFurnace;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TileBlastFurnaceController extends TileMultiblockOniichan implements FluidMultipleTank.IMultipleTankHandler, FluidDefaultTank.ITankHandler, ITickable {

	public static final MultiblockBlastFurnace MULTIBLOCK_BLAST_FURNACE = new MultiblockBlastFurnace(10, WallType.ANY);
	public static final int ALLOYING_PER_TICK = 10; // how much liquid can be created per tick to make alloys
	public static final int MAXIMUM_TEMPERATURE = 5000; // how much temperature can the structure withhold
	public static final int TIME_FACTOR = 8; // basically an "accuracy" so the heat can be more fine grained. required temp is multiplied by this

	@SuppressWarnings("DeprecatedIsStillUsed")
	@Deprecated
	public final FluidDefaultTank fluidHotAirTank = new FluidDefaultTank(Fluid.BUCKET_VOLUME, this) {
		@Override
		public boolean isFluidType(Fluid fluid) {
			return fluid == ModFluids.HOT_AIR;
		}

		@Override
		public boolean canDrainFluidType(FluidStack fluid) {
			return false;
		}
	}; // Used to store air fluid, can not drain or fill directly
	public final FluidMultipleTank fluidMultipleTank = new FluidMultipleTank(this, fluidHotAirTank);
	public final ItemStackHandler itemStackHandler = new ItemStackHandler(0) {
		@Override
		protected void onContentsChanged(int slot) {
			itemHeat.updateHeatRequired(slot);
			markDirty();
			sync();
		}
	};
	public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
			.put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, fluidMultipleTank)
			.put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, itemStackHandler)
			.build();
	public LiquidHeat liquidHeat;
	public ItemHeat itemHeat;
	public int temperature;

	public TileBlastFurnaceController() {
		super(MULTIBLOCK_BLAST_FURNACE);
		/*itemTemperatures = new int[0];
		itemTempRequired = new int[0];
		liquidTemperatures = new int[0];
		liquidTempRequired = new int[0];*/
		liquidHeat = new LiquidHeat();
		itemHeat = new ItemHeat();
	}

	@Override
	public void update() {
		if(isClientWorld()) return;
		if(tick % 20 == 0) checkStructure();

		if(active) {
			if(tick % 4 == 0) {
				itemHeat.heat();
				alloyAlloys();
				liquidHeat.heat();
				loseHeat();
			}

			if (temperature < MAXIMUM_TEMPERATURE) { //Burn fuel
				int fluidAmount = fluidMultipleTank.internal.getFluidAmount();
				if(fluidAmount > 0) {
					float diff = (float) fluidAmount / (float) fluidMultipleTank.internal.getCapacity();
					int burningTickSpeed = 1 + (int) (60 - (60 * diff));
					if(tick % burningTickSpeed == 0) {
						burnFuel();
					}
				}
			}
		}
		tick = (tick + 1) % 20;
	}

	private void burnFuel() {
		int fluidAmount = fluidMultipleTank.internal.getFluidAmount();
		int fluidDrain = 0;
		for(int i = 0; i < itemStackHandler.getSlots() && fluidDrain < fluidAmount; i++) {
			ItemStack stack = itemStackHandler.getStackInSlot(i);
			if(!stack.isEmpty()) {
				for(FuelRecipe recipe : IPMApi.getInstance().fuelRecipes) {
					Optional<RecipeMatch.Match> match = recipe.match(stack);
					if(match.isPresent()) {
						int extractAmount = match.get().matches;
						int extracted = 0;

						for(int j = 0; j < extractAmount; j++) {
							int toDrain = recipe.heat / 2;

							if(fluidAmount >= fluidDrain + toDrain) {
								temperature += recipe.heat;
								fluidDrain += toDrain;
								extracted++;
							} else break;
						}

						itemStackHandler.extractItem(i, extracted, false);
						break;
					}
				}
			}

			if(temperature > MAXIMUM_TEMPERATURE) {
				temperature = MAXIMUM_TEMPERATURE;
				break;
			}
		}

		fluidMultipleTank.internal.drainInternal(fluidDrain, true);
	}

	private void alloyAlloys() {
		if(fluidMultipleTank.getFluidAmount() > fluidMultipleTank.getCapacity()) {
			return;
		}
		for(AlloyRecipe recipe : IPMApi.getInstance().alloyRecipes) {
			int matched = recipe.matches(fluidMultipleTank.getFluids());
			if(matched > ALLOYING_PER_TICK) {
				matched = ALLOYING_PER_TICK;
			}
			while(matched > 0) {
				for(FluidStack liquid : recipe.fluids) {
					FluidStack toDrain = liquid.copy();
					FluidStack drained = fluidMultipleTank.drain(toDrain, true);
					assert drained != null;
					if(!drained.isFluidEqual(toDrain) || drained.amount != toDrain.amount) {
						IPM.LOG.error("Furnace alloy creation drained incorrect amount: was {}:{}, should be {}:{}", drained
								.getUnlocalizedName(), drained.amount, toDrain.getUnlocalizedName(), toDrain.amount);
					}
				}

				FluidStack toFill = recipe.result.copy();
				int filled = fluidMultipleTank.fill(toFill, true);
				if(filled != recipe.result.amount) {
					IPM.LOG.error("Furnace alloy creation filled incorrect amount: was {}, should be {} ({})", filled,
							recipe.result.amount * matched, recipe.result.getUnlocalizedName());
					break;
				}
				matched -= filled;
			}
		}
	}

	private void loseHeat() {
		if(temperature > 0) {
			float ambient = BiomeTemperatureHelper.getCelsiusTemperature(getWorld(), getPos());
			float wattToCelsiusPerMinute = 0.031593903989083F;
			float emissivity = 0.75F;
			float sigma = 5.67e-8F;

			float area = (structure.maxPos.getX() * structure.maxPos.getY()) * 4 + (structure.maxPos.getX() * structure.maxPos.getZ());
			float watts = (emissivity) * (sigma) * (area) * ((float) ((temperature + 273) ^ 4) - ambient + 273F);
			float celsiusPerMinuteLoss = watts * wattToCelsiusPerMinute * 1000;
			float temperaturePerTickLoss = celsiusPerMinuteLoss / 60;
			temperature = Math.max(0, temperature - (int) temperaturePerTickLoss);
		}
	}

	@Override
	public void onTankChange(List<FluidStack> old, List<FluidStack> updated, FluidStack changed) {
		if(!isClientWorld()) {
			if(liquidHeat.temperatures.length == 0)
				liquidHeat.resize(updated.size());
			int index = old.indexOf(changed);
			if(index == -1)
				index = updated.indexOf(changed);
			liquidHeat.updateHeatRequired(index);
			liquidHeat.resize(updated.size());
			markDirty();
			sync();
		}
	}

	@Override
	public void onTankChange(@Nullable FluidStack old, @Nullable FluidStack updated) {
		if(!isClientWorld()) {
			markDirty();
			sync();
		}
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockHorizontal.FACING, getPos()).orElse(EnumFacing.NORTH);
	}

	public boolean isActiveLazy() {
		return getStateValue(Properties.ACTIVE, getPos()).orElse(false);
	}

	@Override
	public void updateStructureData() {
		if(structure == null || isActiveLazy()) return;
		int inventorySize = (structure.x) * (structure.y) * (structure.z);
		if(!world.isRemote && itemStackHandler.getSlots() > inventorySize) {
			for(int i = inventorySize; i < itemStackHandler.getSlots(); i++) {
				if(!itemStackHandler.getStackInSlot(i).isEmpty()) {
					dropItem(itemStackHandler.getStackInSlot(i));
				}
			}
		}
		int liquidsSize = inventorySize * Fluid.BUCKET_VOLUME;
		itemStackHandler.setSize(inventorySize);
		fluidMultipleTank.setCapacity(liquidsSize);
		fluidMultipleTank.liquids.clear();
		itemHeat.resize(inventorySize);
		liquidHeat.resize(0);
		markDirty();
	}

	public void dropItem(ItemStack stack) {
		BlockPos pos = getPos().offset(getFacingLazy());
		EntityItem entityItem = new EntityItem(getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
		getWorld().spawnEntity(entityItem);
	}

	@Override
	public boolean hasSecretCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getSecretCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing)
				? provider.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return active && structureMinPos != null && structureMaxPos != null
				&& structureMinPos != BlockPos.ORIGIN && structureMaxPos != BlockPos.ORIGIN
				? new AxisAlignedBB(structureMinPos, structureMaxPos)
				: super.getRenderBoundingBox();
	}

	/* NBT */
	private static final String TAG_PROVIDER = "provider";
	private static final String TAG_TEMPERATURE = "temperature";
	private static final String TAG_ITEM_TEMPERATURES = "itemTemperatures";
	private static final String TAG_LIQUID_TEMPERATURES = "liquidTemperatures";

	@Override
	void readNBT(NBTTagCompound compound) {
		super.readNBT(compound);
		provider.deserializeNBT(compound.getCompoundTag(TAG_PROVIDER));
		temperature = compound.getInteger(TAG_TEMPERATURE);
		itemHeat.read(compound.getCompoundTag(TAG_ITEM_TEMPERATURES));
		liquidHeat.read(compound.getCompoundTag(TAG_LIQUID_TEMPERATURES));
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		super.writeNBT(compound);
		compound.setTag(TAG_PROVIDER, provider.serializeNBT());
		compound.setInteger(TAG_TEMPERATURE, temperature);
		compound.setTag(TAG_ITEM_TEMPERATURES, itemHeat.write());
		compound.setTag(TAG_LIQUID_TEMPERATURES, liquidHeat.write());
	}

	@Override
	void readSync(NBTTagCompound compound) {
		super.readSync(compound);
		provider.deserializeNBT(compound.getCompoundTag(TAG_PROVIDER));
	}

	@Override
	void writeSync(NBTTagCompound compound) {
		super.writeSync(compound);
		compound.setTag(TAG_PROVIDER, provider.serializeNBT());
	}

	public class ItemHeat {
		public int[] tempRequired = new int[0];
		public int[] temperatures = new int[0];

		private void heat() {
			for(int i = 0; i < itemStackHandler.getSlots(); i++) {
				ItemStack stack = itemStackHandler.getStackInSlot(i);
				if(!stack.isEmpty()) {
					if(tempRequired[i] > 0) {
						if(canHeat(i)) {
							if(temperatures[i] >= tempRequired[i]) {
								if(finishSlot(stack, i)) {
									/*temperatures[i] = 0;
									tempRequired[i] = 0;*/
									updateHeatRequired(i);
								}
							}
							else {
								int heatedItem = temperature / 100;
								temperatures[i] += heatedItem;
								temperature -= heatedItem;
							}
						}
					}
				}
				else {
					temperatures[i] = 0;
				}
			}

			if(temperature < 0) {
				temperature = 0;
			}
		}

		public boolean canHeat(int index) {
			return temperature >= getHeatRequired(index);
		}

		public int getHeatRequired(int index) {
			if(index >= tempRequired.length) {
				return 0;
			}
			return tempRequired[index] / TIME_FACTOR;
		}

		public void resize(int size) {
			this.temperatures = Arrays.copyOf(temperatures, size);
			this.tempRequired = Arrays.copyOf(tempRequired, size);
		}

		public void updateHeatRequired(int index) {
			ItemStack stack = itemStackHandler.getStackInSlot(index);
			boolean heatUpdated = false;
			if(!stack.isEmpty()) {
				for(MeltingRecipe recipe : IPMApi.getInstance().meltingRecipes) {
					if(recipe.isMatch(stack)) {
						tempRequired[index] = recipe.temperature;
						temperatures[index] = 0;
						heatUpdated = true;
					}
				}
			}

			if(!heatUpdated) {
				tempRequired[index] = 0;
				temperatures[index] = 0;
			}
		}

		public boolean finishSlot(ItemStack stack, int i) {
			if(fluidMultipleTank.getFluidAmount() > fluidMultipleTank.getCapacity()) {
				return false;
			}

			boolean meltedItem = false;
			for(MeltingRecipe recipe : IPMApi.getInstance().meltingRecipes) {
				if(recipe.isMatch(stack)) {
					FluidStack fluid = recipe.fluid.copy();
					int filled = fluidMultipleTank.fill(fluid, false);
					if(filled == recipe.fluid.amount) {
						itemStackHandler.extractItem(i, 1, false);
						fluidMultipleTank.fill(fluid, true);
						meltedItem = true;
						break;
					}
				}
			}

			return meltedItem;
		}

		public void read(NBTTagCompound tag) {
			temperatures = tag.getIntArray("temp");
			tempRequired = tag.getIntArray("temp_req");
		}

		public NBTTagCompound write() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("temp", temperatures);
			tag.setIntArray("temp_req", tempRequired);
			return tag;
		}
	}

	public class LiquidHeat {
		public int[] tempRequired = new int[0];
		public int[] temperatures = new int[0];

		private void heat() {
			if(temperature <= 0) {
				return;
			}

			for(int i = 0; i < fluidMultipleTank.liquids.size() && temperature > 0; i++) {
				FluidStack stack = fluidMultipleTank.liquids.get(i);
				if(stack.amount > 0) {
					if(tempRequired[i] > 0) {
						if(canHeat(i)) {
							if(temperatures[i] >= tempRequired[i]) {
								finishSlot(stack);
							}
							else {
								int heatedItem = temperature / 500;
								temperatures[i] += heatedItem;
								temperature -= heatedItem;
							}
						}
					}
				}
				else {
					temperatures[i] = 0;
				}
			}

			if(temperature < 0) {
				temperature = 0;
			}
		}

		public boolean canHeat(int index) {
			return temperature >= getHeatRequired(index);
		}

		protected int getHeatRequired(int index) {
			if(index >= tempRequired.length) {
				return 0;
			}
			return tempRequired[index] / TIME_FACTOR;
		}

		public void resize(int size) {
			this.temperatures = Arrays.copyOf(temperatures, size);
			this.tempRequired = Arrays.copyOf(tempRequired, size);
		}

		private void updateHeatRequired(int index) {
			FluidStack fluid = fluidMultipleTank.getFluids().get(index);
			boolean heatUpdated = false;
			if(fluid.amount > 0) {
				for(EvaporationRecipe recipe : IPMApi.getInstance().evaporationRecipes) {
					if(recipe.isMatch(fluid)) {
						tempRequired[index] = recipe.temperature;
						temperatures[index] = recipe.getTemperature(fluid);
						heatUpdated = true;
					}
				}
			}

			if(!heatUpdated) {
				tempRequired[index] = 0;
				temperatures[index] = 0;
			}
		}

		private void finishSlot(FluidStack fluid) {
			for(EvaporationRecipe recipe : IPMApi.getInstance().evaporationRecipes) {
				if(recipe.isMatch(fluid)) {
					FluidStack drained = fluidMultipleTank.drain(recipe.drain.copy(), false);
					if(drained != null && drained.amount == recipe.drain.amount) {
						if(recipe.fill != null)
							fluidMultipleTank.fill(recipe.fill.copy(), true);
						fluidMultipleTank.drain(recipe.drain.copy(), true);
						break;
					}
				}
			}
		}

		public void read(NBTTagCompound tag) {
			temperatures = tag.getIntArray("temp");
			tempRequired = tag.getIntArray("temp_req");
		}

		public NBTTagCompound write() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("temp", temperatures);
			tag.setIntArray("temp_req", tempRequired);
			return tag;
		}
	}
}
