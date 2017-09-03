/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.quantum.IEntangledTile;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.handler.data.QuantumTileWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public class TileQuantumMirror extends TileBase implements ITickable, IEntangledTile<TileQuantumMirror> {

	private final QuantumTileWrapper<TileQuantumMirror> handler;
	private UUID key;
	public int tick;

	public TileQuantumMirror() {
		handler = new QuantumTileWrapper<>(this);
	}

	@Override
	public void update() {
		if(world.isRemote && world.rand.nextInt(10) == 0) {
			double x = pos.getX() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double y = pos.getY() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double z = pos.getZ() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double speed = 0.05D;

			ParticleUtil.spawnQuorn(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, speed, x, y, z, 0.1F, 0XFFFFFF);
		}
		++tick;
	}

	@Nullable
	@Override
	public UUID getKey() {
		return key;
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
		IBlockState state = world.getBlockState(pos);
		world.notifyNeighborsOfStateChange(pos, state.getBlock(), true);
	}

	@Override
	public void readNBT(NBTTagCompound compound) {
		if(compound.hasUniqueId("key")) {
			key = compound.getUniqueId("key");
		}
	}

	@Override
	public void writeNBT(NBTTagCompound compound) {
		if(key != null) {
			compound.setUniqueId("key", key);
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
}
