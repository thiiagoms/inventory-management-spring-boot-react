package io.thiiagoms.ims.product.infrastructure.persistence.repository;

import io.thiiagoms.ims.category.infrastructure.persistence.repository.CategoryJpaRepository;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.product.infrastructure.persistence.mapper.ProductMapper;
import io.thiiagoms.ims.shared.domain.pagination.Page;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
  private final ProductJpaRepository repository;
  private final CategoryJpaRepository categoryRepository;

  public ProductRepositoryImpl(
      ProductJpaRepository repository, CategoryJpaRepository categoryRepository) {
    this.repository = repository;
    this.categoryRepository = categoryRepository;
  }

  public Optional<Product> findById(Id id) {
    return repository.findById(UUID.fromString(id.value())).map(ProductMapper::toDomain);
  }

  public Optional<Product> findByTitle(Title title) {
    return repository.findByTitle(title.value()).map(ProductMapper::toDomain);
  }

  public Optional<Product> findBySku(Sku sku) {
    return repository.findBySku(sku.value()).map(ProductMapper::toDomain);
  }

  public Page<Product> findAll(Pagination pagination) {
    var pageable =
        PageRequest.of(pagination.page(), pagination.size(), Sort.by("title").ascending());
    var products = repository.findAll(pageable);

    return new Page<>(
        products.getContent().stream().map(ProductMapper::toDomain).toList(),
        products.getNumber(),
        products.getSize(),
        products.getTotalElements(),
        products.getTotalPages(),
        products.isFirst(),
        products.isLast());
  }

  public void save(Product product) {
    var category =
        categoryRepository.getReferenceById(UUID.fromString(product.categoryId().value()));
    repository.save(ProductMapper.toPersistence(product, category));
  }
}
