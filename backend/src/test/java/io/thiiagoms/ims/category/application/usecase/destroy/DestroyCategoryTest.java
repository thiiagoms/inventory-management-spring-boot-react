package io.thiiagoms.ims.category.application.usecase.destroy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.category.application.exception.CategoryNotFoundException;
import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository.CategoryMemoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DestroyCategoryTest {
  private Id id;
  private CategoryRepository repository;
  private DestroyCategory useCase;

  @BeforeEach
  void setUp() {
    id = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    repository = new CategoryMemoryRepository();
    useCase = new DestroyCategory(new CategoryFinder(repository), repository);
  }

  @Test
  void itDestroysAnExistingCategory() {
    repository.save(CategoryFake.start().withId(id).build());

    useCase.execute(id);

    assertTrue(repository.findById(id).isEmpty());
  }

  @Test
  void itRejectsDestroyingAnUnknownCategory() {
    CategoryNotFoundException exception =
        assertThrows(CategoryNotFoundException.class, () -> useCase.execute(id));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("Category not found with the provided id.", exception.getMessage());
  }
}
