/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.quantum;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 04/09/2017.
 * It's distributed as part of Solar.
 */
public interface IQuantum {

	/**
	 * Gets the {@link UUID} if it exists
	 *
	 * @return An {@link Optional<UUID>} containing the key
	 */
	Optional<UUID> getKey();

	/**
	 * Sets the {@param key}
	 *
	 * @param key An {@link UUID}
	 */
	void setKey(@Nullable UUID key);
}
