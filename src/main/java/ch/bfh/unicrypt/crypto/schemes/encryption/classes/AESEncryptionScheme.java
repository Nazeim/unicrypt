package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.crypto.keygenerator.classes.AESKeyGenerator;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractSymmetricEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

public class AESEncryptionScheme
			 extends AbstractSymmetricEncryptionScheme<ByteArrayMonoid, ByteArrayElement, ByteArrayMonoid, ByteArrayElement, FiniteByteArraySet> {

	public static final int AES_BLOCK_SIZE = 128;

	private final FiniteByteArraySet byteArraySet = FiniteByteArraySet.getInstance(16, true);
	private final ByteArrayMonoid byteArrayMonoid = ByteArrayMonoid.getInstance(AES_BLOCK_SIZE / Byte.SIZE);

	@Override
	protected Function abstractGetEncryptionFunction() {
		return new AESEncryptionFunction();
	}

	@Override
	protected Function abstractGetDecryptionFunction() {
		return new AESDecryptionFunction();
	}

	@Override
	protected KeyGenerator abstractGetKeyGenerator() {
		return AESKeyGenerator.getInstance();
	}

	private class AESEncryptionFunction
				 extends AbstractFunction<ProductSet, Pair, ByteArrayMonoid, ByteArrayElement> {

		protected AESEncryptionFunction() {
			super(ProductSet.getInstance(byteArraySet, byteArrayMonoid), byteArrayMonoid);
		}

		@Override
		protected ByteArrayElement abstractApply(Pair element, Random random) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

	}

	private class AESDecryptionFunction
				 extends AbstractFunction<ProductSet, Pair, ByteArrayMonoid, ByteArrayElement> {

		protected AESDecryptionFunction() {
			super(ProductSet.getInstance(byteArraySet, byteArrayMonoid), byteArrayMonoid);
		}

		@Override
		protected ByteArrayElement abstractApply(Pair element, Random random) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

	}

}

