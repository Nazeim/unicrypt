/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Compound;
import ch.bfh.unicrypt.math.helper.CompoundIterator;
import java.lang.reflect.Array;
import java.util.Iterator;

/**
 *
 * @param <CF>
 * @param <F>
 * @param <D>
 * @param <DE>
 * @param <C>
 * @param <CE>
 * @author rolfhaenni
 */
public abstract class AbstractCompoundFunction<CF extends AbstractCompoundFunction<CF, F, D, DE, C, CE>, F extends Function, D extends Set, DE extends Element, C extends Set, CE extends Element>
			 extends AbstractFunction<D, DE, C, CE>
			 implements Compound<CF, F>, Iterable<F> {

	private final F[] functions;
	private final int arity;
	private final Class<?> functionClass; // this is needed to create generic arrays of type F

	protected AbstractCompoundFunction(D domain, C coDomain, F[] functions) {
		super(domain, coDomain);
		this.functionClass = functions.getClass().getComponentType();
		this.functions = functions.clone();
		this.arity = functions.length;
	}

	protected AbstractCompoundFunction(D domain, C coDomain, F function, int arity) {
		super(domain, coDomain);
		this.functionClass = function.getClass();
		this.functions = (F[]) Array.newInstance(this.functionClass, 1);
		this.functions[0] = function;
		this.arity = arity;
	}

	@Override
	public int getArity() {
		return this.arity;
	}

	@Override
	public final boolean isNull() {
		return this.getArity() == 0;
	}

	@Override
	public final boolean isUniform() {
		return this.functions.length <= 1;
	}

	@Override
	public F getFirst() {
		return this.getAt(0);
	}

	@Override
	public F getAt(int index) {
		if (index < 0 || index >= this.getArity()) {
			throw new IndexOutOfBoundsException();
		}
		if (this.isUniform()) {
			return (F) this.functions[0];
		}
		return (F) this.functions[index];
	}

	@Override
	public F getAt(int... indices) {
		if (indices == null) {
			throw new IllegalArgumentException();
		}
		F function = (F) this;
		for (final int index : indices) {
			if (function.isCompound()) {
				function = ((Compound<CF, F>) function).getAt(index);
			} else {
				throw new IllegalArgumentException();
			}
		}
		return function;
	}

	@Override
	public F[] getAll() {
		return this.functions.clone();
	}

	@Override
	public CF removeAt(final int index) {
		int arity = this.getArity();
		if (index < 0 || index >= arity) {
			throw new IndexOutOfBoundsException();
		}
		if (this.isUniform()) {
			return this.abstractRemoveAt(this.getFirst(), arity - 1);
		}
		final F[] remaining = (F[]) Array.newInstance(this.functionClass, arity - 1);
		for (int i = 0; i < arity - 1; i++) {
			if (i < index) {
				remaining[i] = this.getAt(i);
			} else {
				remaining[i] = this.getAt(i + 1);
			}
		}
		return this.abstractRemoveAt(remaining);
	}

	protected abstract CF abstractRemoveAt(F function, int arity);

	protected abstract CF abstractRemoveAt(F[] functions);

	@Override
	public Iterator<F> iterator() {
		return new CompoundIterator<F>(this);
	}

	@Override
	protected boolean standardIsCompound() {
		return true;
	}

	@Override
	protected boolean standardIsEqual(Function function) {
		CF other = (CF) function;
		int arity = this.getArity();
		if (arity != other.getArity()) {
			return false;
		}
		for (int i = 0; i < arity; i++) {
			if (!this.getAt(i).isEqual(other.getAt(i))) {
				return false;
			}
		}
		return true;
	}

}
