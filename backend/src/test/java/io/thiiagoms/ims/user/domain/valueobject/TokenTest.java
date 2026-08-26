package io.thiiagoms.ims.user.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class TokenTest {

  @ParameterizedTest(name = "[{index}] should create token with valid value")
  @ValueSource(
      strings = {
        "signed-token",
        "opaque_access_token-123",
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyLWlkIn0.signature"
      })
  void shouldCreateTokenWithValidValue(String value) {
    Token token = new Token(value);

    assertEquals(value, token.value());
  }

  @ParameterizedTest(name = "[{index}] should mask token in toString")
  @ValueSource(strings = {"signed-token"})
  void shouldMaskTokenInToString(String value) {
    Token token = new Token(value);

    assertEquals("{*******************}", token.toString());
  }

  @ParameterizedTest(name = "[{index}] should throw when token is null/empty/blank: [{0}]")
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   ", "\t", "\n"})
  void shouldThrowWhenTokenIsNullEmptyOrBlank(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Token(value));

    assertEquals(Token.FIELD, exception.getField());
    assertEquals("The field 'token' cannot be null, empty or blank.", exception.getMessage());
  }
}
