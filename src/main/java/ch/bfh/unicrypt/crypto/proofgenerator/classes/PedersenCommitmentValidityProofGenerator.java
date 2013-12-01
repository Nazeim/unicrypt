package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractSetMembershipProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class PedersenCommitmentValidityProofGenerator
	   extends AbstractSetMembershipProofGenerator {

	protected PedersenCommitmentValidityProofGenerator(Function oneWayFunction, Function deltaFunction, Tuple members, HashMethod hashMethod) {
		super(oneWayFunction, deltaFunction, members, hashMethod);
	}

	public static PedersenCommitmentValidityProofGenerator getInstance(PedersenCommitmentScheme pedersenCS, Tuple plaintexts) {
		return PedersenCommitmentValidityProofGenerator.getInstance(pedersenCS, plaintexts, HashMethod.DEFAULT);
	}

	public static PedersenCommitmentValidityProofGenerator getInstance(PedersenCommitmentScheme pedersenCS, Tuple messages, HashMethod hashMethod) {
		if (pedersenCS == null || messages == null || messages.getArity() < 1 || hashMethod == null) {
			throw new IllegalArgumentException();
		}

		Function oneWayFunction = pedersenCS.getCommitmentFunction();

		ProductSet deltaFunctionDomain = ProductSet.getInstance(pedersenCS.getMessageSpace(), oneWayFunction.getCoDomain());
		Function deltaFunction =
			   CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
											 ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1),
																		 CompositeFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 0),
																									   GeneratorFunction.getInstance(pedersenCS.getFirstGenerator()),
																									   InvertFunction.getInstance(pedersenCS.getCyclicGroup()))),
											 ApplyFunction.getInstance(pedersenCS.getCyclicGroup()));

		return new PedersenCommitmentValidityProofGenerator(oneWayFunction, deltaFunction, messages, hashMethod);
	}

}