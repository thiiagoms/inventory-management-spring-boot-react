package io.thiiagoms.ims.product.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class TitleTest {

  @ParameterizedTest
  @CsvSource(
      value = {
        "   home   office   ;Home Office",
        "  mArY-jANe   o'coNNor  ;Mary-Jane O'Connor",
        "product.category;Product.Category"
      },
      delimiter = ';')
  void itUsesTheSameNormalizationAsCategoryTitle(String value, String expected) {
    assertEquals(expected, new Title(value).value());
  }

  @ParameterizedTest
  @ValueSource(strings = {"Office 123", "Office@Home", "Office#Products", "Food & Drinks"})
  void itUsesTheSameAllowedCharactersAsCategoryTitle(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Title(value));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals(
        "Title must contain only letters, spaces, apostrophes, dots, and hyphens.",
        exception.getMessage());
  }
}
