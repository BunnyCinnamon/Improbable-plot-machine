/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.worldaccess.data;

import arekkuusu.implom.api.capability.quantum.IQuantum;
import arekkuusu.implom.api.capability.quantum.QuantumDataHandler;
import arekkuusu.implom.api.capability.quantum.data.WorldAccessData;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 30/10/2018.
 * It's distributed as part of Improbable plot machine.
 */
public interface IWorldAccess extends IQuantum {
	String NBT_TAG = "world_access_nbt";

	/**
	 * Default {@link IWorldAccess} provider
	 */
	Callable<IWorldAccess> DEFAULT = EmptyWorldAccess::new;

	default Optional<WorldAccessData> getData() {
		return getKey().flatMap(key -> QuantumDataHandler.get(WorldAccessData.class, key));
	}

	default Optional<BlockPos> getPos() {
		return getKey().flatMap(key -> QuantumDataHandler.get(WorldAccessData.class, key).map(WorldAccessData::getPos));
	}

	default Optional<EnumFacing> getFacing() {
		return getKey().flatMap(key -> QuantumDataHandler.get(WorldAccessData.class, key).map(WorldAccessData::getFacing));
	}

	default Optional<World> getWorld() {
		return getKey().flatMap(key -> QuantumDataHandler.get(WorldAccessData.class, key).map(WorldAccessData::getWorld));
	}

	default void setPos(@Nullable BlockPos pos) {
		getKey().ifPresent(key -> QuantumDataHandler.getOrCreate(WorldAccessData.class, key).setPos(pos));
	}

	default void setFacing(@Nullable EnumFacing facing) {
		getKey().ifPresent(key -> QuantumDataHandler.getOrCreate(WorldAccessData.class, key).setFacing(facing));
	}

	default void setWorld(@Nullable World world) {
		getKey().ifPresent(key -> QuantumDataHandler.getOrCreate(WorldAccessData.class, key).setWorld(world));
	}
}

class EmptyWorldAccess implements IWorldAccess {

	private UUID key;

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
	}
}