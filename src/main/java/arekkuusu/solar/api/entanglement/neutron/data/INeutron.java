/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.neutron.data;

import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public interface INeutron {
	Callable<INeutron> DEFAULT = Empty::new;
	int get();
	void set(int neutrons);
}
class Empty implements INeutron {

	private int neutrons;

	@Override
	public int get() {
		return neutrons;
	}

	@Override
	public void set(int neutrons) {
		this.neutrons = neutrons;
	}
}
