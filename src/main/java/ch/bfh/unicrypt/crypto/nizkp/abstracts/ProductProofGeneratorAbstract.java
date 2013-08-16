package ch.bfh.unicrypt.crypto.nizkp.abstracts;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.nizkp.interfaces.ProductProofGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public abstract class ProductProofGeneratorAbstract extends ProductCoDomainProofGeneratorAbstract implements ProductProofGenerator {

  // ProductProofGeneratorAbstract should inherit from both
  // ProductDomainProofGeneratorAbstract
  // and ProductCoDomainProofGeneratorAbstract, but since Java does not support
  // multiple
  // inheritance, some code from ProductDomainProofGeneratorAbstract is copied.

  @Override
  public TupleElement generate(final List<Element> secretInputs, final Element publicInput) {
    return this.generate(this.getDomain().getElement(secretInputs), publicInput);
  }

  @Override
  public TupleElement generate(final List<Element> secretInputs, final Element publicInput, final Element otherInput) {
    return this.generate(this.getDomain().getElement(secretInputs), publicInput, otherInput);
  }

  @Override
  public TupleElement generate(final List<Element> secretInputs, final Element publicInput, final Random random) {
    return this.generate(this.getDomain().getElement(secretInputs), publicInput, random);
  }

  @Override
  public TupleElement generate(final List<Element> secretInputs, final Element publicInput, final Element otherInput, final Random random) {
    return this.generate(this.getDomain().getElement(secretInputs), publicInput, otherInput, random);
  }

  @Override
  public ProductGroup getDomain() {
    return (ProductGroup) super.getDomain();
  }

  @Override
  public TupleElement generate(final List<Element> secretInputs, final List<Element> publicInputs) {
    return this.generate(this.getDomain().getElement(secretInputs), this.getCoDomain().getElement(publicInputs));
  }

  @Override
  public TupleElement generate(final List<Element> secretInputs, final List<Element> publicInputs, final Element otherInput) {
    return this.generate(this.getDomain().getElement(secretInputs), this.getCoDomain().getElement(publicInputs), otherInput);
  }

  @Override
  public TupleElement generate(final List<Element> secretInputs, final List<Element> publicInputs, final Random random) {
    return this.generate(this.getDomain().getElement(secretInputs), this.getCoDomain().getElement(publicInputs), random);
  }

  @Override
  public TupleElement generate(final List<Element> secretInputs, final List<Element> publicInputs, final Element otherInput, final Random random) {
    return this.generate(this.getDomain().getElement(secretInputs), this.getCoDomain().getElement(publicInputs), otherInput, random);
  }

}