//import java.math.BigInteger;
//import java.util.Random;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import javax.xml.bind.DatatypeConverter;
//
//import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractEncryptionScheme;
//import ch.bfh.unicrypt.crypto.keygenerator.old.PasswordBasedKeyGenerator;
//import ch.bfh.unicrypt.crypto.utility.AESUtil;
//import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
//import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
//import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
//import ch.bfh.unicrypt.math.function.interfaces.Function;
//
//public class AESEncryptionScheme extends AbstractEncryptionScheme implements DeterministicEncryptionScheme {
//  private int encryptionIterations;
//  private PasswordKeyGenerator keyGenerator;
//  private EncryptionFunction encryptionFunction;
//  private DecryptionFunction decryptionFunction;
//
//  public AESEncryptionScheme() {
//    this(1);
//  }
//
//  public AESEncryptionScheme(int encryptionIterations) {
//    this(encryptionIterations, new PasswordBasedKeyGenerator());
//  }
//
//  public AESEncryptionScheme(int encryptionIterations, ZPlusMod keySpace, int keyIterations) {
//    this(encryptionIterations, new PasswordBasedKeyGenerator(keySpace, keyIterations));
//  }
//
//  public AESEncryptionScheme(int encryptionIterations, PasswordKeyGenerator keyGenerator) {
//    if(encryptionIterations<1)
//      throw new IllegalArgumentException();
//    this.encryptionIterations = encryptionIterations;
//    if(keyGenerator==null)
//      throw new IllegalArgumentException();
//    this.keyGenerator = keyGenerator;
//    this.encryptionFunction = new EncryptionFunction(keyGenerator, encryptionIterations);
//    this.decryptionFunction = new DecryptionFunction(keyGenerator, encryptionIterations);
//
//  }
//
//  public int getEncryptionIterations() {
//    return this.encryptionIterations;
//  }
//
//  public Element encrypt(Element key, Element plaintext) {
//    return this.encryptionFunction.apply(key, plaintext);
//  }
//
//  @Override
//  public Element decrypt(Element key, Element ciphertext) {
//    return this.decryptionFunction.apply(key, ciphertext);
//  }
//
//  @Override
//  public Function getDecryptionFunction() {
//    return this.decryptionFunction;
//  }
//
//  @Override
//  public Function getEncryptionFunction() {
//    return this.encryptionFunction;
//  }
//
//  @Override
//  public Function getIdentityEncryptionFunction() {
//    throw new RuntimeException("This method does not make sense... Redesign");
//  }
//
//  public PasswordKeyGenerator getKeyGenerator() {
//    return this.keyGenerator;
//  }
//
//  public ZPlus getMessageSpace() {
//    return (ZPlus) this.decryptionFunction.getCoDomain();
//  }
//
//  public ZPlus getEncryptionSpace() {
//    return (ZPlus) this.encryptionFunction.getCoDomain();
//  }
//
//  private class EncryptionFunction extends AbstractFunction {
//    private int iterations;
//
//    public EncryptionFunction(PasswordKeyGenerator keyGenerator, int iterationAmount) {
//      super(new ProductGroup(keyGenerator.getKeySpace(), ZPlus.Factory.getInstance()), ZPlus.Factory.getInstance());
//      if (iterationAmount <= 0) {
//        throw new IllegalArgumentException();
//      }
//      this.iterations = iterationAmount;
//    }
//
//    @Override
//    public Element abstractApply(Element element, Random random) {
//      BigInteger plainText = element.getAt(1).getValue();
//      Element keyElement = element.getAt(0);
//      BigInteger key = keyElement.getValue();
//      // HACK: This hack is required in order to re-insert lost 0-bits.
//      int requiredKeySizeInBytes = ((int) Math.ceil((keyElement.getSet().getZPlusModOrder().getModulus().bitLength() + 1) / 8));
//      byte[] keyBytes = key.toByteArray(); //
//      byte[] nKey = new byte[requiredKeySizeInBytes]; //
//      System.arraycopy(keyBytes, 0, nKey, requiredKeySizeInBytes-keyBytes.length, keyBytes.length); //
//      SecretKey secret = new SecretKeySpec(nKey, "AES"); //
//      // HACK: This hack is required in order to re-insert lost 0-bits.
//      byte[] cipherText = null;
//      try {
//        cipherText = AESUtil.encrypt(plainText.toByteArray(), secret, this.iterations);
//      } catch (Exception e) {
//        e.printStackTrace();
//        throw new IllegalArgumentException();
//      }
//    //HACK: The ciphertext is stored as the byte-representation of a Base64-String
//      String base64=DatatypeConverter.printBase64Binary(cipherText);
//      return ZPlus.Factory.getInstance().getElement(new BigInteger(1,base64.getBytes()));
//    //HACK: The ciphertext is stored as the byte-representation of a Base64-String
//    }
//  }
//
//  private class DecryptionFunction extends AbstractFunction {
//    private int iterations;
//
//    public DecryptionFunction(PasswordKeyGenerator keyGenerator, int iterationAmount) {
//      super(new ProductGroup(keyGenerator.getKeySpace(), ZPlus.Factory.getInstance()), ZPlus.Factory.getInstance());
//      if (iterationAmount <= 0) {
//        throw new IllegalArgumentException();
//      }
//      this.iterations = iterationAmount;
//    }
//
//    @Override
//    public Element abstractApply(Element element, Random random) {
//      Element ciphertextElement = element.getAt(1);
//      //HACK: The ciphertext must be unwrapped from the byte-Representation of a Base64-String
//      byte[] cipherText = DatatypeConverter.parseBase64Binary(new String(ciphertextElement.getValue().toByteArray()));
//      //HACK: The ciphertext must be unwrapped from the byte-Representation of a Base64-String
//      Element keyElement = element.getAt(0);
//      BigInteger key = keyElement.getValue();
//      // HACK: This hack is required in order to re-insert lost 0-bits.
//      int requiredKeySizeInBytes = ((int) Math.ceil((keyElement.getSet().getZPlusModOrder().getModulus().bitLength() + 1) / 8));
//      byte[] keyBytes = key.toByteArray(); //
//      byte[] nKey = new byte[requiredKeySizeInBytes]; //
//      System.arraycopy(keyBytes, 0, nKey, requiredKeySizeInBytes-keyBytes.length, keyBytes.length); //
//      // HACK: This hack is required in order to re-insert lost 0-bits.
//      SecretKey secret = new SecretKeySpec(nKey, "AES");
//      byte[] plainText = null;
//      try {
//        plainText = AESUtil.decrypt(cipherText, secret, this.iterations);
//      } catch (Exception e) {
//        e.printStackTrace();
//        throw new IllegalArgumentException();
//      }
//      return ZPlus.Factory.getInstance().getElement(new BigInteger(1, plainText));
//    }
//  }
//

