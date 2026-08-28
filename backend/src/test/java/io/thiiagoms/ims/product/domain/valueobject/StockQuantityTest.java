package io.thiiagoms.ims.product.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StockQuantityTest {

  @Test
  void itAcceptsAPositiveQuantity() {
    assertEquals(10, new StockQuantity(10).value());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  void itRejectsNonPositiveQuantities(int value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new StockQuantity(value));

    assertEquals(StockQuantity.FIELD, exception.getField());
  }
}
