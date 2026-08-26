package io.thiiagoms.ims.user.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;

public record PasswordHash(String value) {

  public static final String FIELD = "password";

  public PasswordHash {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);
    validate(value);
  }

  private void validate(String value) {
    boolean valid =
        value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");

    if (!valid) {
      throw fail("Invalid password hash.");
    }
  }

  @Override
  public String toString() {
    return "{*******************}";
  }

  private InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
