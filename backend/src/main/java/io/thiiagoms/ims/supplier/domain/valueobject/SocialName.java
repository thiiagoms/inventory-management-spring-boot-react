package io.thiiagoms.ims.supplier.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.util.regex.Pattern;

public record SocialName(String value) {
  public static final String FIELD = "socialName";
  private static final int MAX_LENGTH = 250;
  private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s+");

  public SocialName {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);
    value = MULTIPLE_SPACES.matcher(value.trim()).replaceAll(" ");
    if (value.length() > MAX_LENGTH) {
      throw InvalidDomainArgumentException.with(
          "Social name must contain at most %d characters.".formatted(MAX_LENGTH), FIELD);
    }
  }
}
