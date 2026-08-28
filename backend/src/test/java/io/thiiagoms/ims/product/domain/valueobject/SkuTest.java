package io.thiiagoms.ims.product.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SkuTest {

  @Test
  void itNormalizesSkuForReliableUniquenessChecks() {
    assertEquals("CHAIR-001", new Sku("  chair-001 ").value());
  }

  @ParameterizedTest
  @ValueSource(strings = {"ab", "CHAIR 001", "CHAIR@001", "-CHAIR", "CHAIR-"})
  void itRejectsInvalidSkus(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Sku(value));

    assertEquals(Sku.FIELD, exception.getField());
  }
}
