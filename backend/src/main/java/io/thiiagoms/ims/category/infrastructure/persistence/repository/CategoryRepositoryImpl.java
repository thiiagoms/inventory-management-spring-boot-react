package io.thiiagoms.ims.category.infrastructure.persistence.repository;

import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.category.infrastructure.persistence.mapper.CategoryMapper;
import io.thiiagoms.ims.shared.domain.pagination.Page;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

  public Page<Category> findAll(Pagination pagination) {
    var pageable =
        PageRequest.of(pagination.page(), pagination.size(), Sort.by("title").ascending());
    var categories = repository.findAll(pageable);

    return new Page<>(
        categories.getContent().stream().map(CategoryMapper::toDomain).toList(),
        categories.getNumber(),
        categories.getSize(),
        categories.getTotalElements(),
        categories.getTotalPages(),
        categories.isFirst(),
        categories.isLast());
  }

  public void save(Category category) {
    repository.save(CategoryMapper.toPersistence(category));
  }

  public void destroy(Id id) {
    repository.deleteById(UUID.fromString(id.value()));
  }
}
