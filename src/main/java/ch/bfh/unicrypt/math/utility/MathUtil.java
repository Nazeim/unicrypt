package ch.bfh.unicrypt.math.utility;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

/**
 * This is a helper class with some static methods for various mathematical functions.
 *  
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public final class MathUtil {

  /**
   * Returns the value obtained from applying the Euler totient function to an integer {@code value}. 
   * <dt><b>Preconditions:</b></dt>
   * <dd>{@code primeFactorSet} is the complete set of prime factors of {@code value}.</dd>
   * @param value The input value
   * @param primeFactorSet The prime factors of {@code value}
   * @return the result of applying the Euler totient function to {@code value}
   * @throws IllegalArgumentException if {@code value} is {@code null}, {@code 0}, or negative
   * @throws IllegalArgumentException if {@code primeFactorSet} is null or if {@code primeFactorSet} contains {@code null}
   * @see MathUtil#arePrimeFactors(BigInteger, BigInteger[])
   * @see MathUtil#removeDuplicates(BigInteger[])
   * @see "Handbook of Applied Cryptography, Fact 2.101 (iii)"
   */
  public static BigInteger eulerFunction(final BigInteger value, final BigInteger... primeFactorSet) {
    if (value == null || value.signum() == 0 || value.signum() == -1 || primeFactorSet == null) {
      throw new IllegalArgumentException();
    }
    BigInteger product1 = BigInteger.ONE;
    BigInteger product2 = BigInteger.ONE;
    for (final BigInteger prime : primeFactorSet) {
      if (prime == null) {
        throw new IllegalArgumentException();
      }
      product1 = product1.multiply(prime);
      product2 = product2.multiply(prime.subtract(BigInteger.ONE));
    }
    return value.multiply(product2).divide(product1);
  }

  /**
   * Tests if some given BigInteger values are all prime factors of another BigInteger value. The given list of prime factors need not be complete.
   * @param value The given value
   * @param factors A given array of potential prime factors
   * @return {@code true} if all values are prime factors, {@code false} otherwise
   */  
  public static boolean arePrimeFactors(final BigInteger value, final BigInteger... factors) {
    if (factors == null) {
      return false;
    }
    for (BigInteger factor : factors) {
      if (!isPrimeFactor(value, factor)) {
        return false;
      }
    }
    return isPositive(value);
  }

  private static boolean isPrimeFactor(final BigInteger value, final BigInteger factor) {
    return isPositive(value) && isPrime(factor) && value.gcd(factor).equals(factor);
  }

  /**
   * Tests if some given BigInteger values are all prime numbers.
   * @param values A given array of potential prime numbers
   * @return {@code true} if all values are prime numbers, {@code false} otherwise
   */  
  public static boolean arePrime(final BigInteger... values) {
    if (values == null) {
      return false;
    }
    for (BigInteger value : values) {
      if (!isPrime(value)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Tests if a given BigInteger value is a positive prime number.
   * @param value A potential prime number
   * @return {@code true} if {@code value} is prime, {@code false} otherwise
   */  
  public static boolean isPrime(final BigInteger value) {
    return isPositive(value) && value.isProbablePrime(MathUtil.NUMBER_OF_PRIME_TESTS);
  }

  /**
   * Tests if a given BigInteger value is a save prime.
   * @param value A potential save prime 
   * @return {@code true} if {@code value} is a save prime, {@code false} otherwise
   */  
  public static boolean isSavePrime(final BigInteger value) {
    return isPrime(value) && isPrime(value.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)));
  }

  public static final int NUMBER_OF_PRIME_TESTS = 40;

  /**
   * Tests if some given BigInteger values are all positive.
   * @param values A given array of potential positive numbers
   * @return {@code true} if all values are positive, {@code false} otherwise
   */  
  public static boolean arePositive(final BigInteger... values) {
    if (values == null) {
      return false;
    }
    for (BigInteger value : values) {
      if (!isPositive(value)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Tests if a given BigInteger value is positive.
   * @param value A potential positive number
   * @return {@code true} if {@code value} is positive, {@code false} otherwise
   */  
  public static boolean isPositive(final BigInteger value) {
    if (value == null) {
      return false;
    }
    return value.signum() == 1;
  }

  /**
   * Tests if some given integer values are all positive.
   * @param values A given array of potential positive numbers
   * @return {@code true} is all values are positive, {@code false} otherwise
   */  
  public static boolean arePositive(final int... values) {
    for (int value : values) {
      if (value <= 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Removes duplicate values from a BigInteger array
   * @param values An array of BigInteger values
   * @return the same array of BigInteger values without duplicates
   * @throws IllegalArgumentException if {@code values} is {@code null}
   */
  public static BigInteger[] removeDuplicates(final BigInteger... values) {
    if (values == null) {
      throw new IllegalArgumentException();
    }
    final HashSet<BigInteger> hashSet = new HashSet<BigInteger>(Arrays.asList(values));
    return hashSet.toArray(new BigInteger[hashSet.size()]);
  }

  /**
   * Computes the factorial of some integer value. Returns 1 for inut value 0.
   * @param value The input value
   * @return The factorial of {@code value}
   * @throws IllegalArgumentException if {@code value} is negative.
   */
  public static BigInteger factorial(final int value) {
    if (value < 0) {
      throw new IllegalArgumentException();
    }
    BigInteger result = BigInteger.ONE;
    for (int i = 1; i <= value; i++) {
      result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
  }

  /**
   * Computes the maximum value of a given BigInteger array.
   * @param values The given BigInteger array
   * @return The maximum value
   * @throws IllegalArgumentException if {@code values} is null or empty, or if it contains null
   */
  public static BigInteger maxValue(final BigInteger... values) {
    if (values == null || values.length == 0) {
      throw new IllegalArgumentException();
    }
    BigInteger maxValue = null;
    for (final BigInteger value : values) {
      if (value == null) {
        throw new IllegalArgumentException();
      }
      if (maxValue == null) {
        maxValue = value;
      } else {
        maxValue = maxValue.max(value);
      }
    }
    return maxValue;
  }

  /**
   * Computes the elegant pairing function for two non-negative BigInteger values.
   * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
   * @param value1 The first value
   * @param value2 The second value
   * @return The result of applying the elegant pairing function
   * @throws IllegalArgumentException if {@code value1} or {@code value2} is null or negative
   */
  public static BigInteger elegantPair(BigInteger value1, BigInteger value2) {
    if (value1==null || value1.signum()<0 || value2==null || value2.signum()<0) {
      throw new IllegalArgumentException();
    }
    if (value1.compareTo(value2) < 0) {
      return value2.multiply(value2).add(value1);
    }
    return value1.multiply(value1).add(value1).add(value2);
  }

  /**
   * Computes the elegant pairing function for a given list of non-negative BigInteger values. The
   * order in which the binary pairing function is applied is recursively from left to right. 
   * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
   * @param values The given values
   * @return The result of applying the elegant pairing function
   * @throws IllegalArgumentException if {@code values} is null
   * @throws IllegalArgumentException if {@code values} contains null or negative value
   */
  public static BigInteger elegantPair(BigInteger... values) {
    if (values == null) {
      throw new IllegalArgumentException();
    }
    int n = values.length;
    if (n==0) {
      return BigInteger.ZERO;
    }
    if (n==1) {
      return values[0];
    }
    BigInteger[] a = new BigInteger[n/2 + n%2];
    for (int i=0; i<n/2; i++) {
      a[i] = elegantPair(values[2*i], values[2*i+1]);
    }
    if (n%2 == 1) {
      a[n/2] = values[n-1];
    }
    return elegantPair(a);
  }

  /**
   * Computes the elegant pairing function for a given list of non-negative BigInteger values. 
   * The size of the given input list is taken as an additional input value.
   * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
   * @param values The given values
   * @return The result of applying the elegant pairing function
   * @throws IllegalArgumentException if {@code values} is null
   * @throws IllegalArgumentException if {@code values} contains null or negative value
   */
  public static BigInteger elegantPairWithSize(BigInteger... values) {
    if (values == null) {
      throw new IllegalArgumentException();
    }
    return elegantPair(elegantPair(values), BigInteger.valueOf(values.length));
  }

  /**
   * Computes the inverse of the binary elegant pairing function for a given non-negative BigInteger value.
   * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
   * @param value The input value
   * @return An array containing the two resulting values
   * @throws IllegalArgumentException if {@code value} is null or negative
   */
  public static BigInteger[] elegantUnpair(BigInteger value) {
    if (value == null || value.signum()<0) {
      throw new IllegalArgumentException();
    }
    BigInteger x1 = sqrt(value);
    BigInteger x2 = value.subtract(x1.multiply(x1));
    if (x1.compareTo(x2)>0) {
      return new BigInteger[]{x2, x1};
    }
    return new BigInteger[]{x1, x2.subtract(x1)};
  }

  /**
   * Computes the inverse of the n-ary elegant pairing function for a given non-negative BigInteger value.
   * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
   * @param value The input value
   * @param size The number of resulting values
   * @return An array containing the resulting values
   * @throws IllegalArgumentException if {@code value} is null or negative
   * @throws IllegalArgumentException if {@code size} is negative
   */
  public static BigInteger[] elegantUnpair(BigInteger value, int size) {
    if (size < 0 || value.signum() < 0) {
      throw new IllegalArgumentException();
    }
    BigInteger[] result = new BigInteger[size];
    if (size == 0) {
      if (value.signum() > 0) {
        throw new IllegalArgumentException();
      }
    } else {
      elegantUnpair(value, size, 0, result);
    }
    return result;
  }

  // This is a private helper method for doing the recursion
  private static void elegantUnpair(BigInteger value, int size, int start, BigInteger[] result) {
    if (size == 1) {
      result[start] = value;
    } else {
      BigInteger[] values = elegantUnpair(value);
      int powerOfTwo = 1 << BigInteger.valueOf(size-1).bitLength()-1;
      elegantUnpair(values[0], powerOfTwo, start, result);
      elegantUnpair(values[1], size-powerOfTwo, start+powerOfTwo, result);
    }
  }

  /**
   * Computes the inverse of the n-ary elegant pairing function for a given non-negative BigInteger value,
   * where the size is included as additional input value.
   * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
   * @param value The input value
   * @return An array containing the resulting values
   * @throws IllegalArgumentException if {@code value} is null or negative
   */
  public static BigInteger[] elegantUnpairWithSize(BigInteger value) {
    BigInteger[] values = elegantUnpair(value);
    return elegantUnpair(values[0], values[1].intValue());
  }

  public static int[] bigIntegerToIntArray(BigInteger... values) {
    if (values == null) {
      throw new IllegalArgumentException();
    }
    int[] result = new int[values.length];
    for (int i=0; i<values.length; i++) {
      if (values[i] == null) {
        throw new IllegalArgumentException();
      }
      result[i] = values[i].intValue();
    }
    return result;
  }

  public static BigInteger[] intToBigIntegerArray(int... values) {
    if (values == null) {
      throw new IllegalArgumentException();
    }
    BigInteger[] result = new BigInteger[values.length];
    for (int i=0; i<values.length; i++) {
      result[i] = BigInteger.valueOf(values[i]);
    }
    return result;
  }

  // This is a private helper method to compute the integer square root of a BigInteger value.
  private static BigInteger sqrt(BigInteger n) {
    BigInteger a = BigInteger.ONE;
    BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
    while(b.compareTo(a) >= 0) {
      BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
      if(mid.multiply(mid).compareTo(n) > 0) b = mid.subtract(BigInteger.ONE);
      else a = mid.add(BigInteger.ONE);
    }
    return a.subtract(BigInteger.ONE);
  }

}