package io.thiiagoms.ims.fixtures.product.domain;

import io.thiiagoms.ims.product.domain.SkuGenerator;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;

public final class SkuGeneratorStub implements SkuGenerator {
  private Sku nextSku;

  public SkuGeneratorStub willGenerate(Sku sku) {
    nextSku = sku;
    return this;
  }

  @Override
  public Sku generate(Title title) {
    return nextSku;
  }
}
