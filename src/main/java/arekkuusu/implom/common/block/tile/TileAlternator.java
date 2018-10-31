/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.relativity.RelativityHandler;
import arekkuusu.implom.api.capability.relativity.data.IRelative;
import arekkuusu.implom.api.capability.relativity.data.RelativeTileWrapper;
import arekkuusu.implom.api.state.State;
import arekkuusu.implom.common.handler.data.WorldAlternatorData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

/*
 * Created by <Arekkuusu> on 23/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileAlternator extends TileRelativeBase {

	private UUID loadKey;

	@Override
	public IRelative createHandler() {
		return new RelativeTileWrapper<TileAlternator>(this) {
			@Override
			public void add() {
				if(!getWorld().isRemote) {
					if(loadKey == null) {
						loadKey = UUID.randomUUID();
						markDirty();
					}
					if(RelativityHandler.addRelative(this)) {
						getKey().ifPresent(key -> TileAlternator.getData(getWorld()).add(key, loadKey));
					}
				}
			}

			@Override
			public void remove() {
				if(!getWorld().isRemote && RelativityHandler.removeRelative(this)) {
					if(loadKey != null) {
						getKey().ifPresent(key -> TileAlternator.getData(getWorld()).remove(key, loadKey));
					}
				}
			}
		};
	}

	public boolean areAllActive() {
		return handler.getKey().map(key -> {
			int size = 0;
			for(IRelative tile : RelativityHandler.getRelatives(key)) {
				if(tile instanceof RelativeTileWrapper && ((RelativeTileWrapper) tile).isLoaded()) ++size;
			}
			int loaded = TileAlternator.getData(world).getSize(key);
			return size == loaded;
		}).orElse(false);
	}

	public boolean isActiveLazy() {
		return getStateValue(State.ACTIVE, pos).orElse(false);
	}

	public static WorldAlternatorData getData(World world) {
		return WorldAlternatorData.get(world);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		super.readNBT(compound);
		loadKey = compound.getUniqueId("loadKey");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		super.writeNBT(compound);
		compound.setUniqueId("loadKey", loadKey);
	}
}
