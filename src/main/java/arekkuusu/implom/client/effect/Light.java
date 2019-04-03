/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

/**
 * Created by <Arekkuusu> on 01/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public enum Light {
	DULL(false),
	GLOW(true);

	public final boolean additive;

	Light(boolean additive) {
		this.additive = additive;
	}
}
