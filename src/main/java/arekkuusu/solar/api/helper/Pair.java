/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.helper;

import javafx.beans.NamedArg;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 30/09/2017.
 * It's distributed as part of Solar.
 */
public class Pair<T> {

	private T l;
	private T r;

	public Pair(){}

	public Pair(@NamedArg("l") T l, @NamedArg("r") T r) {
		this.l = l;
		this.r = r;
	}

	public Pair<T> offer(T t) {
		if(l == null && !equalsR(t)) setL(t);
		else if(r == null && !equalsL(t)) setR(t);

		return this;
	}

	public Pair<T> remove(T t) {
		if(equalsL(t)) setL(null);
		else if(equalsR(t)) setR(null);

		return this;
	}

	@Nullable
	public T getInverse(T t) {
		if(equalsL(t)) return r;
		if(equalsR(t)) return l;

		return null;
	}

	@Nullable
	public T getL() {
		return l;
	}

	public void setL(@Nullable T l) {
		this.l = l;
	}

	@Nullable
	public T getR() {
		return r;
	}

	public void setR(@Nullable T r) {
		this.r = r;
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
}
