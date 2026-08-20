package io.thiiagoms.ims.user.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PhoneTest {

  @ParameterizedTest
  @CsvSource({
    "11987654321, 11987654321",
    "'(11) 98765-4321', 11987654321",
    "'(11) 3456-7890', 1134567890"
  })
  void shouldNormalizeValidPhone(String value, String expected) {
    assertEquals(expected, new Phone(value).value());
  }

  @ParameterizedTest
  @ValueSource(strings = {"1198765432a", "1198765#4321"})
  void shouldRejectUnsupportedCharacters(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Phone(value));

    assertEquals(
        "Phone must contain only numbers and formatting characters.", exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"123456789", "123456789012"})
  void shouldRejectInvalidDigitCount(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Phone(value));

    assertEquals("Phone must contain 10 or 11 digits.", exception.getMessage());
  }
}
