package io.thiiagoms.ims.product.domain;

import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;

@FunctionalInterface
public interface SkuGenerator {
  Sku generate(Title title);
}
