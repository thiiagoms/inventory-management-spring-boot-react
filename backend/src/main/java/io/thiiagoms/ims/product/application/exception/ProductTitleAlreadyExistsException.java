package io.thiiagoms.ims.product.application.exception;

import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.application.exception.ResourceAlreadyExistsException;

public final class ProductTitleAlreadyExistsException extends ResourceAlreadyExistsException {
  private ProductTitleAlreadyExistsException(String message, String field) {
    super(message, field);
  }

  public static ProductTitleAlreadyExistsException create() {
    return new ProductTitleAlreadyExistsException(
        "A product with this title already exists.", Title.FIELD);
  }
}
