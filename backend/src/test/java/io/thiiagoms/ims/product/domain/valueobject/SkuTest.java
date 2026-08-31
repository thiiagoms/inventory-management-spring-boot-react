package io.thiiagoms.ims.product.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SkuTest {

  @Test
  void itNormalizesSkuForReliableUniquenessChecks() {
    assertEquals("chair-001", new Sku("  CHAIR-001 ").value());
  }

  @Test
  void itGeneratesSkuFromTheLowercaseTitleUuidAndCurrentTimestamp() {
    long before = System.currentTimeMillis();

    Sku sku = Sku.generate(new Title("Cadeira Ágil"));

    String[] parts = sku.value().split("-");
    long timestamp = Long.parseLong(parts[parts.length - 1]);
    assertTrue(sku.value().matches("cadeira-agil-[0-9a-f-]{36}-\\d{13}"));
    assertTrue(timestamp >= before && timestamp <= System.currentTimeMillis());
  }

  @Test
  void itGeneratesSkuForANonLatinTitle() {
    assertTrue(Sku.generate(new Title("办公椅子")).value().startsWith("办公椅子-"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"ab", "CHAIR 001", "CHAIR@001", "-CHAIR", "CHAIR-"})
  void itRejectsInvalidSkus(String value) {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Sku(value));

    assertEquals(Sku.FIELD, exception.getField());
  }
}
