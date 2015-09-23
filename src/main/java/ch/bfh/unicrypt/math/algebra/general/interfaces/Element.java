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
package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import java.math.BigInteger;

/**
 * This interface represents the concept of an element of a mathematical set. Each instance of {@link Element} is linked
 * to a (unique) instance of {@link Set}. In case the same element is contained in more than one set, multiple instances
 * need to be created, one for each set membership. In other words, sets in UniCrypt are treated as being disjoint,
 * which is not true in a strict mathematical sense.
 * <p>
 * Internally, each element is represented by value of the generic type {@code V}. Elements are usually constructed by
 * specifying this value.
 * <p>
 * For improved convenience, several pairs of equivalent methods exist for {@link Set} and {@link Element} and for
 * corresponding sub-interfaces. This allows both set-oriented and element-oriented writing of code.
 * <p>
 * @param <V> The generic type of values stored in this element
 * @see Set
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Element<V> {

	/**
	 * Returns {@code true} if this element is an {@link AdditiveElement}.
	 * <p>
	 * @return {
	 * @true} if this element is an AdditiveElement
	 */
	public boolean isAdditive();

	/**
	 * Returns {@code true} if this element is a {@link MultiplicativeElement}.
	 * <p>
	 * @return {
	 * @true} if this element is a MultiplicativeElement
	 */
	public boolean isMultiplicative();

	/**
	 * Returns {@code true} if this element is a {@link ConcatenativeElement}.
	 * <p>
	 * @return {
	 * @true} if this element is a ConcatenativeElement
	 */
	public boolean isConcatenative();

	/**
	 * Returns {@code true} if this element is a {@link DualisticElement}.
	 * <p>
	 * @return {@code true} if this element is a DualisticElement
	 */
	public boolean isDualistic();

	/**
	 * Returns {@code true} if this element is a {@link Tuple}.
	 * <p>
	 * @return {@code true} if this element is a Tuple
	 */
	public boolean isTuple();

	/**
	 * Returns the unique {@link Set} to which this element belongs.
	 * <p>
	 * @return The element's set
	 */
	public Set<V> getSet();

	/**
	 * Returns the positive BigInteger value that corresponds this element.
	 * <p>
	 * @return The corresponding BigInteger value
	 */
	public V getValue();

	public <W> W convertTo(Converter<V, W> converter);

	public <W> W convertTo(ConvertMethod<W> convertMethod, Aggregator<W> aggregator);

	public <W, X> X convertTo(ConvertMethod<W> convertMethod, Aggregator<W> aggregator, Converter<W, X> finalConverter);

	public <W> Tree<W> convertTo(ConvertMethod<W> convertMethod);

	public BigInteger convertToBigInteger();

	public ByteArray convertToByteArray();

	public String convertToString();

	public ByteArray getHashValue();

	public <W> ByteArray getHashValue(ConvertMethod<W> convertMethod, HashMethod<W> hashMethod);

	/**
	 * Checks if this element is mathematically equivalent to the given element. For this, they need to belong to
	 * equivalent sets and their values must be equal.
	 * <p>
	 * @param element The given Element
	 * @return {@code true} if the element is equivalent to the given element
	 */
	public boolean isEquivalent(Element element);

	public Element<V> apply(Element element);

	/**
	 * @param amount
	 * @return @see Group#selfApply(Element, long)
	 */
	public Element<V> selfApply(long amount);

	/**
	 * @param amount
	 * @return @see Group#selfApply(Element, BigInteger)
	 */
	public Element<V> selfApply(BigInteger amount);

	/**
	 * @param amount
	 * @return @see Group#selfApply(Element, Element)
	 */
	public Element<V> selfApply(Element<BigInteger> amount);

	/**
	 * @return @see Group#selfApply(Element)
	 */
	public Element<V> selfApply();

	/**
	 * @return @see Group#isIdentityElement(Element)
	 */
	public boolean isIdentity();

	/**
	 * @return @see Group#invert(Element)
	 */
	public Element<V> invert();

	/**
	 * @param element
	 * @return @see Group#applyInverse(Element, Element)
	 */
	public Element<V> applyInverse(Element element);

	/**
	 * @return @see CyclicGroup#isGenerator(Element)
	 */
	public boolean isGenerator();

}
