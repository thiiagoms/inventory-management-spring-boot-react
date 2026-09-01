package io.thiiagoms.ims.product.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.List;
import org.junit.jupiter.api.Test;

class CategoryIdsTest {
  private static final Id FIRST = new Id("430e7bc1-59b9-472e-ae21-3cd90cde7caa");
  private static final Id SECOND = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
  private static final Id THIRD = new Id("baa86496-638f-4beb-bc03-de2f7589ad63");

  @Test
  void itAcceptsAtLeastThreeUniqueCategories() {
    var categoryIds = new CategoryIds(List.of(FIRST, SECOND, THIRD));

    assertEquals(List.of(FIRST, SECOND, THIRD), categoryIds.values());
  }

  @Test
  void itRejectsFewerThanThreeCategories() {
    InvalidDomainArgumentException exception =
        assertThrows(
            InvalidDomainArgumentException.class, () -> new CategoryIds(List.of(FIRST, SECOND)));

    assertEquals(CategoryIds.FIELD, exception.getField());
    assertEquals("A product must have at least 3 categories.", exception.getMessage());
  }

  @Test
  void itRejectsDuplicateCategories() {
    InvalidDomainArgumentException exception =
        assertThrows(
            InvalidDomainArgumentException.class,
            () -> new CategoryIds(List.of(FIRST, SECOND, SECOND)));

    assertEquals(CategoryIds.FIELD, exception.getField());
    assertEquals("A product cannot contain duplicate categories.", exception.getMessage());
  }
}
