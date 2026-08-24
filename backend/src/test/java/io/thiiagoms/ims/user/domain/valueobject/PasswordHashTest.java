package io.thiiagoms.ims.user.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class PasswordHashTest {

  @ParameterizedTest(name = "[{index}] should create password hash with valid prefix")
  @ValueSource(
      strings = {
        "$2a$10$C6UzMDM.H6dfI/f/IKxGhu",
        "$2b$12$abcdefghijklmnopqrstuv",
        "$2y$08$1234567890123456789012"
      })
  void shouldCreatePasswordHashWithValidPrefix(String value) {
    PasswordHash password = new PasswordHash(value);
    assertEquals(value, password.value());
  }

  @ParameterizedTest(name = "[{index}] should mask password hash in toString")
  @ValueSource(strings = {"$2a$10$C6UzMDM.H6dfI/f/IKxGhu"})
  void shouldMaskPasswordHashInToString(String value) {
    PasswordHash password = new PasswordHash(value);
    assertEquals("{*******************}", password.toString());
  }

  @ParameterizedTest(name = "[{index}] should throw when password hash is null/empty/blank: [{0}]")
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   "})
  void shouldThrowWhenPasswordHashIsNullEmptyOrBlank(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new PasswordHash(value));

    assertEquals(PasswordPlain.FIELD, exception.getField());
    assertEquals("The field 'password' cannot be null, empty or blank.", exception.getMessage());
  }

  @ParameterizedTest(name = "[{index}] should throw when password hash has invalid prefix: {0}")
  @ValueSource(
      strings = {"$2x$10$C6UzMDM.H6dfI/f/IKxGhu", "$3a$10$C6UzMDM.H6dfI/f/IKxGhu", "plain-text"})
  void shouldThrowWhenPasswordHashHasInvalidPrefix(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new PasswordHash(value));

    assertEquals(PasswordHash.FIELD, exception.getField());
    assertEquals("Invalid password hash.", exception.getMessage());
  }
}
