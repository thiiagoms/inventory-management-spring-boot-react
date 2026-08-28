package io.thiiagoms.ims.product.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Price(BigDecimal value) {

  public static final String FIELD = "price";

  private static final int SCALE = 2;
  private static final int MAX_INTEGER_DIGITS = 36;

  public Price {
    Guard.againstNull(FIELD, value);

    if (value.signum() <= 0) {
      throw fail("Price must be greater than zero.");
    }

    BigDecimal normalized = value.stripTrailingZeros();
    if (Math.max(0, normalized.scale()) > SCALE) {
      throw fail("Price must have at most two decimal places.");
    }

    int integerDigits = Math.max(0, normalized.precision() - normalized.scale());
    if (integerDigits > MAX_INTEGER_DIGITS) {
      throw fail("Price exceeds the supported monetary range.");
    }

    value = normalized.setScale(SCALE, RoundingMode.UNNECESSARY);
  }

  private static InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
