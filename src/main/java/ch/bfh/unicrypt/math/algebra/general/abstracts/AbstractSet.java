/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.math.algebra.general.abstracts;

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.CompositeConverter;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.helper.converter.classes.string.BigIntegerToString;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.iterable.IterablePrefix;
import ch.bfh.unicrypt.helper.tree.Leaf;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeSemiGroup;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;
import ch.bfh.unicrypt.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;
import java.util.Iterator;

/**
 * This abstract class provides a base implementation for the interface {@link Set}. Non-abstract sub-classes need to
 * specify the two generic types {@code E} and {@code V} and all abstract methods. In some sub-classes, it might be
 * necessary to override some default methods. Every abstract method has a name starting with {@code abstract...} and
 * every default method has a name starting with {@code default...}.
 * <p>
 * @param <E> Generic type of elements of this set
 * @param <V> Generic type of values stored in the elements of this set
 * @see AbstractElement
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractSet<E extends Element<V>, V extends Object>
	   extends UniCrypt
	   implements Set<V> {

	private static final long serialVersionUID = 1L;

	// the class of the values used to represent elements of this set
	private final Class<?> valueClass;

	// various variables for storing information about the order of this set
	private BigInteger order, lowerBound, upperBound, minimum;

	// the default converters used to convert elements into BigInteger, String, and ByteArray
	private Converter<V, BigInteger> bigIntegerConverter;
	private Converter<V, String> stringConverter;
	private Converter<V, ByteArray> byteArrayConverter;

	protected AbstractSet(Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	@Override
	public final boolean isSemiGroup() {
		return this instanceof SemiGroup;
	}

	@Override
	public final boolean isMonoid() {
		return this instanceof Monoid;
	}

	@Override
	public final boolean isGroup() {
		return this instanceof Group;
	}

	@Override
	public final boolean isSemiRing() {
		return this instanceof SemiRing;
	}

	@Override
	public final boolean isRing() {
		return this instanceof Ring;
	}

	@Override
	public final boolean isField() {
		return this instanceof Field;
	}

	@Override
	public final boolean isCyclic() {
		return this instanceof CyclicGroup;
	}

	@Override
	public final boolean isAdditive() {
		return this instanceof AdditiveSemiGroup;
	}

	@Override
	public final boolean isMultiplicative() {
		return this instanceof MultiplicativeSemiGroup;
	}

	@Override
	public final boolean isConcatenative() {
		return this instanceof ConcatenativeSemiGroup;
	}

	@Override
	public final boolean isProduct() {
		return this instanceof ProductSet;
	}

	@Override
	public final boolean isFinite() {
		return !this.getOrder().equals(Set.INFINITE_ORDER);
	}

	@Override
	public final boolean hasKnownOrder() {
		return !this.getOrder().equals(Set.UNKNOWN_ORDER);
	}

	@Override
	public final BigInteger getOrder() {
		if (this.order == null) {
			this.order = this.abstractGetOrder();
		}
		return this.order;
	}

	@Override
	public final BigInteger getOrderLowerBound() {
		if (this.lowerBound == null) {
			if (this.hasKnownOrder()) {
				this.lowerBound = this.getOrder();
			} else {
				this.lowerBound = this.defaultGetOrderLowerBound();
			}
		}
		return this.lowerBound;
	}

	@Override
	public final BigInteger getOrderUpperBound() {
		if (this.upperBound == null) {
			if (this.hasKnownOrder()) {
				this.upperBound = this.getOrder();
			} else {
				this.upperBound = this.defaultGetOrderUpperBound();
			}
		}
		return this.upperBound;
	}

	@Override
	public final BigInteger getMinimalOrder() {
		if (this.minimum == null) {
			this.minimum = this.defaultGetMinimalOrder();
		}
		return this.minimum;
	}

	@Override
	public final boolean isSingleton() {
		return this.getOrder().equals(BigInteger.ONE);
	}

	@Override
	public ZMod getZModOrder() {
		if (!(this.isFinite() && this.hasKnownOrder())) {
			throw new UnsupportedOperationException();
		}
		return ZMod.getInstance(this.getOrder());
	}

	@Override
	public ZStarMod getZStarModOrder() {
		if (!(this.isFinite() && this.hasKnownOrder())) {
			throw new UnsupportedOperationException();
		}
		return ZStarMod.getInstance(this.getOrder());
	}

	@Override
	public final E getElement(V value) {
		if (!this.contains(value)) {
			throw new IllegalArgumentException();
		}
		return this.abstractGetElement(value);
	}

	@Override
	public final boolean contains(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractContains(value);
	}

	@Override
	public final boolean contains(Element<?> element) {
		if (element == null) {
			throw new IllegalArgumentException();
		}
		if (!this.valueClass.isInstance(element.getValue())) {
			return false;
		}
		return this.defaultContains((Element<V>) element);
	}

	@Override
	public final E getRandomElement() {
		return this.abstractGetRandomElement(HybridRandomByteSequence.getInstance());
	}

	@Override
	public final E getRandomElement(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractGetRandomElement(randomByteSequence);
	}

	@Override
	public final <W> E getElementFrom(W value, Converter<V, W> converter) {
		if (value == null || converter == null) {
			throw new IllegalArgumentException();
		}
		try {
			V convertedValue = converter.reconvert(value);
			return this.abstractGetElement(convertedValue);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public final <W> E getElementFrom(W value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator) {
		if (value == null || convertMethod == null || aggregator == null) {
			throw new IllegalArgumentException();
		}
		try {
			Tree<W> tree = aggregator.disaggregate(value);
			return this.defaultGetElementFrom(tree, convertMethod);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public final <W> E getElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod) {
		if (tree == null || convertMethod == null) {
			throw new IllegalArgumentException();
		}
		try {
			return this.defaultGetElementFrom(tree, convertMethod);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public final E getElementFrom(int value) {
		return this.defaultGetElementFrom(BigInteger.valueOf(value));
	}

	@Override
	public final E getElementFrom(BigInteger value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		try {
			return this.defaultGetElementFrom(value);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public final E getElementFrom(ByteArray value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		try {
			return this.defaultGetElementFrom(value);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public final E getElementFrom(String value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		try {
			return this.defaultGetElementFrom(value);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public final boolean isEquivalent(final Set<?> other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		if (this == other) {
			return true;
		}
		// check if this.getClass() is a superclass of other.getClass()
		if (this.getClass().isAssignableFrom(other.getClass())) {
			return this.defaultIsEquivalent(other);
		}
		// vice versa
		if (other.getClass().isAssignableFrom(this.getClass())) {
			return other.isEquivalent(this);
		}
		return false;
	}

	@Override
	public final Iterable<E> getElements() {
		return new Iterable<E>() {

			@Override
			public Iterator<E> iterator() {
				return defaultGetIterator();
			}

		};
	}

	@Override
	public final Iterable<E> getElements(final int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		return IterablePrefix.getInstance(new Iterable<E>() {

			@Override
			public Iterator<E> iterator() {
				return defaultGetIterator();
			}

		}, n);
	}

	@Override
	public final Class<?> getValueClass() {
		return this.valueClass;
	}

	@Override
	public final int hashCode() {
		int hash = 7;
		hash = 47 * hash + this.valueClass.hashCode();
		hash = 47 * hash + this.getClass().hashCode();
		hash = 47 * hash + this.abstractHashCode();
		return hash;
	}

	@Override
	public final boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		return this.abstractEquals((Set) other);
	}

	protected final <W> Converter<V, W> getConverter(ConvertMethod<W> convertMethod) {
		Converter<V, W> converter = convertMethod.getConverter((Class<V>) this.getValueClass());
		if (converter == null) {
			Class<W> outputClass = convertMethod.getOutputClass();
			if (outputClass == String.class) {
				converter = (Converter<V, W>) this.getStringConverter();
			} else if (outputClass == BigInteger.class) {
				converter = (Converter<V, W>) this.getBigIntegerConverter();
			} else if (outputClass == ByteArray.class) {
				converter = (Converter<V, W>) this.getByteArrayConverter();
			} else {
				throw new IllegalArgumentException();
			}
		}
		return converter;
	}

	protected final Converter<V, BigInteger> getBigIntegerConverter() {
		if (this.bigIntegerConverter == null) {
			this.bigIntegerConverter = this.abstractGetBigIntegerConverter();
		}
		return this.bigIntegerConverter;
	}

	protected final Converter<V, ByteArray> getByteArrayConverter() {
		if (this.byteArrayConverter == null) {
			this.byteArrayConverter = this.defaultGetByteArrayConverter();
		}
		return this.byteArrayConverter;
	}

	protected final Converter<V, String> getStringConverter() {
		if (this.stringConverter == null) {
			this.stringConverter = this.defaultGetStringConverter();
		}
		return this.stringConverter;
	}

	// this method is only called for sets of unknown order
	protected BigInteger defaultGetOrderLowerBound() {
		return BigInteger.ONE;
	}

	// this method is only called for sets of unknown order
	protected BigInteger defaultGetOrderUpperBound() {
		return Set.INFINITE_ORDER;
	}

	// this method is different only for ProductSet
	protected BigInteger defaultGetMinimalOrder() {
		return this.getOrderLowerBound();
	}

	// this method is different only for Subset
	protected boolean defaultContains(final Element<V> element) {
		return this.isEquivalent(element.getSet());
	}

	// this method is overridden in ProductSet
	protected <W> E defaultGetElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod) {
		if (!tree.isLeaf()) {
			throw new IllegalArgumentException();
		}
		W value = ((Leaf<W>) tree).getValue();
		Converter<V, W> converter = this.getConverter(convertMethod);
		return this.abstractGetElement(converter.reconvert(value));
	}

	// this method is overridden in ProductSet
	protected E defaultGetElementFrom(BigInteger value) {
		return this.getElementFrom(value, this.getBigIntegerConverter());
	}

	// this method is overridden in ProductSet
	protected E defaultGetElementFrom(ByteArray value) {
		return this.getElementFrom(value, this.getByteArrayConverter());
	}

	// this method is overridden in ProductSet
	protected E defaultGetElementFrom(String value) {
		return this.getElementFrom(value, this.getStringConverter());
	}

	// this method us usually overriden in classes of value type String
	protected Converter<V, String> defaultGetStringConverter() {
		return CompositeConverter.getInstance(this.getBigIntegerConverter(), BigIntegerToString.getInstance());
	}

	// this method us usually overriden in classes of value type ByteArray
	protected Converter<V, ByteArray> defaultGetByteArrayConverter() {
		return CompositeConverter.getInstance(this.getBigIntegerConverter(), BigIntegerToByteArray.getInstance());
	}

	// isEquivalent is usually the same as equals, but there are a few exceptions
	protected boolean defaultIsEquivalent(Set<?> set) {
		return this.abstractEquals(set);
	}

	// some sets allow a more efficient itertation method than this one
	protected Iterator<E> defaultGetIterator() {
		final AbstractSet<E, V> set = this;
		return new Iterator<E>() {
			private BigInteger counter = BigInteger.ZERO;
			private BigInteger currentValue = BigInteger.ZERO;
			private final BigInteger maxCounter = set.isFinite() ? set.getOrderLowerBound() : null;

			@Override
			public boolean hasNext() {
				return maxCounter != null && counter.compareTo(maxCounter) < 0;
			}

			@Override
			public E next() {
				E element = set.getElementFrom(this.currentValue);
				while (element == null) {
					this.currentValue = this.currentValue.add(BigInteger.ONE);
					element = set.getElementFrom(this.currentValue);
				}
				this.counter = this.counter.add(BigInteger.ONE);
				this.currentValue = this.currentValue.add(BigInteger.ONE);
				return element;
			}

		};
	}

	protected abstract BigInteger abstractGetOrder();

	protected abstract boolean abstractContains(V value);

	protected abstract E abstractGetElement(V value);

	protected abstract E abstractGetRandomElement(RandomByteSequence randomByteSequence);

	protected abstract Converter<V, BigInteger> abstractGetBigIntegerConverter();

	protected abstract boolean abstractEquals(Set set);

	protected abstract int abstractHashCode();

}
