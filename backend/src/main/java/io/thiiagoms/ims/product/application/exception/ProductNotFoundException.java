package io.thiiagoms.ims.product.application.exception;

import io.thiiagoms.ims.shared.application.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {

  private ProductNotFoundException(String message, String field) {
    super(message, field);
  }

  public static ProductNotFoundException with(String message, String searchableResource) {
    return new ProductNotFoundException(message, searchableResource);
  }
}
