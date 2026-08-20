package io.thiiagoms.ims.user.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.util.regex.Pattern;

public record PasswordPlain(String value) {

  public static final String FIELD = "password";

  private static final int MIN_LENGTH = 8;

  private static final Pattern PASSWORD_PATTERN =
      Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s])\\S+$");

  public PasswordPlain {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);
    validate(value);
  }

  private void validate(String password) {
    if (!hasMinimumLength(password) || !matchesPattern(password)) {
      var message =
          "Password must be at least 8 characters and include uppercase, lowercase,"
              + " digit, and special character.";
      fail(message);
    }
  }

  private boolean hasMinimumLength(String password) {
    return password.length() >= MIN_LENGTH;
  }

  private boolean matchesPattern(String password) {
    return PASSWORD_PATTERN.matcher(password).matches();
  }

  @Override
  public String toString() {
    return "{*******************}";
  }

  private void fail(String message) {
    throw InvalidDomainArgumentException.with(message, FIELD);
  }
}
