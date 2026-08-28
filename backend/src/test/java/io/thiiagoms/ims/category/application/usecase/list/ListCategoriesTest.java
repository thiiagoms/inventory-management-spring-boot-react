package io.thiiagoms.ims.category.application.usecase.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository.CategoryMemoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ListCategoriesTest {
  private CategoryRepository repository;
  private ListCategories useCase;

  @BeforeEach
  void setUp() {
    repository = new CategoryMemoryRepository();
    useCase = new ListCategories(repository);
  }

  @Test
  void itReturnsAllCategories() {
    repository.save(CategoryFake.start().build());
    repository.save(
        CategoryFake.start()
            .withId(new Id("baa86496-638f-4beb-bc03-de2f7589ad63"))
            .withTitle(new Title("Kitchen"))
            .build());

    var categories = useCase.execute();

    assertEquals(2, categories.size());
    assertEquals("Office", categories.get(0).title());
    assertEquals("Kitchen", categories.get(1).title());
  }

  @Test
  void itReturnsAnEmptyListWhenThereAreNoCategories() {
    assertTrue(useCase.execute().isEmpty());
  }
}
