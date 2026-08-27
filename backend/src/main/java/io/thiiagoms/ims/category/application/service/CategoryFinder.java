package io.thiiagoms.ims.category.application.service;

import io.thiiagoms.ims.category.application.exception.CategoryNotFoundException;
import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public class CategoryFinder {
  private final CategoryRepository repository;

  public CategoryFinder(CategoryRepository repository) {
    this.repository = repository;
  }

  public Category byId(Id id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                CategoryNotFoundException.with(
                    "Category not found with the provided id.", Id.FIELD));
  }
}
