package cinnamon.implom.common.block.tile;

import cinnamon.implom.api.helper.WorldHelper;
import cinnamon.implom.common.handler.data.capability.SimpleTank;
import cinnamon.implom.common.handler.data.capability.provider.CapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;

public class TileBlastFurnacePipe extends TileBase {

    public static final Comparator<IFluidHandler> COMPARATOR = (a, b) -> {
        FluidStack s0 = a.getFluidInTank(0);
        FluidStack s1 = b.getFluidInTank(0);
        int a0 = s0.getAmount();
        int a1 = s1.getAmount();
        return Integer.compare(a0, a1);
    };
    public static final int CRITICAL_AMOUNT = 1000;

    public final SimpleTank airTank = new SimpleTank(this::airTankChange); { airTank.setTankCapacity(1000); }
    public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
            .put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, airTank)
            .build();
    public EnumMap<Direction, LazyOptional<IFluidHandler>> cached = new EnumMap<>(Direction.class);
    public boolean scan = false;
    public boolean load = false;
    public int tick;

    public TileBlastFurnacePipe(BlockPos arg2, BlockState arg3) {
        super(ModTiles.BLAST_FURNACE_PIPE.get(), arg2, arg3);
    }

    public IFluidHandler[] getTransfers(int max) {
        IFluidHandler[] transfers = new IFluidHandler[0];
        for (LazyOptional<IFluidHandler> lazy : cached.values()) {
            if (!lazy.isPresent()) continue; //Woa mama!
            IFluidHandler handler = lazy.orElseThrow(NullPointerException::new);
            for (int i = 0; i < handler.getTanks(); i++) {
                if (handler.isFluidValid(i, airTank.fluid) && handler.getFluidInTank(i).getAmount() < max) {
                    transfers = Arrays.copyOf(transfers, transfers.length + 1);
                    transfers[transfers.length - 1] = handler;
                    break;
                }
            }
        }
        Arrays.sort(transfers, COMPARATOR);
        return transfers;
    }

    public void transfer(IFluidHandler[] handlers, int inserted) {
        for (IFluidHandler handler : handlers) {
            int stored = handler.getFluidInTank(0).getAmount();
            if(stored >= inserted) continue; //Skip if amount is less or equal
            FluidStack stack = new FluidStack(airTank.fluid.getFluid(), (int) (((inserted - stored) / 2) * 0.9));
            if (handler.fill(stack, IFluidHandler.FluidAction.SIMULATE) > 0) {
                inserted -= airTank.drain(handler.fill(stack, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE).getAmount();
            }
            if (inserted <= 0) break; //Exit if amount is depleted
        }
    }

    public void setupConnections() {
        boolean keepScanning = false;
        for (Direction direction : Direction.values()) {
            LazyOptional<IFluidHandler> optional = this.cached.getOrDefault(direction, LazyOptional.empty());
            if (!optional.isPresent()) {
                LazyOptional<IFluidHandler> lazy = WorldHelper.getCapability(getLevel(), getBlockPos().relative(direction), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite());
                if (lazy.isPresent()) {
                    this.cached.put(direction, lazy);
                    this.scan = false;
                } else keepScanning = true;
                lazy.addListener(l -> {
                    this.cached.remove(direction);
                    this.scan = true;
                });
            }
        }
        if(keepScanning && !this.scan) this.scan = true;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return provider.getCapability(cap, side);
    }

    public void airTankChange() {
        setChanged();
        sync();
    }

    /* NBT */
    private static final String TAG_PROVIDER = "provider";

    @Override
    void loadDisk(CompoundTag compound) {
        this.provider.deserializeNBT(compound.getCompound(TAG_PROVIDER));
    }

    @Override
    void saveDisk(CompoundTag compound) {
        compound.put(TAG_PROVIDER, this.provider.serializeNBT());
    }

    public static class Ticking implements BlockEntityTicker<TileBlastFurnacePipe> {

        @Override
        public void tick(Level arg, BlockPos arg2, BlockState arg3, TileBlastFurnacePipe instance) {
            if (instance.isClientWorld()) return;
            if (!instance.load) {
                instance.setupConnections();
                instance.load = true;
            }
            if (instance.scan) instance.setupConnections();
            int amount = instance.airTank.getFluidInTank(instance.airTank.getTanks()).getAmount();
            if (amount > 0 && !instance.cached.isEmpty()) {
                IFluidHandler[] transfers = instance.getTransfers(amount); //Lookup valid targets
                instance.transfer(transfers, amount); //Transfer air to targets
            }

            if (amount >= CRITICAL_AMOUNT) {
                //BAKURETSU LA LA LA
            }
            instance.tick = (instance.tick + 1) % 20;
        }
    }
}
