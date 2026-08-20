package io.thiiagoms.ims.user.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class EmailTest {

  @ParameterizedTest(name = "[{index}] should create e-mail with valid value: {0}")
  @ValueSource(
      strings = {"john.doe@example.com", "john+alias@example.com", "dev_team-123@sub.example.com"})
  void shouldCreateEmailWithValidValue(String value) {
    Email email = new Email(value);
    assertEquals(value, email.value());
  }

  @ParameterizedTest(name = "[{index}] should normalize e-mail value: {0}")
  @ValueSource(strings = {"  John.Doe@Example.COM  ", "  DEV+TEAM@MAIL.COM"})
  void shouldNormalizeEmailValue(String value) {
    Email email = new Email(value);
    assertEquals(value.trim().toLowerCase(), email.value());
  }

  @ParameterizedTest(name = "[{index}] should throw when e-mail is null/empty/blank: [{0}]")
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   "})
  void shouldThrowWhenEmailIsNullEmptyOrBlank(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Email(value));

    assertEquals(Email.FIELD, exception.getField());
    assertEquals("The field 'email' cannot be null, empty or blank.", exception.getMessage());
  }

  @ParameterizedTest(name = "[{index}] should throw when e-mail has invalid format: {0}")
  @ValueSource(strings = {"invalid-email", "john@", "@example.com", "john doe@example.com"})
  void shouldThrowWhenEmailHasInvalidFormat(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Email(value));

    assertEquals(Email.FIELD, exception.getField());
    assertEquals("Invalid e-mail address.", exception.getMessage());
  }
}
