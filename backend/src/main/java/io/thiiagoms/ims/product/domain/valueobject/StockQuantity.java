package io.thiiagoms.ims.product.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;

public record StockQuantity(Integer value) {

  public static final String FIELD = "stockQuantity";

  public StockQuantity {
    Guard.againstNull(FIELD, value);

    if (value <= 0) {
      throw InvalidDomainArgumentException.with("Stock quantity must be greater than zero.", FIELD);
    }
  }
}
