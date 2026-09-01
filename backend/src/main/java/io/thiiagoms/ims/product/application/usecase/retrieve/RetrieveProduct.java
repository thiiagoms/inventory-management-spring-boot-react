package io.thiiagoms.ims.product.application.usecase.retrieve;

import io.thiiagoms.ims.product.application.dto.ProductOutput;
import io.thiiagoms.ims.product.application.service.ProductFinder;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public class RetrieveProduct {

  private final ProductFinder finder;

  public RetrieveProduct(ProductFinder finder) {
    this.finder = finder;
  }

  public ProductOutput execute(Id id) {
    return ProductOutput.from(finder.byId(id));
  }
}
