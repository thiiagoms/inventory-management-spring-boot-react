package io.thiiagoms.ims.category.application.usecase.list;

import io.thiiagoms.ims.category.application.dto.CategoryOutput;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import java.util.List;

public class ListCategories {
  private final CategoryRepository repository;

  public ListCategories(CategoryRepository repository) {
    this.repository = repository;
  }

  public List<CategoryOutput> execute() {
    return repository.findAll().stream().map(CategoryOutput::from).toList();
  }
}
