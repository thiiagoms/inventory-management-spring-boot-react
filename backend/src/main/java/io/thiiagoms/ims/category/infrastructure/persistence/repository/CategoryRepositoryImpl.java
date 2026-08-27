package io.thiiagoms.ims.category.infrastructure.persistence.repository;

import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.category.infrastructure.persistence.mapper.CategoryMapper;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
  private final CategoryJpaRepository repository;

  public CategoryRepositoryImpl(CategoryJpaRepository repository) {
    this.repository = repository;
  }

  public Optional<Category> findById(Id id) {
    return repository.findById(UUID.fromString(id.value())).map(CategoryMapper::toDomain);
  }

  public Optional<Category> findByTitle(Title title) {
    return repository.findByTitle(title.value()).map(CategoryMapper::toDomain);
  }

  public List<Category> findAll() {
    return repository.findAll().stream().map(CategoryMapper::toDomain).toList();
  }

  public void save(Category category) {
    repository.save(CategoryMapper.toPersistence(category));
  }

  public void destroy(Id id) {
    repository.deleteById(UUID.fromString(id.value()));
  }
}
