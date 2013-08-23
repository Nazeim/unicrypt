package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.abstracts.AtomicElement;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.interfaces.Group;

/**
 * This abstract class provides a basis implementation for objects of type {@link Group}.
 *
 * @see AbstractElement
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractGroup extends AbstractMonoid implements Group {

  private static final long serialVersionUID = 1L;

  @Override
  public final AtomicElement invert(final Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    return this.abstractInvert(element);
  }

  @Override
  public final AtomicElement applyInverse(Element element1, Element element2) {
    return this.apply(element1, this.invert(element2));
  }

  @Override
  protected AtomicElement standardSelfApply(Element element, BigInteger amount) {
    if (amount.signum() < 0) {
      return this.invert(super.selfApply(element, amount.abs()));
    }
    return super.standardSelfApply(element, amount);
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class.
  //

  protected abstract AtomicElement abstractInvert(Element element);

}