package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.math.algebra.multiplicative.abstracts.AbstractMultiplicativeElement;
import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.abstracts.AbstractMultiplicativeMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 * /**
 * This class implements the multiplicative monoid of (positive and negative)
 * integers with infinite order. Its identity element is 0.
 *
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a
 * href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ZTimes extends AbstractMultiplicativeMonoid<MultiplicativeElement> {

  /**
   * This is the private constructor of this class. It is called by the static
   * factory methods of the static nested class Factory.
   */
  private ZTimes() {
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected MultiplicativeElement abstractGetElement(BigInteger value) {
    return new AbstractMultiplicativeElement<ZTimes, MultiplicativeElement<ZTimes>>(this, value) {
    };
  }

  @Override
  protected AbstractMultiplicativeElement abstractGetRandomElement(final Random random) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected boolean abstractContains(final BigInteger value) {
    return true;
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Group.INFINITE_ORDER;
  }

  @Override
  protected MultiplicativeElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected MultiplicativeElement abstractApply(final Element element1, final Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()));
  }

  @Override
  protected boolean standardEquals(Set set) {
    return true;
  }
  //
  // STATIC FACTORY METHODS
  //
  private static ZTimes instance;

  /**
   * Returns the singleton object of this class.
   *
   * @return The singleton object of this class
   */
  public static ZTimes getInstance() {
    if (ZTimes.instance == null) {
      ZTimes.instance = new ZTimes();
    }
    return ZTimes.instance;
  }

}
