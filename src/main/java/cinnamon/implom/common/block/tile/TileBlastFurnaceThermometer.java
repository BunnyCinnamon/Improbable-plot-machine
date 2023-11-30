package cinnamon.implom.common.block.tile;

import cinnamon.implom.api.helper.WorldHelper;
import cinnamon.implom.api.multiblock.MultiBlockRectanguloid;
import cinnamon.implom.common.block.BlockBaseMultiBlock;
import cinnamon.implom.common.block.fluid.ModFluids;
import cinnamon.implom.common.block.tile.multiblock.MultiBlockHotAir;
import cinnamon.implom.common.handler.data.capability.SimpleTank;
import cinnamon.implom.common.handler.data.capability.provider.CapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBlastFurnaceThermometer extends TileMultiBlockOniichan {

    public static final MultiBlockHotAir MULTI_BLOCK_HOT_AIR = new MultiBlockHotAir(3, MultiBlockRectanguloid.WallType.ANY);

    public final SimpleTank airTank = new SimpleTank(this::airTankChange); // Used to store air fluid, can not drain or fill directly
    public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
            .put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, airTank)
            .build();
    public final FluidStack fillAmount = new FluidStack(ModFluids.HOT_AIR.get(), 0);
    private int tick;

    public TileBlastFurnaceThermometer(BlockPos arg2, BlockState arg3) {
        super(ModTiles.BLAST_FURNACE_AIR_VENT.get(), MULTI_BLOCK_HOT_AIR, arg2, arg3);
    }

    @Override
    public void updateStructureData() {
        if (structure == null || isActiveLazy()) return;
        int inventorySize = (structure.x) * ((structure.y) - 3) * (structure.z);
        int liquidsSize = inventorySize * 1000;
        int amountFill = (int) Mth.clamp((inventorySize / 6F) * 50F, 1F, 100F);
        airTank.setTankCapacity(liquidsSize);
        fillAmount.setAmount(amountFill);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getMultiBlockCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return provider.getCapability(cap, side);
    }

    public boolean isActiveLazy() {
        return WorldHelper.getStateValue(getLevel(), getBlockPos(), BlockBaseMultiBlock.ACTIVE).orElse(false);
    }

    public void airTankChange() {
        setChanged();
        sync();
    }

    /* NBT */
    private static final String TAG_PROVIDER = "provider";
    private static final String TAG_AMOUNT_FILL = "amount";

    @Override
    void loadDisk(CompoundTag compound) {
        super.loadDisk(compound);
        provider.deserializeNBT(compound.getCompound(TAG_PROVIDER));
        fillAmount.setAmount(compound.getInt(TAG_AMOUNT_FILL));
    }

    @Override
    void saveDisk(CompoundTag compound) {
        super.saveDisk(compound);
        compound.put(TAG_PROVIDER, provider.serializeNBT());
        compound.putInt(TAG_AMOUNT_FILL, fillAmount.getAmount());
    }

    @Override
    void readSync(CompoundTag compound) {
        super.readSync(compound);
        provider.deserializeNBT(compound.getCompound(TAG_PROVIDER));
    }

    @Override
    void writeSync(CompoundTag compound) {
        super.writeSync(compound);
        compound.put(TAG_PROVIDER, provider.serializeNBT());
    }

    public static class Ticking implements BlockEntityTicker<TileBlastFurnaceThermometer> {

        @Override
        public void tick(Level arg, BlockPos arg2, BlockState arg3, TileBlastFurnaceThermometer instance) {
            if (instance.isClientWorld()) return;
            if (instance.tick % 20 == 0) instance.checkStructure();

            if (instance.active && instance.fillAmount.getAmount() > 0 && instance.airTank.getFluidInTank(0).getAmount() < instance.airTank.getTankCapacity(0) && instance.tick % 10 == 0) {
                instance.airTank.fill(instance.fillAmount, IFluidHandler.FluidAction.EXECUTE);
            }
            instance.tick = (instance.tick + 1) % 20;
        }
    }
}
