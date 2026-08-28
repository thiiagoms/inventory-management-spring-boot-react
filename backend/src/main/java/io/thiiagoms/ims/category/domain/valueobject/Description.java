package io.thiiagoms.ims.category.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Pattern;

public record Description(String value) {

  public static final String FIELD = "description";

  private static final int MIN_LENGTH = 3;

  private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s+");

  public Description {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);

    String description = normalize(value);
    validate(description);
    value = description;
  }

  private String normalize(String description) {
    ensureUtf8Encodable(description);

    return MULTIPLE_SPACES.matcher(description.trim().toLowerCase(Locale.ROOT)).replaceAll(" ");
  }

  private void ensureUtf8Encodable(String description) {
    if (StandardCharsets.UTF_8.newEncoder().canEncode(description)) {
      return;
    }

    throw fail("Description could not be normalized due to invalid UTF-8 input.");
  }

  private void validate(String description) {
    ensureDescriptionLengthIsValid(description);
  }

  private void ensureDescriptionLengthIsValid(String description) {
    int length = description.length();

    if (length >= MIN_LENGTH) {
      return;
    }

    throw fail("Description must be at least %d characters long.".formatted(MIN_LENGTH));
  }

  private InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
