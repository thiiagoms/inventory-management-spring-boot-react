package io.thiiagoms.ims.category.application.usecase.destroy;

import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.springframework.transaction.annotation.Transactional;

public class DestroyCategory {
  private final CategoryFinder finder;
  private final CategoryRepository repository;

  public DestroyCategory(CategoryFinder finder, CategoryRepository repository) {
    this.finder = finder;
    this.repository = repository;
  }

  @Transactional
  public void execute(Id id) {
    finder.byId(id);
    repository.destroy(id);
  }
}
