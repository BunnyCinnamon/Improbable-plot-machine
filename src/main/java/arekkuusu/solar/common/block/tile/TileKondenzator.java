/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import arekkuusu.solar.api.entanglement.energy.data.LumenStackProvider;
import net.minecraft.block.BlockDirectional;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Solar.
 */
public class TileKondenzator extends TileLumenBase implements ITickable {

	public static final int MAX_LUMEN = 100;

	@Override
	public void update() {
		if(!world.isRemote && world.getTotalWorldTime() % 20 == 0 && handler.get() > 0) handler.set(handler.get() - 1);
	}

	public boolean add(ItemStack stack) {
		if(stack.hasCapability(LumenStackProvider.NEUTRON_CAPABILITY, null)) {
			if(!world.isRemote) {
				ILumen lumen = stack.getCapability(LumenStackProvider.NEUTRON_CAPABILITY, null);
				assert lumen != null;
				int missing = MAX_LUMEN - handler.get();
				if(missing > 0 && lumen.drain(missing, false) > 0) {
					handler.fill(lumen.drain(missing, true), true);
					if(lumen.get() <= 0) stack.shrink(1);
				}
			}
			return true;
		} else return false;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	void writeSync(NBTTagCompound compound) {
		compound.setInteger(ILumen.NBT_TAG, handler.get());
	}

	@Override
	void readSync(NBTTagCompound compound) {
		handler.set(compound.getInteger(ILumen.NBT_TAG));
	}

	@Override
	void onLumenChange() {
		if(!world.isRemote) sync();
	}

	@Override
	public int getCapacity() {
		return MAX_LUMEN;
	}

	@SideOnly(Side.CLIENT)
	public int getLumen() {
		return handler.get();
	}
}
