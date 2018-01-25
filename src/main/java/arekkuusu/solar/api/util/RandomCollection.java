/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by <Arekkuusu> on 25/11/2017.
 * It's distributed as part of Solar.
 */
public class RandomCollection<E> {
	private final NavigableMap<Double, E> map = new TreeMap<>();
	private final Random random;
	private double total = 0;

	public RandomCollection() {
		this(new Random());
	}

	public RandomCollection(Random random) {
		this.random = random;
	}

	public RandomCollection<E> add(double weight, E result) {
		if (weight <= 0) return this;
		total += weight;
		map.put(total, result);
		return this;
	}

	public E next() {
		double value = random.nextDouble() * total;
		return map.higherEntry(value).getValue();
	}

	public void setSeed(long seed) {
		this.random.setSeed(seed);
	}
}
