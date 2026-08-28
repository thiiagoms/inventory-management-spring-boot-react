package io.thiiagoms.ims.product.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

  @Test
  void itNormalizesMoneyToTwoDecimalPlaces() {
    assertEquals(new BigDecimal("499.90"), new Price(new BigDecimal("499.9")).value());
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "-0.01", "19.999"})
  void itRejectsNonPositiveOrOverPrecisePrices(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Price(new BigDecimal(value)));

    assertEquals(Price.FIELD, exception.getField());
  }
}
