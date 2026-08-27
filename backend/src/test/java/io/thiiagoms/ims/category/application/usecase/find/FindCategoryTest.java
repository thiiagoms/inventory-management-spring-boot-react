package io.thiiagoms.ims.category.application.usecase.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.category.application.exception.CategoryNotFoundException;
import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository.CategoryMemoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FindCategoryTest {
  private Id id;
  private CategoryRepository repository;
  private FindCategory useCase;

  @BeforeEach
  void setUp() {
    id = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    repository = new CategoryMemoryRepository();
    useCase = new FindCategory(new CategoryFinder(repository));
  }

  @Test
  void itReturnsCategoryDataWhenTheCategoryExists() {
    var expectedCategory = CategoryFake.start().withId(id).build();
    repository.save(expectedCategory);

    var category = useCase.execute(id);

    assertEquals(expectedCategory.id().value(), category.id());
    assertEquals(expectedCategory.title().value(), category.title());
    assertEquals(expectedCategory.description().value(), category.description());
  }

  @Test
  void itRejectsAnUnknownCategoryId() {
    CategoryNotFoundException exception =
        assertThrows(CategoryNotFoundException.class, () -> useCase.execute(id));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("Category not found with the provided id.", exception.getMessage());
  }
}
