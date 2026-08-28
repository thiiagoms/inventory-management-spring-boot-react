package io.thiiagoms.ims.product.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public record ImageUrl(String value) {

  public static final String FIELD = "imageUrl";

  private static final int MAX_LENGTH = 255;

  public ImageUrl {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);

    value = value.trim();
    validate(value);
  }

  private static void validate(String value) {
    if (value.length() > MAX_LENGTH) {
      throw fail("Image URL must not exceed %d characters.".formatted(MAX_LENGTH));
    }

    try {
      URI uri = new URI(value);
      String scheme = uri.getScheme();
      boolean supportedScheme =
          scheme != null
              && (scheme.toLowerCase(Locale.ROOT).equals("http")
                  || scheme.toLowerCase(Locale.ROOT).equals("https"));

      if (!supportedScheme || uri.getHost() == null || uri.getHost().isBlank()) {
        throw fail("Image URL must be an absolute HTTP or HTTPS URL.");
      }
    } catch (URISyntaxException exception) {
      throw fail("Image URL must be an absolute HTTP or HTTPS URL.");
    }
  }

  private static InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
