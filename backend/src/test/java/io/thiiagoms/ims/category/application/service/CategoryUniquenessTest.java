package io.thiiagoms.ims.category.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.category.application.exception.CategoryTitleAlreadyExistsException;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository.CategoryMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CategoryUniquenessTest {

  private Title title;

  private CategoryRepository repository;

  private CategoryUniqueness service;

  @BeforeEach
  void setUp() {
    title = new Title("Office");
    repository = new CategoryMemoryRepository();
    service = new CategoryUniqueness(repository);
  }

  @Test
  void itAllowsAnAvailableTitle() {
    assertDoesNotThrow(() -> service.ensureTitleIsAvailable(title));
  }

  @Test
  void itRejectsATitleAlreadyOwnedByACategory() {
    repository.save(CategoryFake.start().withTitle(title).build());

    CategoryTitleAlreadyExistsException exception =
        assertThrows(
            CategoryTitleAlreadyExistsException.class, () -> service.ensureTitleIsAvailable(title));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals("A category with this title already exists.", exception.getMessage());
  }
}
