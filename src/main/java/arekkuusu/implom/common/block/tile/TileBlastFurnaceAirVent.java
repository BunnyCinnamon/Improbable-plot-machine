package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.helper.WorldHelper;
import arekkuusu.implom.api.multiblock.MultiBlockRectanguloid;
import arekkuusu.implom.common.block.BlockBaseMultiBlock;
import arekkuusu.implom.common.block.fluid.ModFluids;
import arekkuusu.implom.common.block.tile.multiblock.MultiBlockHotAir;
import arekkuusu.implom.common.handler.data.capability.SimpleTank;
import arekkuusu.implom.common.handler.data.capability.provider.CapabilityProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBlastFurnaceAirVent extends TileMultiBlockOniichan implements ITickableTileEntity {

    public static final MultiBlockHotAir MULTI_BLOCK_HOT_AIR = new MultiBlockHotAir(3, MultiBlockRectanguloid.WallType.ANY);

    public final SimpleTank airTank = new SimpleTank(this::airTankChange); // Used to store air fluid, can not drain or fill directly
    public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
            .put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, airTank)
            .build();
    public final FluidStack fillAmount = new FluidStack(ModFluids.HOT_AIR.get(), 0);
    private int tick;

    public TileBlastFurnaceAirVent() {
        super(ModTiles.BLAST_FURNACE_AIR_VENT.get(), MULTI_BLOCK_HOT_AIR);
    }

    @Override
    public void tick() {
        if (isClientWorld()) return;
        if (tick % 20 == 0) checkStructure();

        if (active && fillAmount.getAmount() > 0 && airTank.getFluidInTank(0).getAmount() < airTank.getTankCapacity(0) && tick % 10 == 0) {
            airTank.fill(fillAmount, IFluidHandler.FluidAction.EXECUTE);
        }
        tick = (tick + 1) % 20;
    }

    @Override
    public void updateStructureData() {
        if (structure == null || isActiveLazy()) return;
        int inventorySize = (structure.x) * ((structure.y) - 3) * (structure.z);
        int liquidsSize = inventorySize * 1000;
        int amountFill = (int) MathHelper.clamp((inventorySize / 6F) * 50F, 1F, 100F);
        airTank.setTankCapacity(liquidsSize);
        fillAmount.setAmount(amountFill);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getMultiBlockCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return provider.getCapability(cap, side);
    }

    public boolean isActiveLazy() {
        return WorldHelper.getStateValue(getWorld(), getPos(), BlockBaseMultiBlock.ACTIVE).orElse(false);
    }

    public void airTankChange() {
        markDirty();
        sync();
    }

    /* NBT */
    private static final String TAG_PROVIDER = "provider";
    private static final String TAG_AMOUNT_FILL = "amount";

    @Override
    void load(CompoundNBT compound) {
        super.load(compound);
        provider.deserializeNBT(compound.getCompound(TAG_PROVIDER));
        fillAmount.setAmount(compound.getInt(TAG_AMOUNT_FILL));
    }

    @Override
    void save(CompoundNBT compound) {
        super.save(compound);
        compound.put(TAG_PROVIDER, provider.serializeNBT());
        compound.putInt(TAG_AMOUNT_FILL, fillAmount.getAmount());
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
}
