/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.helper.tree;

import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.iterable.IterableValue;
import java.util.Iterator;

/**
 * An instance of this class represents a leaf of a {@link Tree}. Every leaf stores a value of a generic type {@code V}.
 * The recursive definition of a tree implies that a leaf is a tree on its own.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the value stored in the leaf
 * @see Tree
 * @see Node
 */
public class Leaf<V>
	   extends Tree<V> {

	private final V value;

	private Leaf(V value) {
		this.value = value;
	}

	/**
	 * Creates a new leaf storing a given value.
	 * <p>
	 * @param <V>   The generic type of the given value and the resulting leaf
	 * @param value The given value
	 * @return The new leaf
	 */
	public static <V> Leaf<V> getInstance(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return new Leaf<V>(value);
	}

	/**
	 * Returns the value stored in the leaf.
	 * <p>
	 * @return The value stored in the leaf
	 */
	public V getValue() {
		return this.value;
	}

	@Override
	public V abstractAggregate(Aggregator<V> aggregator) {
		return aggregator.aggregateLeaf(this.value);
	}

	@Override
	public Iterator<V> iterator() {
		return IterableValue.getInstance(this.value).iterator();
	}

	@Override
	protected String defaultToStringContent() {
		return this.value.toString();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + this.value.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Leaf<?> other = (Leaf<?>) obj;
		return this.value.equals(other.value);
	}

}