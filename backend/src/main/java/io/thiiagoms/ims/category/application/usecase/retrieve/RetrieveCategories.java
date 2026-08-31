package io.thiiagoms.ims.category.application.usecase.retrieve;

import io.thiiagoms.ims.category.application.dto.CategoryPageOutput;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;

public class RetrieveCategories {
  private final CategoryRepository repository;

  public RetrieveCategories(CategoryRepository repository) {
    this.repository = repository;
  }

  public CategoryPageOutput execute(Pagination pagination) {
    return CategoryPageOutput.from(repository.findAll(pagination));
  }
}
