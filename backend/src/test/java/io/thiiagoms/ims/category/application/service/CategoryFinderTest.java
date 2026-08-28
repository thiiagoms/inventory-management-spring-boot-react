package io.thiiagoms.ims.category.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.category.application.exception.CategoryNotFoundException;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository.CategoryMemoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CategoryFinderTest {

  private Id id;

  private CategoryRepository repository;

  private CategoryFinder service;

  @BeforeEach
  void setUp() {
    id = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    repository = new CategoryMemoryRepository();
    service = new CategoryFinder(repository);
  }

  @Test
  void itReturnsACategoryWhenTheProvidedIdExists() {
    var expectedCategory = CategoryFake.start().withId(id).build();
    repository.save(expectedCategory);

    var category = service.byId(id);

    assertEquals(expectedCategory, category);
  }

  @Test
  void itRejectsAnUnknownCategoryId() {
    CategoryNotFoundException exception =
        assertThrows(CategoryNotFoundException.class, () -> service.byId(id));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("Category not found with the provided id.", exception.getMessage());
  }
}
