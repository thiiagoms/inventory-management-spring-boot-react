package io.thiiagoms.ims.user.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.util.regex.Pattern;

public record Email(String value) {

  public static final String FIELD = "email";

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

  public Email {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);
    String email = normalize(value);
    validate(email);
    value = email;
  }

  private String normalize(String value) {
    return value.trim().toLowerCase();
  }

  private void validate(String value) {
    if (!EMAIL_PATTERN.matcher(value).matches()) {
      fail("Invalid e-mail address.");
    }
  }

  private void fail(String message) {
    throw InvalidDomainArgumentException.with(message, FIELD);
  }
}
