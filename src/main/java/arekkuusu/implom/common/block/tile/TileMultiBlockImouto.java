package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.multiblock.MultiBlockImouto;
import arekkuusu.implom.api.multiblock.MultiBlockOniichan;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class TileMultiBlockImouto extends TileBase implements MultiBlockImouto {

    public boolean hasOniichanByHerSide;
    public BlockPos oniichan;
    public Block oniichanBlock;
    public BlockState oniichanState;

    public TileMultiBlockImouto(TileEntityType<?> type) {
        super(type);
    }

    public TileMultiBlockImouto() {
        this(ModTiles.IMOUTO.get());
    }

    @Override
    public void wakeUpOniichan() {
        if (this.hasOniichanByHerSide) {
            TileEntity tile = getWorld().getTileEntity(oniichan);
            if (tile instanceof MultiBlockOniichan) {
                ((MultiBlockOniichan) tile).okaeriOniichan();
            }
        }
    }

    @Override
    public void overrideOniichan(BlockPos pos) {
        this.oniichan = pos;
        this.oniichanState = getWorld().getBlockState(this.oniichan);
        this.oniichanBlock = this.oniichanState.getBlock();
        this.hasOniichanByHerSide = true;
        markDirty();
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
        markDirty();
        sync();
    }

    @Override
    public boolean hasValidOniichan() {
        return this.hasOniichanByHerSide && hasWorld() && this.getWorld().getBlockState(this.oniichan) == this.oniichanState
                && (this.getWorld().getBlockState(this.oniichan).getBlock() == this.oniichanBlock);
    }

    @Override
    public BlockPos getOniichan() {
        return this.oniichan;
    }

    @Override
    void load(CompoundNBT compound) {
        this.hasOniichanByHerSide = compound.getBoolean("hasOniichanByHerSide");
        if (this.hasOniichanByHerSide) {
            int xCenter = compound.getInt("xCenter");
            int yCenter = compound.getInt("yCenter");
            int zCenter = compound.getInt("zCenter");
            this.oniichan = new BlockPos(xCenter, yCenter, zCenter);
            this.oniichanBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("oniichanBlock")));
            this.oniichanState = Block.getStateById(compound.getInt("oniichanState"));
        }
    }

    @Override
    void save(CompoundNBT compound) {
        compound.putBoolean("hasOniichanByHerSide", this.hasOniichanByHerSide);
        if (this.hasOniichanByHerSide) {
            compound.putInt("xCenter", this.oniichan.getX());
            compound.putInt("yCenter", this.oniichan.getY());
            compound.putInt("zCenter", this.oniichan.getZ());
            compound.putString("oniichanBlock", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(this.oniichanBlock)).toString());
            compound.putInt("oniichanState", Block.getStateId(this.oniichanState));
        }
    }
}
