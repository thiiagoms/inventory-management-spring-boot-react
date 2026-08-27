package io.thiiagoms.ims.category.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class TitleTest {

  private static final String LENGTH_MESSAGE =
      "Title value must be between 3 and 250 characters long.";

  @ParameterizedTest(name = "[{index}] should create title with valid value: {0}")
  @ValueSource(strings = {"Office", "José D'Ávila", "Home-Office", "Mary Jane A. Watson"})
  void shouldCreateTitleWithValidValue(String value) {
    Title title = new Title(value);

    assertEquals(value, title.value());
  }

  @ParameterizedTest(name = "[{index}] should normalize title")
  @CsvSource(
      value = {
        "   home   office   ;Home Office",
        "  mArY-jANe   o'coNNor  ;Mary-Jane O'Connor",
        "product.category;Product.Category"
      },
      delimiter = ';')
  void shouldNormalizeTitleValue(String value, String expected) {
    Title title = new Title(value);

    assertEquals(expected, title.value());
  }

  @Test
  void shouldAcceptMinimumBoundaryLength() {
    Title title = new Title("Abc");

    assertEquals("Abc", title.value());
  }

  @Test
  void shouldAcceptMaximumBoundaryLength() {
    String raw = "a".repeat(250);
    String expected = "A" + "a".repeat(249);

    Title title = new Title(raw);

    assertEquals(expected, title.value());
  }

  @ParameterizedTest(name = "[{index}] should throw when title is null/empty/blank: [{0}]")
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   "})
  void shouldThrowWhenTitleIsNullEmptyOrBlank(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Title(value));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals("The field 'title' cannot be null, empty or blank.", exception.getMessage());
  }

  @ParameterizedTest(name = "[{index}] should throw when title has invalid chars: {0}")
  @ValueSource(strings = {"Office 123", "Office@Home", "Office#Products", "Food & Drinks"})
  void shouldThrowWhenTitleHasInvalidChars(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Title(value));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals(
        "Title must contain only letters, spaces, apostrophes, dots, and hyphens.",
        exception.getMessage());
  }

  @Test
  void shouldThrowWhenTitleCannotBeEncodedAsUtf8() {
    String value = "Office \uD800 Products";

    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Title(value));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals(
        "Title could not be normalized due to invalid UTF-8 input.", exception.getMessage());
  }

  @Test
  void shouldThrowWhenTitleLengthIsBelowMinimum() {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Title("Ab"));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals(LENGTH_MESSAGE, exception.getMessage());
  }

  @Test
  void shouldThrowWhenTitleLengthIsAboveMaximum() {
    String value = "a".repeat(251);

    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Title(value));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals(LENGTH_MESSAGE, exception.getMessage());
  }
}
