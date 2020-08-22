package arekkuusu.implom.common.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileBase extends TileEntity {

    public TileBase(TileEntityType<?> type) {
        super(type);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        this.readSync(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        this.writeSync(tag);
        return new SUpdateTileEntityPacket(getPos(), 0, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        super.write(tag);
        return tag;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        load(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        save(tag);
        return tag;
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public World getWorld() {
        return super.getWorld();
    }

    public boolean isClientWorld() {
        return getWorld().isRemote();
    }

    public final void sync() {
        if (hasWorld() && !getWorld().isRemote()) {
            BlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 2);
        }
    }

    void writeSync(CompoundNBT compound) {
        save(compound);
    }

    @OnlyIn(Dist.CLIENT)
    void readSync(CompoundNBT compound) {
        load(compound);
    }

    void load(CompoundNBT compound) {
        //ON RENT
    }

    void save(CompoundNBT compound) {
        //ON RENT
    }
}
