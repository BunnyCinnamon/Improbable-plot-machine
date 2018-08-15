/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.util;

import javafx.beans.NamedArg;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 30/09/2017.
 * It's distributed as part of Solar.
 */
public class Pair<T> {

	public T l;
	public T r;

	public Pair(){}

	public Pair(@NamedArg("l") T l, @NamedArg("r") T r) {
		this.l = l;
		this.r = r;
	}

	public Pair<T> offer(T t) {
		if(l == null && !equalsR(t)) l = t;
		else if(r == null && !equalsL(t)) r = t;

		return this;
	}

	public Pair<T> remove(T t) {
		if(equalsL(t)) l = null;
		else if(equalsR(t)) r = null;

		return this;
	}

	@Nullable
	public T getInverse(T t) {
		if(equalsL(t)) return r;
		if(equalsR(t)) return l;

		return null;
	}

	@Override
	public int hashCode() {
		return (l != null ? l.hashCode() : 0)+ (r != null ? r.hashCode() : 0);
	}

	public boolean equalsL(T t) {
		return l != null && (l == t || l.equals(t));
	}

	public boolean equalsR(T t) {
		return r != null && (r == t || r.equals(t));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof Pair) {
			Pair pair = (Pair) o;
			return (l != null ? l.equals(pair.l) : pair.l == null) && (r != null ? r.equals(pair.r) : pair.r == null);
		}
		return false;
	}

	public static <T> Pair<T> empty() {
		return new Pair<>(null, null);
	}
}
