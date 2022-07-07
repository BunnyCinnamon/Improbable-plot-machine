package cinnamon.implom.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public abstract class TileBase extends BlockEntity {

    public TileBase(BlockEntityType<?> type, BlockPos arg2, BlockState arg3) {
        super(type, arg2, arg3);
    }

    @Nonnull
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    public boolean isClientWorld() {
        return getLevel().isClientSide;
    }

    public final void sync() {
        if (hasLevel() && !isClientWorld()) {
            BlockState state = getLevel().getBlockState(getBlockPos());
            getLevel().sendBlockUpdated(getBlockPos(), state, state, 2);
        }
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if(pkt.getTag() != null) {
            readSync(pkt.getTag());
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag arg) {
        super.load(arg);
        loadDisk(arg);
    }

    @Override
    protected void saveAdditional(CompoundTag arg) {
        super.saveAdditional(arg);
        saveDisk(arg);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        writeSync(updateTag);
        return updateTag;
    }

    void saveDisk(CompoundTag compound) {

    }

    void loadDisk(CompoundTag compound) {

    }

    void readSync(CompoundTag compound) {

    }

    void writeSync(CompoundTag compound) {

    }
}
