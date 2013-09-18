package ch.bfh.unicrypt.math.algebra.general.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;

/**
 * This abstract class provides a basis implementation for objects of type
 * {@link SemiGroup}.
 *
 * @see AbstractElement
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractSemiGroup<E extends Element> extends AbstractSet<E> implements SemiGroup {

  @Override
  public final E apply(final Element element1, final Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    return this.abstractApply(element1, element2);
  }

  @Override
  public final E apply(final Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    return this.standardApply(elements);
  }

  @Override
  public final E selfApply(final Element element, final BigInteger amount) {
    if (!this.contains(element) || (amount == null)) {
      throw new IllegalArgumentException();
    }
    return this.standardSelfApply(element, amount);
  }

  @Override
  public final E selfApply(final Element element, final Element amount) {
    if (amount == null) {
      throw new IllegalArgumentException();
    }
    return this.selfApply(element, amount.getValue());
  }

  @Override
  public final E selfApply(final Element element, final int amount) {
    return this.selfApply(element, BigInteger.valueOf(amount));
  }

  @Override
  public final E selfApply(final Element element) {
    return this.apply(element, element);
  }

  @Override
  public final E multiSelfApply(final Element[] elements, final BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length)) {
      throw new IllegalArgumentException();
    }
    return this.standardMultiSelfApply(elements, amounts);
  }

  //
  // The following protected methods are standard implementations for sets.
  // They may need to be changed in certain sub-classes.
  //
  protected E standardApply(final Element... elements) {
    if (elements.length == 0) {
      throw new IllegalArgumentException();
    }
    E result = null;
    for (Element element : elements) {
      if (result == null) {
        result = (E) element;
      } else {
        result = this.apply(result, element);
      }
    }
    return result;
  }

  protected E standardSelfApply(Element element, BigInteger amount) {
    if (amount.signum() <= 0) {
      throw new IllegalArgumentException();
    }
    Element result = element;
    for (int i = amount.bitLength() - 2; i >= 0; i--) {
      result = result.selfApply();
      if (amount.testBit(i)) {
        result = result.apply(element);
      }
    }
    return (E) result;
  }

  protected E standardMultiSelfApply(final Element[] elements, final BigInteger[] amounts) {
    if (elements.length == 0) {
      throw new IllegalArgumentException();
    }
    Element[] results = new Element[elements.length];
    for (int i = 0; i < elements.length; i++) {
      results[i] = this.selfApply(elements[i], amounts[i]);
    }
    return this.apply(results);
  }

  //
  // The following protected abstract method must be implemented in every
  // direct sub-class.
  //
  protected abstract E abstractApply(Element element1, Element element2);

}

