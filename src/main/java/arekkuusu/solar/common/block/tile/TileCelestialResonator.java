/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.state.MoonPhase;
import arekkuusu.solar.api.state.State;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 29/12/2017.
 * It's distributed as part of Solar.
 */
public class TileCelestialResonator extends TileBase implements ITickable {

	@Override
	public void update() {
		MoonPhase currentPhase = getPhaseLazy();
		MoonPhase newPhase = MoonPhase.getMoonPhase(world);
		if(currentPhase != newPhase) {
			IBlockState state = world.getBlockState(pos);
			world.setBlockState(pos, state.withProperty(MoonPhase.MOON_PHASE, newPhase).withProperty(State.ACTIVE, true));
			world.scheduleUpdate(pos, state.getBlock(), 1);
		}
	}

	@Nullable
	public MoonPhase getPhaseLazy() {
		return getStateValue(MoonPhase.MOON_PHASE, pos).orElse(null);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		//Nothing
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		//Nothing
	}
}
