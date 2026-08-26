package io.thiiagoms.ims.shared.domain.support;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;

public final class Guard {

  private static final String DEFAULT_MESSAGE = "The field '%s' cannot be null, empty or blank.";

  private Guard() {}

  public static void againstNull(String field, Object value) {
    if (value == null) {
      throw fail(field);
    }
  }

  public static void againstNullOrEmptyOrBlank(String field, String value) {
    if (value == null || value.isBlank()) {
      throw fail(field);
    }
  }

  private static InvalidDomainArgumentException fail(String field) {
    return InvalidDomainArgumentException.with(DEFAULT_MESSAGE.formatted(field), field);
  }
}
