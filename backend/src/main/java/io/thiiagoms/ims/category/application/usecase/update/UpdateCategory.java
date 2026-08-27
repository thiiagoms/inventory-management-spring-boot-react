package io.thiiagoms.ims.category.application.usecase.update;

import io.thiiagoms.ims.category.application.dto.CategoryOutput;
import io.thiiagoms.ims.category.application.exception.CategoryNotChangedException;
import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.category.application.service.CategoryUniqueness;
import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

public class UpdateCategory {
  private final CategoryFinder finder;
  private final CategoryRepository repository;
  private final CategoryUniqueness uniqueness;

  public UpdateCategory(
      CategoryFinder finder, CategoryRepository repository, CategoryUniqueness uniqueness) {
    this.finder = finder;
    this.repository = repository;
    this.uniqueness = uniqueness;
  }

  @Transactional
  public CategoryOutput execute(UpdateCategoryData data) {
    var category = finder.byId(data.id());
    boolean changed = updateTitle(category, data) | updateDescription(category, data);
    if (!changed) {
      throw CategoryNotChangedException.create();
    }
    repository.save(category);
    return CategoryOutput.from(category);
  }

  private boolean updateTitle(Category category, UpdateCategoryData data) {
    return data.title()
        .filter(title -> !title.equals(category.title()))
        .map(
            title -> {
              uniqueness.ensureTitleIsAvailable(title);
              category.changeTitleTo(title);
              return true;
            })
        .orElse(false);
  }

  private boolean updateDescription(Category category, UpdateCategoryData data) {
    return data.description()
        .filter(description -> !description.equals(category.description()))
        .map(
            description -> {
              category.changeDescriptionTo(description);
              return true;
            })
        .orElse(false);
  }
}
