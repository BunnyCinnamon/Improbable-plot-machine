/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.state.Power;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.network.PacketHandler;
import arekkuusu.solar.common.network.PhenomenaMessage;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 08/09/2017.
 * It's distributed as part of Solar.
 */
public class TilePhenomena extends TileBase implements ITickable {

	private static final ResourceLocation ASM = new ResourceLocation(LibMod.MOD_ID, "armatures/phenomena_asm.json");

	private final IAnimationStateMachine animation;
	private boolean powered;
	private boolean inverse;
	public int timer;

	public TilePhenomena() {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			animation = ModelLoaderRegistry.loadASM(ASM, ImmutableMap.of());
		} else {
			animation = null;
		}
	}

	@Override
	public void update() {
		if(hasCooldown()) {
			--timer;
			if(world.isRemote) {
				if(timer <= 0) {
					animation.transition("default");
				}
			} else {
				if(inverse && timer == 5) inverse();
				if(timer == 10) propagate();
			}
			markDirty();
		}
	}

	public void makePhenomenon() {
		if(!hasCooldown()) {
			if(world.isRemote) {
				animation.transition(isInvisible() ? "grow" : "shrink");
			} else {
				inverse = isInvisible();
				if(!inverse) inverse();
				PacketHandler.sendToAllAround(new PhenomenaMessage(getPos()), PacketHandler.fromTileEntity(this, 25));
			}
			timer = 20;
		}
	}

	private void inverse() {
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(Power.POWER, state.getValue(Power.POWER).inverse()));
	}

	private void propagate() {
		for(EnumFacing facing : EnumFacing.values()) {
			BlockPos offset = pos.offset(facing);
			IBlockState state = world.getBlockState(offset);
			if(state.getBlock() == ModBlocks.phenomena) {
				TilePhenomena phenomena = (TilePhenomena) world.getTileEntity(offset);
				if(phenomena != null) {
					phenomena.makePhenomenon();
				}
			}
		}
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
	}

	public boolean hasCooldown() {
		return timer > 0;
	}

	public boolean isInvisible() {
		IBlockState state = world.getBlockState(pos);
		return state.getValue(Power.POWER) == Power.OFF;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityAnimation.ANIMATION_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityAnimation.ANIMATION_CAPABILITY ? CapabilityAnimation.ANIMATION_CAPABILITY.cast(animation) : null;
	}

	@Override
	public boolean hasFastRenderer() {
		return true;
	}

	@Override
	void readNBT(NBTTagCompound cmp) {
		timer = cmp.getInteger("timer");
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
		cmp.setInteger("timer", timer);
	}
}
