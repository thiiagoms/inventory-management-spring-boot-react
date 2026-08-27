package io.thiiagoms.ims.category.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class DescriptionTest {

  private static final String LENGTH_MESSAGE = "Description must be at least 3 characters long.";

  @ParameterizedTest(name = "[{index}] should create description with valid value: {0}")
  @ValueSource(
      strings = {"office products", "products for cafés", "tools, equipment & accessories", "123"})
  void shouldCreateDescriptionWithValidValue(String value) {
    Description description = new Description(value);

    assertEquals(value, description.value());
  }

  @ParameterizedTest(name = "[{index}] should normalize description")
  @CsvSource(
      value = {
        "  OFFICE   products ;office products",
        " Products FOR   Home Offices ;products for home offices"
      },
      delimiter = ';')
  void shouldNormalizeDescriptionValue(String value, String expected) {
    Description description = new Description(value);

    assertEquals(expected, description.value());
  }

  @Test
  void shouldAcceptMinimumBoundaryLength() {
    Description description = new Description("Abc");

    assertEquals("abc", description.value());
  }

  @ParameterizedTest(name = "[{index}] should throw when description is null/empty/blank: [{0}]")
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   "})
  void shouldThrowWhenDescriptionIsNullEmptyOrBlank(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Description(value));

    assertEquals(Description.FIELD, exception.getField());
    assertEquals("The field 'description' cannot be null, empty or blank.", exception.getMessage());
  }

  @ParameterizedTest(name = "[{index}] should throw when description is below minimum: [{0}]")
  @ValueSource(strings = {"a", "ab"})
  void shouldThrowWhenDescriptionLengthIsBelowMinimum(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Description(value));

    assertEquals(Description.FIELD, exception.getField());
    assertEquals(LENGTH_MESSAGE, exception.getMessage());
  }

  @Test
  void shouldThrowWhenDescriptionCannotBeEncodedAsUtf8() {
    String value = "Office \uD800 products";

    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Description(value));

    assertEquals(Description.FIELD, exception.getField());
    assertEquals(
        "Description could not be normalized due to invalid UTF-8 input.", exception.getMessage());
  }
}
