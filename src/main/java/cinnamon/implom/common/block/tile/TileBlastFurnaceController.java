package cinnamon.implom.common.block.tile;

import cinnamon.implom.IPM;
import cinnamon.implom.api.API;
import cinnamon.implom.api.helper.BiomeTemperatureHelper;
import cinnamon.implom.api.helper.WorldHelper;
import cinnamon.implom.api.multiblock.MultiBlockOniichan;
import cinnamon.implom.api.multiblock.MultiBlockRectanguloid;
import cinnamon.implom.api.recipe.*;
import cinnamon.implom.common.block.BlockBaseMultiBlock;
import cinnamon.implom.common.block.tile.multiblock.MultiBlockBlastFurnace;
import cinnamon.implom.common.handler.data.capability.MultipleTank;
import cinnamon.implom.common.handler.data.capability.SharedStackHandler;
import cinnamon.implom.common.handler.data.capability.SimpleTank;
import cinnamon.implom.common.handler.data.capability.provider.CapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TileBlastFurnaceController extends TileMultiBlockOniichan implements MultiBlockOniichan {

    public static final MultiBlockBlastFurnace MULTI_BLOCK_BLAST_FURNACE = new MultiBlockBlastFurnace(10, MultiBlockRectanguloid.WallType.ANY);
    public static final int ALLOYING_PER_TICK = 10; // how much liquid can be created per tick to make alloys
    public static final int MAXIMUM_TEMPERATURE = 5000; // how much temperature can the structure withhold
    public static final int HEAT_TIME_FACTOR = 8; // basically an "accuracy" so the heat can be more fine grained. required temp is multiplied by this

    public final SimpleTank airTank = new SimpleTank(this::airTankChange); // Used to store air fluid, can not drain or fill directly
    public final MultipleTank meltingTank = new MultipleTank(this::meltingTankChange);
    public final ItemStackHandler fuelStackHandler = new ItemStackHandler(0) {

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return API.fuelRecipes.stream().anyMatch(r -> r.match(stack).isPresent());
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();
        }
    };
    public final ItemStackHandler oreStackHandler = new ItemStackHandler(0) {

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return API.meltingRecipes.stream().anyMatch(r -> r.match(stack));
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (isClientWorld()) return;
            itemHeat.updateHeatRequired(slot);
            setChanged();
            sync();
        }
    };
    public final SharedStackHandler sharedStackHandler = new SharedStackHandler(fuelStackHandler, oreStackHandler);
    public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
            .put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, airTank, Direction.UP)
            .put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, meltingTank, Direction.DOWN)
            .put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sharedStackHandler)
            .put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, fuelStackHandler, Direction.UP)
            .put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, oreStackHandler, Direction.DOWN)
            .build();
    public LiquidHeat liquidHeat;
    public ItemHeat itemHeat;
    public float temperature;

    public TileBlastFurnaceController(BlockPos arg2, BlockState arg3) {
        super(ModTiles.BLAST_FURNACE_CONTROLLER.get(), MULTI_BLOCK_BLAST_FURNACE, arg2, arg3);
        liquidHeat = new LiquidHeat();
        itemHeat = new ItemHeat();
    }

    private void burnFuel() {
        int fluidAmount = airTank.getFluidInTank(airTank.getTanks()).getAmount();
        int fluidDrain = 0;
        for (int i = 0; i < fuelStackHandler.getSlots() && fluidDrain < fluidAmount; i++) {
            ItemStack stack = fuelStackHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                for (FuelRecipe recipe : API.fuelRecipes) {
                    Optional<RecipeMatch.Match> match = recipe.match(stack);
                    if (match.isPresent()) {
                        int extractAmount = match.get().matches;
                        int extracted = 0;

                        for (int j = 0; j < extractAmount; j++) {
                            int toDrain = recipe.heat / 2;

                            if (fluidAmount >= fluidDrain + toDrain) {
                                temperature += recipe.heat;
                                fluidDrain += toDrain;
                                extracted++;
                            } else break;
                        }

                        fuelStackHandler.extractItem(i, extracted, false);
                        break;
                    }
                }
            }

            if (temperature > MAXIMUM_TEMPERATURE) {
                temperature = MAXIMUM_TEMPERATURE;
                break;
            }
        }

        airTank.drain(fluidDrain, IFluidHandler.FluidAction.EXECUTE);
    }

    private void alloyAlloys() {
        if (meltingTank.getFluidAmount() > meltingTank.maxCapacity) {
            return;
        }
        for (AlloyRecipe recipe : API.alloyRecipes) {
            int matched = recipe.matches(meltingTank.fluids);
            if (matched > ALLOYING_PER_TICK) {
                matched = ALLOYING_PER_TICK;
            }
            while (matched > 0) {
                for (FluidStack liquid : recipe.fluids) {
                    FluidStack toDrain = liquid.copy();
                    FluidStack drained = meltingTank.drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
                    if (!drained.isFluidEqual(toDrain) || drained.getAmount() != toDrain.getAmount()) {
                        IPM.LOG.error("Furnace alloy creation drained incorrect amount: was {}:{}, should be {}:{}", drained
                                .getDisplayName(), drained.getAmount(), toDrain.getDisplayName(), toDrain.getAmount());
                    }
                }

                FluidStack toFill = recipe.result.copy();
                int filled = meltingTank.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                if (filled != recipe.result.getAmount()) {
                    IPM.LOG.error("Furnace alloy creation filled incorrect amount: was {}, should be {} ({})", filled,
                            recipe.result.getAmount() * matched, recipe.result.getDisplayName());
                    break;
                }
                matched -= filled;
            }
        }
    }

    private void loseHeat() {
        if (temperature > 0) {
            float ambient = BiomeTemperatureHelper.getCelsiusTemperature(getLevel(), getBlockPos());
            float wattToCelsiusPerMinute = 0.031593903989083F;
            float emissivity = 0.75F;
            float sigma = 5.67e-8F;

            float area = structure.x * structure.z * 2 + structure.x * structure.y * 2 + structure.z * structure.y * 2;
            float watts = (emissivity) * (sigma) * (area) * ((float) (((int) temperature + 273) ^ 4) - ambient + 273F);
            float celsiusPerMinuteLoss = watts * wattToCelsiusPerMinute * 1000;
            float temperaturePerTickLoss = celsiusPerMinuteLoss / 60;
            temperature = Math.max(0, temperature - temperaturePerTickLoss);
        }
    }

    private void loseAir() {
        int amount = airTank.getFluidInTank(airTank.getTanks()).getAmount();
        if(amount > 0) {
            float min = Math.min(amount / 100F, amount);
            int floor = (int) Math.floor(min);
            airTank.drain(floor, IFluidHandler.FluidAction.EXECUTE);
        }
    }
    // Progress update

    //Structure change
    @Override
    public void updateStructureData() {
        if (structure == null || !isActiveLazy()) provider.invalidateAll();
        if (structure == null || isActiveLazy()) return;
        int inventoryVerticalSize = (structure.x) * (structure.y) * (structure.z);
        int inventoryHorizontalSize = (structure.x) * (structure.z);
        fuelStackHandler.setSize(inventoryHorizontalSize);
        oreStackHandler.setSize(inventoryVerticalSize);
        meltingTank.setTankCapacity(inventoryVerticalSize * 1000);
        airTank.setTankCapacity(inventoryHorizontalSize * 1000);
        meltingTank.clear();
        airTank.clear();
        itemHeat.resize(inventoryVerticalSize);
        liquidHeat.resize(0);
        setChanged();
        sync();
    }

    public void meltingTankChange(List<FluidStack> old, List<FluidStack> updated, FluidStack changed) {
        if (liquidHeat.temperatures.length == 0)
            liquidHeat.resize(updated.size());
        int index = old.indexOf(changed);
        if (index == -1)
            index = updated.indexOf(changed);
        liquidHeat.resize(updated.size());
        liquidHeat.updateHeatRequired(index);
        setChanged();
        sync();
    }

    public void airTankChange() {
        setChanged();
        sync();
    }
    //Structure change

    @Nullable
    @Override
    public <T> LazyOptional<T> getMultiBlockCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return provider.getCapability(cap, side);
    }

    public boolean isActiveLazy() {
        return WorldHelper.getStateValue(getLevel(), getBlockPos(), BlockBaseMultiBlock.ACTIVE).orElse(false);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return active && structureMinPos != null && structureMaxPos != null
                && structureMinPos != BlockPos.ZERO && structureMaxPos != BlockPos.ZERO
                ? new AABB(structureMinPos, structureMaxPos)
                : super.getRenderBoundingBox();
    }

    /* NBT */
    private static final String TAG_PROVIDER = "provider";
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_ITEM_TEMPERATURES = "itemTemperatures";
    private static final String TAG_LIQUID_TEMPERATURES = "liquidTemperatures";

    @Override
    void loadDisk(CompoundTag compound) {
        super.loadDisk(compound);
        provider.deserializeNBT(compound.getCompound(TAG_PROVIDER));
        temperature = compound.getFloat(TAG_TEMPERATURE);
        itemHeat.read(compound.getCompound(TAG_ITEM_TEMPERATURES));
        liquidHeat.read(compound.getCompound(TAG_LIQUID_TEMPERATURES));
    }

    @Override
    void saveDisk(CompoundTag compound) {
        super.saveDisk(compound);
        compound.put(TAG_PROVIDER, provider.serializeNBT());
        compound.putFloat(TAG_TEMPERATURE, temperature);
        compound.put(TAG_ITEM_TEMPERATURES, itemHeat.write());
        compound.put(TAG_LIQUID_TEMPERATURES, liquidHeat.write());
    }

    @Override
    void writeSync(CompoundTag compound) {
        super.writeSync(compound);
        compound.put(TAG_PROVIDER, provider.serializeNBT());
    }

    @Override
    void readSync(CompoundTag compound) {
        super.readSync(compound);
        provider.deserializeNBT(compound.getCompound(TAG_PROVIDER));
    }

    public class ItemHeat {
        public int[] tempRequired = new int[0];
        public int[] temperatures = new int[0];

        private void heat() {
            for (int i = 0; i < oreStackHandler.getSlots(); i++) {
                ItemStack stack = oreStackHandler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    if (tempRequired[i] > 0) {
                        if (canHeat(i)) {
                            if (temperatures[i] >= tempRequired[i]) {
                                if (finishSlot(stack, i)) {
                                    updateHeatRequired(i);
                                }
                            } else {
                                int heatedItem = (int) temperature / 100;
                                temperatures[i] += heatedItem;
                                temperature -= heatedItem;
                            }
                        }
                    }
                } else {
                    temperatures[i] = 0;
                }
            }

            if (temperature < 0) {
                temperature = 0;
            }
        }

        public boolean canHeat(int index) {
            return temperature >= getHeatRequired(index);
        }

        public int getHeatRequired(int index) {
            if (index >= tempRequired.length) {
                return 0;
            }
            return tempRequired[index] / HEAT_TIME_FACTOR;
        }

        public void resize(int size) {
            this.temperatures = Arrays.copyOf(temperatures, size);
            this.tempRequired = Arrays.copyOf(tempRequired, size);
        }

        public void updateHeatRequired(int index) {
            ItemStack stack = oreStackHandler.getStackInSlot(index);
            boolean heatUpdated = false;
            if (!stack.isEmpty()) {
                for (MeltingRecipe recipe : API.meltingRecipes) {
                    if (recipe.match(stack)) {
                        tempRequired[index] = recipe.temperature;
                        temperatures[index] = 0;
                        heatUpdated = true;
                    }
                }
            }

            if (!heatUpdated) {
                tempRequired[index] = 0;
                temperatures[index] = 0;
            }
        }

        public boolean finishSlot(ItemStack stack, int i) {
            if (meltingTank.getFluidAmount() > meltingTank.maxCapacity) {
                return false;
            }

            boolean meltedItem = false;
            for (MeltingRecipe recipe : API.meltingRecipes) {
                if (recipe.match(stack)) {
                    FluidStack fluid = recipe.fluid.copy();
                    int filled = meltingTank.fill(fluid, IFluidHandler.FluidAction.SIMULATE);
                    if (filled == recipe.fluid.getAmount()) {
                        oreStackHandler.extractItem(i, 1, false);
                        meltingTank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                        meltedItem = true;
                        break;
                    }
                }
            }

            return meltedItem;
        }

        public void read(CompoundTag tag) {
            temperatures = tag.getIntArray("temp");
            tempRequired = tag.getIntArray("temp_req");
        }

        public CompoundTag write() {
            CompoundTag tag = new CompoundTag();
            tag.putIntArray("temp", temperatures);
            tag.putIntArray("temp_req", tempRequired);
            return tag;
        }
    }

    public class LiquidHeat {
        public int[] tempRequired = new int[0];
        public int[] temperatures = new int[0];

        private void heat() {
            if (temperature <= 0) {
                return;
            }

            for (int i = 0; i < meltingTank.fluids.size() && temperature > 0; i++) {
                FluidStack stack = meltingTank.fluids.get(i);
                if (stack.getAmount() > 0) {
                    if (tempRequired[i] > 0) {
                        if (canHeat(i)) {
                            if (temperatures[i] >= tempRequired[i]) {
                                finishSlot(stack);
                            } else {
                                int heatedItem = (int) temperature / 500;
                                temperatures[i] += heatedItem;
                                temperature -= heatedItem;
                            }
                        }
                    }
                } else {
                    temperatures[i] = 0;
                }
            }

            if (temperature < 0) {
                temperature = 0;
            }
        }

        public boolean canHeat(int index) {
            return temperature >= getHeatRequired(index);
        }

        protected int getHeatRequired(int index) {
            if (index >= tempRequired.length) {
                return 0;
            }
            return tempRequired[index] / HEAT_TIME_FACTOR;
        }

        public void resize(int size) {
            this.temperatures = Arrays.copyOf(temperatures, size);
            this.tempRequired = Arrays.copyOf(tempRequired, size);
        }

        private void updateHeatRequired(int index) {
            FluidStack fluid = meltingTank.fluids.get(index);
            boolean heatUpdated = false;
            if (fluid.getAmount() > 0) {
                for (EvaporationRecipe recipe : API.evaporationRecipes) {
                    if (recipe.isMatch(fluid)) {
                        tempRequired[index] = recipe.temperature;
                        temperatures[index] = recipe.getTemperature(fluid);
                        heatUpdated = true;
                    }
                }
            }

            if (!heatUpdated) {
                tempRequired[index] = 0;
                temperatures[index] = 0;
            }
        }

        private void finishSlot(FluidStack fluid) {
            for (EvaporationRecipe recipe : API.evaporationRecipes) {
                if (recipe.isMatch(fluid)) {
                    FluidStack drained = meltingTank.drain(recipe.drain.copy(), IFluidHandler.FluidAction.SIMULATE);
                    if (drained.getAmount() == recipe.drain.getAmount()) {
                        if (recipe.fill != null)
                            meltingTank.fill(recipe.fill.copy(), IFluidHandler.FluidAction.EXECUTE);
                        meltingTank.drain(recipe.drain.copy(), IFluidHandler.FluidAction.EXECUTE);
                        break;
                    }
                }
            }
        }

        public void read(CompoundTag tag) {
            temperatures = tag.getIntArray("temp");
            tempRequired = tag.getIntArray("temp_req");
        }

        public CompoundTag write() {
            CompoundTag tag = new CompoundTag();
            tag.putIntArray("temp", temperatures);
            tag.putIntArray("temp_req", tempRequired);
            return tag;
        }
    }

    //Ticking
    public static class Ticking implements BlockEntityTicker<TileBlastFurnaceController> {

        @Override
        public void tick(Level arg, BlockPos position, BlockState state, TileBlastFurnaceController instance) {
            if (instance.isClientWorld()) return;
            if (instance.tick % 20 == 0) instance.checkStructure();

            if (instance.active) {
                if (instance.tick % 4 == 0) {
                    instance.itemHeat.heat();
                    instance.alloyAlloys();
                    instance.liquidHeat.heat();
                    instance.loseHeat();
                    instance.loseAir();
                }

                if (instance.temperature < MAXIMUM_TEMPERATURE) { //Burn fuel
                    int fluidAmount = instance.airTank.getFluidInTank(instance.airTank.getTanks()).getAmount();
                    if (fluidAmount > 0) {
                        float diff = (float) fluidAmount / (float) instance.airTank.maxCapacity;
                        int burningTickSpeed = 1 + (int) (60 - (60 * diff));
                        if (instance.tick % burningTickSpeed == 0) {
                            instance.burnFuel();
                        }
                    }
                }
            }
            instance.tick++;
        }
    }
}
