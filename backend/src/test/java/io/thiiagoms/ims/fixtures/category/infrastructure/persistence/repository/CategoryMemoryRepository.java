package io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository;

import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoryMemoryRepository implements CategoryRepository {
  private final Map<Id, Category> categories = new LinkedHashMap<>();

  public Optional<Category> findById(Id id) {
    return Optional.ofNullable(categories.get(id));
  }

  public Optional<Category> findByTitle(Title title) {
    return categories.values().stream()
        .filter(category -> category.title().equals(title))
        .findFirst();
  }

  public List<Category> findAll() {
    return List.copyOf(categories.values());
  }

  public void save(Category category) {
    categories.put(category.id(), category);
  }

  public void destroy(Id id) {
    categories.remove(id);
  }
}
