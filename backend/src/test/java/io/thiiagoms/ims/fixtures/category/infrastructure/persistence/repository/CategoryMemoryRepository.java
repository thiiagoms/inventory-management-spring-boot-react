package io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository;

import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.pagination.Page;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Comparator;
import java.util.LinkedHashMap;
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

  public Page<Category> findAll(Pagination pagination) {
    var allCategories =
        categories.values().stream()
            .sorted(Comparator.comparing(category -> category.title().value()))
            .toList();
    long offset = (long) pagination.page() * pagination.size();
    int fromIndex = (int) Math.min(offset, allCategories.size());
    int toIndex = Math.min(fromIndex + pagination.size(), allCategories.size());
    int totalPages =
        allCategories.isEmpty()
            ? 0
            : (int) Math.ceil((double) allCategories.size() / pagination.size());

    return new Page<>(
        allCategories.subList(fromIndex, toIndex),
        pagination.page(),
        pagination.size(),
        allCategories.size(),
        totalPages,
        pagination.page() == 0,
        pagination.page() >= totalPages - 1);
  }

  public void save(Category category) {
    categories.put(category.id(), category);
  }

  public void destroy(Id id) {
    categories.remove(id);
  }
}
