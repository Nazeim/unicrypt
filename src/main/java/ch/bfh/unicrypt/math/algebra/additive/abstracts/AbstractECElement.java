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
package ch.bfh.unicrypt.math.algebra.additive.abstracts;

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.EC;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.ECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <V> Type Finite Field ov EC
 * @param <E> Type of FiniteFieldElement
 */
public class AbstractECElement<V extends Object,E extends DualisticElement<V>>
	   extends AbstractAdditiveElement<EC<V,E>, AbstractECElement<V,E>, Point<E>> implements ECElement<V,E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final boolean infinity;

	// the main constructor
	protected AbstractECElement(EC<V,E> ecGroup, Point<E> value) {
		super(ecGroup, value);
		this.infinity = false;
	}

	// special constructor is necessary for the additional point of infinity
	protected AbstractECElement(EC<V,E> ecGroup) {
		super(ecGroup, Point.<E>getInstance());
		this.infinity = true;
	}

	// additional convenience getter method to handle to point of infinity
	public E getX() {
		if (this.infinity) {
			throw new UnsupportedOperationException();
		}
		return this.getValue().getX();
	}

	// additional convenience getter method to handle to point of infinity
	public E getY() {
		if (this.infinity) {
			throw new UnsupportedOperationException();
		}
		return this.getValue().getY();
	}

	@Override
	protected String defaultToStringValue() {
		if (this.infinity) {
			return "Infinity";
		} else {
			return "(" + this.getX().getValue() + "," + this.getY().getValue() + ")";
		}
	}

}
