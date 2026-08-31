package io.thiiagoms.ims.product.application.exception;

import io.thiiagoms.ims.shared.application.exception.ResourceNotChangedException;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public final class ProductNotChangedException extends ResourceNotChangedException {
  private ProductNotChangedException() {
    super("The product was not changed.", Id.FIELD);
  }

  public static ProductNotChangedException create() {
    return new ProductNotChangedException();
  }
}
