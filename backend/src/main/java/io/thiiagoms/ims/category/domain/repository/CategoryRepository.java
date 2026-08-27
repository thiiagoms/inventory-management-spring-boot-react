package io.thiiagoms.ims.category.domain.repository;

import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

  Optional<Category> findById(Id id);

  Optional<Category> findByTitle(Title title);

  List<Category> findAll();

  void save(Category category);

  void destroy(Id id);
}
