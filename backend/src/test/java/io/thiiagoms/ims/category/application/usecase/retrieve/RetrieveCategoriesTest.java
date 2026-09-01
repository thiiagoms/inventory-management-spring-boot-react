package io.thiiagoms.ims.category.application.usecase.retrieve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository.CategoryMemoryRepository;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RetrieveCategoriesTest {
  private CategoryRepository repository;
  private RetrieveCategories useCase;

  @BeforeEach
  void setUp() {
    repository = new CategoryMemoryRepository();
    useCase = new RetrieveCategories(repository);
  }

  @Test
  void itRetrievesTheFirstCategoryPage() {
    saveCategories();

    var output = useCase.execute(new Pagination(0, 2));

    assertEquals(2, output.content().size());
    assertEquals("Kitchen", output.content().get(0).title());
    assertEquals("Office", output.content().get(1).title());
    assertEquals(0, output.page());
    assertEquals(2, output.size());
    assertEquals(3, output.totalElements());
    assertEquals(2, output.totalPages());
    assertTrue(output.first());
    assertFalse(output.last());
  }

  @Test
  void itRetrievesTheLastCategoryPage() {
    saveCategories();

    var output = useCase.execute(new Pagination(1, 2));

    assertEquals(1, output.content().size());
    assertEquals("Warehouse", output.content().getFirst().title());
    assertFalse(output.first());
    assertTrue(output.last());
  }

  @Test
  void itRetrievesAnEmptyCategoryPage() {
    var output = useCase.execute(new Pagination(0, 20));

    assertTrue(output.content().isEmpty());
    assertEquals(0, output.totalElements());
    assertEquals(0, output.totalPages());
    assertTrue(output.first());
    assertTrue(output.last());
  }

  private void saveCategories() {
    repository.save(CategoryFake.start().build());
    repository.save(
        CategoryFake.start()
            .withId(new Id("baa86496-638f-4beb-bc03-de2f7589ad63"))
            .withTitle(new Title("Warehouse"))
            .build());
    repository.save(
        CategoryFake.start()
            .withId(new Id("430e7bc1-59b9-472e-ae21-3cd90cde7caa"))
            .withTitle(new Title("Kitchen"))
            .build());
  }
}
