package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.IPM;
import arekkuusu.implom.api.API;
import arekkuusu.implom.api.helper.BiomeTemperatureHelper;
import arekkuusu.implom.api.helper.WorldHelper;
import arekkuusu.implom.api.multiblock.IMultiblockOniichan;
import arekkuusu.implom.api.multiblock.MultiblockRectanguloid;
import arekkuusu.implom.api.recipe.*;
import arekkuusu.implom.common.block.BlockBaseMultiBlock;
import arekkuusu.implom.common.block.tile.multiblock.MultiBlockBlastFurnace;
import arekkuusu.implom.common.handler.data.capability.BlastFurnaceAirTank;
import arekkuusu.implom.common.handler.data.capability.BlastFurnaceMeltingTank;
import arekkuusu.implom.common.handler.data.capability.provider.CapabilityProvider;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TileBlastFurnaceController extends TileMultiblockOniichan implements IMultiblockOniichan, ITickable {

    public static final MultiBlockBlastFurnace MULTI_BLOCK_BLAST_FURNACE = new MultiBlockBlastFurnace(10, MultiblockRectanguloid.WallType.ANY);
    public static final int ALLOYING_PER_TICK = 10; // how much liquid can be created per tick to make alloys
    public static final int MAXIMUM_TEMPERATURE = 5000; // how much temperature can the structure withhold
    public static final int TIME_FACTOR = 8; // basically an "accuracy" so the heat can be more fine grained. required temp is multiplied by this

    public final BlastFurnaceAirTank airTank = new BlastFurnaceAirTank(this); // Used to store air fluid, can not drain or fill directly
    public final BlastFurnaceMeltingTank meltingTank = new BlastFurnaceMeltingTank(this);
    public final ItemStackHandler fuelStackHandler = new ItemStackHandler(0) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            sync();
        }
    };
    public final ItemStackHandler oreStackHandler = new ItemStackHandler(0) {
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (isClientWorld()) return;
            itemHeat.updateHeatRequired(slot);
            markDirty();
            sync();
        }
    };
    public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
            .put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, meltingTank)
            .put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, fuelStackHandler)
            .put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, oreStackHandler)
            .build();
    public LiquidHeat liquidHeat;
    public ItemHeat itemHeat;
    public int temperature;

    public TileBlastFurnaceController() {
        super(ModTiles.IMOUTO.get(), MULTI_BLOCK_BLAST_FURNACE);
    }

    @Override
    public void tick() {
        if (isClientWorld()) return;
        if (tick % 20 == 0) checkStructure();

        if (active) {
            if (tick % 4 == 0) {
                itemHeat.heat();
                alloyAlloys();
                liquidHeat.heat();
                loseHeat();
            }

            if (temperature < MAXIMUM_TEMPERATURE) { //Burn fuel
                int fluidAmount = airTank.getFluidInTank(0).getAmount();
                if (fluidAmount > 0) {
                    float diff = (float) fluidAmount / (float) airTank.maxCapacity;
                    int burningTickSpeed = 1 + (int) (60 - (60 * diff));
                    if (tick % burningTickSpeed == 0) {
                        burnFuel();
                    }
                }
            }
        }
        tick = (tick + 1) % 20;
    }

    private void burnFuel() {
        int fluidAmount = airTank.getFluidInTank(0).getAmount();
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

    public void meltingTankChange(List<FluidStack> old, List<FluidStack> updated, FluidStack changed) {
        if (liquidHeat.temperatures.length == 0)
            liquidHeat.resize(updated.size());
        int index = old.indexOf(changed);
        if (index == -1)
            index = updated.indexOf(changed);
        liquidHeat.updateHeatRequired(index);
        liquidHeat.resize(updated.size());
        markDirty();
        sync();
    }

    public void airTankChange() {
        markDirty();
        sync();
    }

    @Override
    public void updateStructureData() {
        if (structure == null || isActiveLazy()) return;
        int inventorySize = (structure.x) * (structure.y) * (structure.z);
        if (!isClientWorld() && oreStackHandler.getSlots() > inventorySize) {
            for (int i = inventorySize; i < oreStackHandler.getSlots(); i++) {
                if (!oreStackHandler.getStackInSlot(i).isEmpty()) {
                    dropItem(oreStackHandler.getStackInSlot(i));
                }
            }
        }
        int liquidsSize = inventorySize * 1000;
        fuelStackHandler.setSize(1 + inventorySize % 5);
        oreStackHandler.setSize(inventorySize);
        meltingTank.setTankCapacity(liquidsSize);
        meltingTank.fluids.clear();
        itemHeat.resize(inventorySize);
        liquidHeat.resize(0);
        markDirty();
    }

    public void dropItem(ItemStack stack) {
        BlockPos pos = getPos().offset(getFacingLazy());
        ItemEntity entityItem = new ItemEntity(getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
        getWorld().addEntity(entityItem);
    }

    public Direction getFacingLazy() {
        return WorldHelper.getStateValue(getWorld(), getPos(), HorizontalBlock.HORIZONTAL_FACING).orElse(Direction.NORTH);
    }

    public boolean isActiveLazy() {
        return WorldHelper.getStateValue(getWorld(), getPos(), BlockBaseMultiBlock.ACTIVE).orElse(false);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return active && structureMinPos != null && structureMaxPos != null
                && structureMinPos != BlockPos.ZERO && structureMaxPos != BlockPos.ZERO
                ? new AxisAlignedBB(structureMinPos, structureMaxPos)
                : super.getRenderBoundingBox();
    }

    /* NBT */
    private static final String TAG_PROVIDER = "provider";
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_ITEM_TEMPERATURES = "itemTemperatures";
    private static final String TAG_LIQUID_TEMPERATURES = "liquidTemperatures";

    @Override
    void load(CompoundNBT compound) {
        super.load(compound);
        provider.deserializeNBT(compound.getCompound(TAG_PROVIDER));
        temperature = compound.getInt(TAG_TEMPERATURE);
        itemHeat.read(compound.getCompound(TAG_ITEM_TEMPERATURES));
        liquidHeat.read(compound.getCompound(TAG_LIQUID_TEMPERATURES));
    }

    @Override
    void save(CompoundNBT compound) {
        super.save(compound);
        compound.put(TAG_PROVIDER, provider.serializeNBT());
        compound.putInt(TAG_TEMPERATURE, temperature);
        compound.put(TAG_ITEM_TEMPERATURES, itemHeat.write());
        compound.put(TAG_LIQUID_TEMPERATURES, liquidHeat.write());
    }

    @Override
    void readSync(CompoundNBT compound) {
        super.readSync(compound);
        provider.deserializeNBT(compound.getCompound(TAG_PROVIDER));
    }

    @Override
    void writeSync(CompoundNBT compound) {
        super.writeSync(compound);
        compound.put(TAG_PROVIDER, provider.serializeNBT());
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
									/*temperatures[i] = 0;
									tempRequired[i] = 0;*/
                                    updateHeatRequired(i);
                                }
                            } else {
                                int heatedItem = temperature / 100;
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
            return tempRequired[index] / TIME_FACTOR;
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
                    if (recipe.isMatch(stack)) {
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
                if (recipe.isMatch(stack)) {
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

        public void read(CompoundNBT tag) {
            temperatures = tag.getIntArray("temp");
            tempRequired = tag.getIntArray("temp_req");
        }

        public CompoundNBT write() {
            CompoundNBT tag = new CompoundNBT();
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
                                int heatedItem = temperature / 500;
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
            return tempRequired[index] / TIME_FACTOR;
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

        public void read(CompoundNBT tag) {
            temperatures = tag.getIntArray("temp");
            tempRequired = tag.getIntArray("temp_req");
        }

        public CompoundNBT write() {
            CompoundNBT tag = new CompoundNBT();
            tag.putIntArray("temp", temperatures);
            tag.putIntArray("temp_req", tempRequired);
            return tag;
        }
    }
}
