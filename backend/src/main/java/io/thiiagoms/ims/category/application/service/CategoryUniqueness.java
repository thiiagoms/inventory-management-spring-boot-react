package io.thiiagoms.ims.category.application.service;

import io.thiiagoms.ims.category.application.exception.CategoryTitleAlreadyExistsException;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Title;

public class CategoryUniqueness {
  private final CategoryRepository repository;

  public CategoryUniqueness(CategoryRepository repository) {
    this.repository = repository;
  }

  public void ensureTitleIsAvailable(Title title) {
    if (repository.findByTitle(title).isPresent()) {
      throw CategoryTitleAlreadyExistsException.create();
    }
  }
}
