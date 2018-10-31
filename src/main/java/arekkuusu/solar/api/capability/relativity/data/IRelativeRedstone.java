/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.relativity.data;

import arekkuusu.solar.api.capability.relativity.RelativityHandler;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 11/12/2017.
 * It's distributed as part of Solar.
 */
public interface IRelativeRedstone extends IRelative {
	String NBT_TAG = "redstone_nbt";

	/**
	 * Default {@link IRelativeRedstone} provider
	 */
	Callable<IRelativeRedstone> DEFAULT = EmptyRedstone::new;

	/**
	 * Updates this tile when the power amount changes
	 */
	void onPowerUpdate();

	default boolean isPowered() {
		return RelativityHandler.isPowered(this);
	}

	default int getPower() {
		return RelativityHandler.getPower(this);
	}

	default void setPower(int power, boolean update) {
		RelativityHandler.setPower(this, power, update);
	}
}

class EmptyRedstone implements IRelativeRedstone {

	private UUID key;

	@Override
	public void onPowerUpdate() {
		//NO-OP
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
	}
}
