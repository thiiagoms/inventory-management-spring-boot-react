package io.thiiagoms.ims.product.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DescriptionTest {

  @Test
  void itNormalizesSurroundingAndRepeatedWhitespace() {
    assertEquals("Ergonomic office chair", new Description("  Ergonomic   office chair  ").value());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "ab"})
  void itRejectsMissingOrTooShortDescriptions(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Description(value));

    assertEquals(Description.FIELD, exception.getField());
  }

  @Test
  void itRejectsDescriptionsLongerThanThePersistenceLimit() {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Description("a".repeat(256)));

    assertEquals(Description.FIELD, exception.getField());
  }
}
