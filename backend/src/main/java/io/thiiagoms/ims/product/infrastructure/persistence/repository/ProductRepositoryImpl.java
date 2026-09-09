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
import io.thiiagoms.ims.supplier.infrastructure.persistence.repository.SupplierJpaRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
  private final ProductJpaRepository repository;
  private final CategoryJpaRepository categoryRepository;
  private final SupplierJpaRepository supplierRepository;

  public ProductRepositoryImpl(
      ProductJpaRepository repository,
      CategoryJpaRepository categoryRepository,
      SupplierJpaRepository supplierRepository) {
    this.repository = repository;
    this.categoryRepository = categoryRepository;
    this.supplierRepository = supplierRepository;
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
    var categories =
        product.categoryIds().values().stream()
            .map(
                categoryId ->
                    categoryRepository.getReferenceById(UUID.fromString(categoryId.value())))
            .collect(java.util.stream.Collectors.toSet());
    var supplier =
        supplierRepository.getReferenceById(UUID.fromString(product.supplierId().value()));
    repository.saveAndFlush(ProductMapper.toPersistence(product, Set.copyOf(categories), supplier));
  }

  public void destroy(Id id) {
    repository.deleteById(UUID.fromString(id.value()));
    repository.flush();
  }
}
