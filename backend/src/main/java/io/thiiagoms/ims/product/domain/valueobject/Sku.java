package io.thiiagoms.ims.product.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public record Sku(String value) {

  public static final String FIELD = "sku";

  private static final int MIN_LENGTH = 3;
  private static final int MAX_LENGTH = 100;
  private static final Pattern FORMAT =
      Pattern.compile("^[\\p{L}\\p{N}]+(?:[-_.][\\p{L}\\p{N}]+)*$");
  private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
  private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^\\p{L}\\p{N}]+");

  public Sku {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);

    value = value.trim().toLowerCase(Locale.ROOT);
    if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
      throw fail(
          "SKU must be between %d and %d characters long.".formatted(MIN_LENGTH, MAX_LENGTH));
    }

    if (!FORMAT.matcher(value).matches()) {
      throw fail(
          "SKU must contain only lowercase letters and numbers, optionally separated by "
              + "hyphens, dots, or underscores.");
    }
  }

  public static Sku generate(Title title) {
    Guard.againstNull(Title.FIELD, title);

    String normalized = Normalizer.normalize(title.value(), Normalizer.Form.NFD);
    String name = DIACRITICS.matcher(normalized).replaceAll("").toLowerCase(Locale.ROOT);
    name = NON_ALPHANUMERIC.matcher(name).replaceAll("-").replaceAll("^-|-$", "");

    String uniqueSuffix = UUID.randomUUID() + "-" + System.currentTimeMillis();
    int maximumNameLength = MAX_LENGTH - uniqueSuffix.length() - 1;
    if (name.length() > maximumNameLength) {
      int endIndex = maximumNameLength;
      if (Character.isHighSurrogate(name.charAt(endIndex - 1))) {
        endIndex--;
      }
      name = name.substring(0, endIndex).replaceFirst("-+$", "");
    }

    return new Sku(name + "-" + uniqueSuffix);
  }

  private static InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
