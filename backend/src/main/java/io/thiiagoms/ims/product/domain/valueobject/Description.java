package io.thiiagoms.ims.product.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public record Description(String value) {

  public static final String FIELD = "description";

  private static final int MIN_LENGTH = 3;
  private static final int MAX_LENGTH = 255;
  private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s+");

  public Description {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);
    ensureUtf8Encodable(value);

    value = MULTIPLE_SPACES.matcher(value.trim()).replaceAll(" ");
    int length = value.codePointCount(0, value.length());
    if (length < MIN_LENGTH || length > MAX_LENGTH) {
      throw fail(
          "Description must be between %d and %d characters long."
              .formatted(MIN_LENGTH, MAX_LENGTH));
    }
  }

  private static void ensureUtf8Encodable(String value) {
    if (!StandardCharsets.UTF_8.newEncoder().canEncode(value)) {
      throw fail("Description could not be normalized due to invalid UTF-8 input.");
    }
  }

  private static InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
