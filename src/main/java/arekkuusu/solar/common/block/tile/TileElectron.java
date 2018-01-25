/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.util.Vector3;
import arekkuusu.solar.api.tool.FixedDamage;
import arekkuusu.solar.api.state.State;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by <Arekkuusu> on 28/12/2017.
 * It's distributed as part of Solar.
 */
public class TileElectron extends TileBase implements ITickable {

	@Override
	public void update() {
		if(!world.isRemote && getPowerLazy() > 0) {
			world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(Vector3.create(pos).add(0.5D).toBlockPos()).grow(1)).forEach(ent -> {
				ent.attackEntityFrom(FixedDamage.ELECTRICITY, getPowerLazy());
			});
		}
	}

	public int getPowerLazy() {
		return getStateValue(State.POWER, pos).orElse(0);
	}

	@Override
	void readNBT(NBTTagCompound compound) {

	}

	@Override
	void writeNBT(NBTTagCompound compound) {

	}
}
