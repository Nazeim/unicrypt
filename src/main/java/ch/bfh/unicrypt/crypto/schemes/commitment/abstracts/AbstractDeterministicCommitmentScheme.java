/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.crypto.schemes.commitment.abstracts;

import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.DeterministicCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SharedDomainFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author R. Haenni
 * @param <MS> Message space
 * @param <ME> Message element
 * @param <CS> Commitment space
 * @param <CE> Commitment element
 */
public abstract class AbstractDeterministicCommitmentScheme<MS extends Set, ME extends Element, CS extends Set, CE extends Element>
	   extends AbstractCommitmentScheme<MS, CS>
	   implements DeterministicCommitmentScheme {

	protected AbstractDeterministicCommitmentScheme(MS messageSpace, CS commitmentSpace) {
		super(messageSpace, commitmentSpace);
	}

	@Override
	public final CE commit(final Element message) {
		return (CE) this.getCommitmentFunction().apply(message);
	}

	@Override
	public final BooleanElement decommit(final Element message, final Element commitment) {
		return (BooleanElement) this.getDecommitmentFunction().apply(message, commitment);
	}

	@Override
	protected Function abstractGetDecommitmentFunction() {
		ProductSet decommitmentDomain = ProductSet.getInstance(this.messageSpace, this.commitmentSpace);
		return CompositeFunction.getInstance(
			   SharedDomainFunction.getInstance(CompositeFunction
					  .getInstance(SelectionFunction.getInstance(decommitmentDomain, 0),
								   this.getCommitmentFunction()),
												SelectionFunction.getInstance(decommitmentDomain, 1)),
			   EqualityFunction.getInstance(this.getCommitmentSpace()));
	}

}
