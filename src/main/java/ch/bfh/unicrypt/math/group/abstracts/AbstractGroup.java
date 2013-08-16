package ch.bfh.unicrypt.math.group.abstracts;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.utility.MathUtil;

/**
 * This abstract class provides a basis implementation for objects of type {@link Group}.
 *
 * @see Element
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractGroup extends AbstractMonoid implements Group {

  private static final long serialVersionUID = 1L;

  @Override
  public final Element invert(final Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    return abstractInvert(element);
  }

  @Override
  public final Element applyInverse(Element element1, Element element2) {
    return this.apply(element1, this.invert(element2));
  }

  @Override
  protected Element standardSelfApply(Element element, BigInteger amount) {
    if (amount.signum() < 0) {
      return super.selfApply(element, amount.abs()).invert();
    }
    return super.standardSelfApply(element, amount);
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class.
  //

  protected abstract Element abstractInvert(Element element);

}