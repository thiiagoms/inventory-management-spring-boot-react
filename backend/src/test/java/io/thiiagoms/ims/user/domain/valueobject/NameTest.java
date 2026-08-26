package io.thiiagoms.ims.user.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class NameTest {

  private static final String LENGTH_MESSAGE =
      "Name value must be between 3 and 150 characters long.";

  @ParameterizedTest(name = "[{index}] should create name with valid value: {0}")
  @ValueSource(strings = {"John Doe", "Ana", "José D'Ávila", "Mary-Jane A. Watson"})
  void shouldCreateNameWithValidValue(String value) {
    Name name = new Name(value);
    assertEquals(value, name.value());
  }

  @ParameterizedTest(name = "[{index}] should normalize name")
  @CsvSource(
      value = {
        "   joao   da   silva   ;Joao Da Silva",
        "  mArY-jANe   o'coNNor  ;Mary-Jane O'Connor"
      },
      delimiter = ';')
  void shouldNormalizeNameValue(String value, String expected) {
    Name name = new Name(value);
    assertEquals(expected, name.value());
  }

  @Test
  void shouldAcceptMinimumBoundaryLength() {
    Name name = new Name("Abc");
    assertEquals("Abc", name.value());
  }

  @Test
  void shouldAcceptMaximumBoundaryLength() {
    String raw = "a".repeat(150);
    String expected = "A" + "a".repeat(149);

    Name name = new Name(raw);
    assertEquals(expected, name.value());
  }

  @ParameterizedTest(name = "[{index}] should throw when name is null/empty/blank: [{0}]")
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   "})
  void shouldThrowWhenNameIsNullEmptyOrBlank(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Name(value));

    assertEquals(Name.FIELD, exception.getField());
    assertEquals("The field 'name' cannot be null, empty or blank.", exception.getMessage());
  }

  @ParameterizedTest(name = "[{index}] should throw when name has invalid chars: {0}")
  @ValueSource(strings = {"John123", "Mary@Doe", "Ana#Maria"})
  void shouldThrowWhenNameHasInvalidChars(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Name(value));

    assertEquals(Name.FIELD, exception.getField());
    assertEquals(
        "Name must contain only letters, spaces, apostrophes, dots, and hyphens.",
        exception.getMessage());
  }

  @Test
  void shouldThrowWhenNameCannotBeEncodedAsUtf8() {
    String value = "John \uD800 Doe";

    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Name(value));

    assertEquals(Name.FIELD, exception.getField());
    assertEquals(
        "Name could not be normalized due to invalid UTF-8 input.", exception.getMessage());
  }

  @ParameterizedTest(name = "[{index}] should throw when name length is invalid: {0}")
  @ValueSource(strings = {"Ab"})
  void shouldThrowWhenNameLengthIsInvalidBelowMinimum(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Name(value));

    assertEquals(Name.FIELD, exception.getField());
    assertEquals(LENGTH_MESSAGE, exception.getMessage());
  }

  @Test
  void shouldThrowWhenNameLengthIsInvalidAboveMaximum() {
    String value = "a".repeat(151);

    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Name(value));

    assertEquals(Name.FIELD, exception.getField());
    assertEquals(LENGTH_MESSAGE, exception.getMessage());
  }
}
