/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.relativity.IRelativeTile;
import arekkuusu.solar.api.entanglement.relativity.RelativityHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public class TileBlinker extends TileBase implements ITickable, IRelativeTile {

	private static final Map<UUID, Integer> POWER_MAP = new HashMap<>();
	private UUID key;

	public static boolean isPowered(TileBlinker tile) {
		return getPower(tile) > 0;
	}

	public static int getPower(TileBlinker tile) {
		return POWER_MAP.getOrDefault(tile.getKey().orElse(null), 0);
	}

	public static void setPower(TileBlinker tile, int newPower) {
		tile.getKey().ifPresent(uuid -> POWER_MAP.compute(uuid, (key, prevPower) -> {
			if(prevPower == null || prevPower != newPower) updateAll(key);
			return newPower > 0 ? newPower : null;
		}));
	}

	private static void updateAll(UUID uuid) {
		RelativityHandler.getRelativityMap().get(uuid)
				.stream().filter(tile -> tile.isLoaded() && tile instanceof TileBlinker )
				.map(tile -> (TileBlinker) tile ).forEach(TileBlinker::updateState);
	}

	private void updateState() {
		IBlockState state = getWorld().getBlockState(getPos());
		getWorld().scheduleUpdate(getPos(), state.getBlock(), 0);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			add();
		}
	}

	@Override
	public void add() {
		RelativityHandler.addRelative(this, TileBlinker::updateState);
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		if(key == null) remove();
		this.key = key;
		updateState();
	}

	@Override
	void readNBT(NBTTagCompound cmp) {
		if(cmp.hasUniqueId("uuid_key")) {
			key = cmp.getUniqueId("uuid_key");
		}
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
		if(key != null) {
			cmp.setUniqueId("uuid_key", key);
		}
	}
}
