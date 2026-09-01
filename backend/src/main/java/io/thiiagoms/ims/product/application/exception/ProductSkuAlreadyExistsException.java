package io.thiiagoms.ims.product.application.exception;

import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.shared.application.exception.ResourceAlreadyExistsException;

public final class ProductSkuAlreadyExistsException extends ResourceAlreadyExistsException {
  private ProductSkuAlreadyExistsException(String message, String field) {
    super(message, field);
  }

  public static ProductSkuAlreadyExistsException create() {
    return new ProductSkuAlreadyExistsException(
        "A product with this SKU already exists.", Sku.FIELD);
  }
}
