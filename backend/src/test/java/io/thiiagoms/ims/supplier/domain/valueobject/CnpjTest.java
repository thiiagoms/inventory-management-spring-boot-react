package io.thiiagoms.ims.supplier.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;

class CnpjTest {

  @Test
  void itNormalizesAFormattedCnpj() {
    assertEquals("11222333000181", new Cnpj("11.222.333/0001-81").value());
  }

  @Test
  void itRejectsAnInvalidCnpj() {
    var exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Cnpj("12.345.678/0001-90"));

    assertEquals(Cnpj.FIELD, exception.getField());
    assertEquals("CNPJ must be valid.", exception.getMessage());
  }

  @Test
  void itRejectsRepeatedDigits() {
    assertThrows(InvalidDomainArgumentException.class, () -> new Cnpj("11.111.111/1111-11"));
  }
}
