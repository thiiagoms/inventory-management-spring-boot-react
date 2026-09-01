package io.thiiagoms.ims.category.application.usecase.retrieve;

import io.thiiagoms.ims.category.application.dto.CategoryOutput;
import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public class RetrieveCategory {
  private final CategoryFinder finder;

  public RetrieveCategory(CategoryFinder finder) {
    this.finder = finder;
  }

  public CategoryOutput execute(Id id) {
    return CategoryOutput.from(finder.byId(id));
  }
}
