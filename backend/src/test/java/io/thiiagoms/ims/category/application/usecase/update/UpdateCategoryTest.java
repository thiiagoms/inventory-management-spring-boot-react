package io.thiiagoms.ims.category.application.usecase.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.category.application.exception.CategoryNotChangedException;
import io.thiiagoms.ims.category.application.exception.CategoryNotFoundException;
import io.thiiagoms.ims.category.application.exception.CategoryTitleAlreadyExistsException;
import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.category.application.service.CategoryUniqueness;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository.CategoryMemoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpdateCategoryTest {

  private Id id;

  private CategoryRepository repository;

  private UpdateCategory useCase;

  @BeforeEach
  void setUp() {
    id = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    repository = new CategoryMemoryRepository();
    useCase =
        new UpdateCategory(
            new CategoryFinder(repository), repository, new CategoryUniqueness(repository));
  }

  @Test
  void itUpdatesTheEntireCategory() {
    repository.save(CategoryFake.start().withId(id).build());
    var data =
        new UpdateCategoryData(
            id,
            Optional.of(new Title("Home office")),
            Optional.of(new Description("Products for home offices")));

    var category = useCase.execute(data);

    assertEquals(data.title().orElseThrow().value(), category.title());
    assertEquals(data.description().orElseThrow().value(), category.description());
  }

  @Test
  void itUpdatesOnlyTheCategoryTitle() {
    var searchableCategory = CategoryFake.start().withId(id).build();
    repository.save(searchableCategory);
    var data = new UpdateCategoryData(id, Optional.of(new Title("Home office")), Optional.empty());

    var category = useCase.execute(data);

    assertEquals(data.title().orElseThrow().value(), category.title());
    assertEquals(searchableCategory.description().value(), category.description());
  }

  @Test
  void itUpdatesOnlyTheCategoryDescription() {
    var searchableCategory = CategoryFake.start().withId(id).build();
    repository.save(searchableCategory);
    var data =
        new UpdateCategoryData(
            id, Optional.empty(), Optional.of(new Description("Products for home offices")));

    var category = useCase.execute(data);

    assertEquals(searchableCategory.title().value(), category.title());
    assertEquals(data.description().orElseThrow().value(), category.description());
  }

  @Test
  void itRejectsAnUnknownCategoryId() {
    var data = new UpdateCategoryData(id, Optional.empty(), Optional.empty());

    CategoryNotFoundException exception =
        assertThrows(CategoryNotFoundException.class, () -> useCase.execute(data));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("Category not found with the provided id.", exception.getMessage());
  }

  @Test
  void itRejectsATitleAlreadyOwnedByAnotherCategory() {
    var duplicatedTitle = new Title("Kitchen");
    repository.save(CategoryFake.start().withId(id).build());
    repository.save(
        CategoryFake.start()
            .withId(new Id("baa86496-638f-4beb-bc03-de2f7589ad63"))
            .withTitle(duplicatedTitle)
            .build());
    var data = new UpdateCategoryData(id, Optional.of(duplicatedTitle), Optional.empty());

    CategoryTitleAlreadyExistsException exception =
        assertThrows(CategoryTitleAlreadyExistsException.class, () -> useCase.execute(data));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals("A category with this title already exists.", exception.getMessage());
  }

  @Test
  void itRejectsAnUpdateWhenNothingChanges() {
    var category = CategoryFake.start().withId(id).build();
    repository.save(category);
    var data =
        new UpdateCategoryData(
            id, Optional.of(category.title()), Optional.of(category.description()));

    CategoryNotChangedException exception =
        assertThrows(CategoryNotChangedException.class, () -> useCase.execute(data));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("The category was not changed.", exception.getMessage());
  }
}
