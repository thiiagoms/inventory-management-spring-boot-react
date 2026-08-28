package io.thiiagoms.ims.product.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ImageUrlTest {

  @Test
  void itAcceptsAndTrimsAnAbsoluteHttpsUrl() {
    assertEquals(
        "https://example.com/chair.png", new ImageUrl("  https://example.com/chair.png  ").value());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"chair.png", "/images/chair.png", "ftp://example.com/chair.png", "https://"})
  void itRejectsNonHttpOrRelativeUrls(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new ImageUrl(value));

    assertEquals(ImageUrl.FIELD, exception.getField());
  }
}
