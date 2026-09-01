package io.thiiagoms.ims.shared.domain.pagination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import org.junit.jupiter.api.Test;

class PaginationTest {
  @Test
  void itRejectsANegativePage() {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Pagination(-1, 20));

    assertEquals("page", exception.getField());
  }

  @Test
  void itRejectsASizeOutsideTheSupportedRange() {
    InvalidDomainArgumentException exception =
        assertThrows(InvalidDomainArgumentException.class, () -> new Pagination(0, 101));

    assertEquals("size", exception.getField());
  }
}
