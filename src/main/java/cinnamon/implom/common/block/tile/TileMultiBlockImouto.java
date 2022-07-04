package cinnamon.implom.common.block.tile;

import cinnamon.implom.api.multiblock.MultiBlockImouto;
import cinnamon.implom.api.multiblock.MultiBlockOniichan;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class TileMultiBlockImouto extends TileBase implements MultiBlockImouto {

    public boolean hasOniichanByHerSide;
    public BlockPos oniichan;
    public Block oniichanBlock;
    public BlockState oniichanState;

    public TileMultiBlockImouto(BlockEntityType<?> type, BlockPos arg2, BlockState arg3) {
        super(type, arg2, arg3);
    }

    public TileMultiBlockImouto(BlockPos arg2, BlockState arg3) {
        this(ModTiles.IMOUTO.get(), arg2, arg3);
    }

    @Override
    public void wakeUpOniichan() {
        if (this.hasOniichanByHerSide) {
            BlockEntity tile = getLevel().getBlockEntity(oniichan);
            if (tile instanceof MultiBlockOniichan) {
                ((MultiBlockOniichan) tile).okaeriOniichan();
            }
        }
    }

    @Override
    public void overrideOniichan(BlockPos pos) {
        this.oniichan = pos;
        this.oniichanState = getLevel().getBlockState(this.oniichan);
        this.oniichanBlock = this.oniichanState.getBlock();
        this.hasOniichanByHerSide = true;
        setChanged();
        sync();
    }

    @Override
    public boolean setOniichan(BlockPos pos) {
        if (!hasValidOniichan()) {
            this.overrideOniichan(pos);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removeOniichan() {
        this.oniichan = null;
        this.oniichanState = null;
        this.oniichanBlock = null;
        this.hasOniichanByHerSide = false;
        setChanged();
        sync();
    }

    @Override
    public boolean hasValidOniichan() {
        return this.hasOniichanByHerSide && hasLevel() && this.getLevel().getBlockState(this.oniichan) == this.oniichanState
                && (this.getLevel().getBlockState(this.oniichan).getBlock() == this.oniichanBlock);
    }

    @Override
    public BlockPos getOniichan() {
        return this.oniichan;
    }

    @Override
    void saveDisk(CompoundTag compound) {
        compound.putBoolean("hasOniichanByHerSide", this.hasOniichanByHerSide);
        if (this.hasOniichanByHerSide) {
            compound.putInt("xCenter", this.oniichan.getX());
            compound.putInt("yCenter", this.oniichan.getY());
            compound.putInt("zCenter", this.oniichan.getZ());
            compound.putString("oniichanBlock", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(this.oniichanBlock)).toString());
            compound.putInt("oniichanState", Block.getId(this.oniichanState));
        }
    }

    @Override
    void loadDisk(CompoundTag compound) {
        this.hasOniichanByHerSide = compound.getBoolean("hasOniichanByHerSide");
        if (this.hasOniichanByHerSide) {
            int xCenter = compound.getInt("xCenter");
            int yCenter = compound.getInt("yCenter");
            int zCenter = compound.getInt("zCenter");
            this.oniichan = new BlockPos(xCenter, yCenter, zCenter);
            this.oniichanBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("oniichanBlock")));
            this.oniichanState = Block.stateById(compound.getInt("oniichanState"));
        }
    }
}
