package io.thiiagoms.ims.product.infrastructure.persistence.repository;

import io.thiiagoms.ims.category.infrastructure.persistence.repository.CategoryJpaRepository;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.product.infrastructure.persistence.mapper.ProductMapper;
import java.util.Optional;
import java.util.UUID;
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

  public Optional<Product> findByTitle(Title title) {
    return repository.findByTitle(title.value()).map(ProductMapper::toDomain);
  }

  public Optional<Product> findBySku(Sku sku) {
    return repository.findBySku(sku.value()).map(ProductMapper::toDomain);
  }

  public void save(Product product) {
    var category =
        categoryRepository.getReferenceById(UUID.fromString(product.categoryId().value()));
    repository.save(ProductMapper.toPersistence(product, category));
  }
}
