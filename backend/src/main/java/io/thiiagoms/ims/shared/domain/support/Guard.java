package io.thiiagoms.ims.shared.domain.support;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;

public final class Guard {

  private Guard() {}

  public static void againstNull(String field, Object value) {
    if (value == null) {
      fail(field);
    }
  }

  public static void againstNullOrEmptyOrBlank(String field, String value) {
    if (value == null || value.isBlank()) {
      fail(field);
    }
  }

  private static String fail(String field) {
    var message = String.format("The field '%s' cannot be null, empty or blank.", field);
    throw InvalidDomainArgumentException.with(message, field);
  }
}
