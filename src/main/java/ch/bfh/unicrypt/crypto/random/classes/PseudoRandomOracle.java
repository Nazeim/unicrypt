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
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.abstracts.AbstractRandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.HashMap;

/**
 *
 * @author rolfhaenni
 */
public class PseudoRandomOracle
	   extends AbstractRandomOracle {

	HashMap<Element, PseudoRandomReferenceString> referenceStrings;

	public static final PseudoRandomOracle DEFAULT = PseudoRandomOracle.getInstance();

	protected PseudoRandomOracle(HashMethod hashMethod) {
		super(hashMethod);
		referenceStrings = new HashMap<Element, PseudoRandomReferenceString>();

	}

	//TODO: Warning, this is a memory-hog!
	//This code will only work when there is a fast 'equals' method ready for Element
	@Override
	protected RandomReferenceString abstractGetRandomReferenceString(Element query) {
		if (!referenceStrings.containsKey(query)) {
			referenceStrings.put(query, PseudoRandomReferenceString.getInstance(query));
		}
		RandomReferenceString referenceString = referenceStrings.get(query);
		referenceString.reset();
		return referenceString;
	}
//	@Override
//	protected RandomReferenceString abstractGetRandomReferenceString(Element query) {
//		return PseudoRandomReferenceString.getInstance(query);
//	}

	public static PseudoRandomOracle getInstance() {
		return PseudoRandomOracle.getInstance(HashMethod.DEFAULT);
	}

	public static PseudoRandomOracle getInstance(HashMethod hashMethod) {
		if (hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new PseudoRandomOracle(hashMethod);
	}

}