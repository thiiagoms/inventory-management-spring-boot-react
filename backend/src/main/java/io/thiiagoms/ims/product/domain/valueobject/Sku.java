package io.thiiagoms.ims.product.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.util.Locale;
import java.util.regex.Pattern;

public record Sku(String value) {

  public static final String FIELD = "sku";

  private static final int MIN_LENGTH = 3;
  private static final int MAX_LENGTH = 100;
  private static final Pattern FORMAT = Pattern.compile("^[A-Z0-9]+(?:[-_.][A-Z0-9]+)*$");

  public Sku {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);

    value = value.trim().toUpperCase(Locale.ROOT);
    if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
      throw fail(
          "SKU must be between %d and %d characters long.".formatted(MIN_LENGTH, MAX_LENGTH));
    }

    if (!FORMAT.matcher(value).matches()) {
      throw fail(
          "SKU must contain only letters and numbers, optionally separated by "
              + "hyphens, dots, or underscores.");
    }
  }

  private static InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
