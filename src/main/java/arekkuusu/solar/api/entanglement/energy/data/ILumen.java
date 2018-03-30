/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.energy.data;

import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public interface ILumen {
	Callable<ILumen> DEFAULT = Empty::new;
	int get();
	void set(int neutrons);
	int drain(int amount);
	int fill(int amount);
}
class Empty implements ILumen {

	private int neutrons;

	@Override
	public int get() {
		return neutrons;
	}

	@Override
	public void set(int neutrons) {
		this.neutrons = neutrons;
	}

	@Override
	public int drain(int amount) {
		if(amount > 0) {
			int contained = get();
			int drained = amount < Integer.MAX_VALUE ? amount : Integer.MAX_VALUE;
			int remain = contained;
			int removed = remain < drained ? contained : drained;
			remain -= removed;
			set(remain);
			return removed;
		} else return 0;
	}

	@Override
	public int fill(int amount) {
		if(amount > 0) {
			int contained = get();
			int sum = contained + amount;
			int remain = 0;
			set(sum);
			return remain;
		} else return 0;
	}
}
