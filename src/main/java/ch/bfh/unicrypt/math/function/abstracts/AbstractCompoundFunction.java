/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.abstracts.AbstractCompoundSet;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Compound;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCompoundFunction extends AbstractFunction implements Compound<Function> {

  private final Function[] functions;
  private final int arity;

  protected AbstractCompoundFunction(Set domain, Set coDomain, Function[] functions) {
    super(domain, coDomain);
    this.functions = functions.clone();
    this.arity = functions.length;
  }

  protected AbstractCompoundFunction(Set domain, Set coDomain, Function function, int arity) {
    super(domain, coDomain);
    this.functions = new Function[]{function};
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
  public Function getFirst() {
    return this.getAt(0);

  }

  @Override
  public Function getAt(int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return this.functions[0];
    }
    return this.functions[index];
  }

  @Override
  public Function getAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Function function = this;
    for (final int index : indices) {
      if (function instanceof Compound) {
        function = ((Compound<Function>) function).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return function;
  }

  @Override
  public Function[] getAll() {
    int arity = this.getArity();
    Function[] result = new Function[arity];
    for (int i = 0; i < arity; i++) {
      result[i] = this.getAt(i);
    }
    return result;
  }

  @Override
  public Iterator<Function> iterator() {
    final AbstractCompoundFunction compoundFunction = this;
    return new Iterator<Function>() {
      int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex >= compoundFunction.getArity();
      }

      @Override
      public Function next() {
        if (this.hasNext()) {
          return compoundFunction.getAt(this.currentIndex);
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
    };
  }

}