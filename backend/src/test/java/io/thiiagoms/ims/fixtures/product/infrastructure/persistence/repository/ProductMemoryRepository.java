package io.thiiagoms.ims.fixtures.product.infrastructure.persistence.repository;

import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.pagination.Page;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ProductMemoryRepository implements ProductRepository {
  private final Map<String, Product> products = new LinkedHashMap<>();

  public Optional<Product> findById(Id id) {
    return Optional.ofNullable(products.get(id.value()));
  }

  public Optional<Product> findByTitle(Title title) {
    return products.values().stream().filter(product -> product.title().equals(title)).findFirst();
  }

  public Optional<Product> findBySku(Sku sku) {
    return products.values().stream().filter(product -> product.sku().equals(sku)).findFirst();
  }

  public Page<Product> findAll(Pagination pagination) {
    var allProducts =
        products.values().stream()
            .sorted(Comparator.comparing(product -> product.title().value()))
            .toList();
    long offset = (long) pagination.page() * pagination.size();
    int fromIndex = (int) Math.min(offset, allProducts.size());
    int toIndex = Math.min(fromIndex + pagination.size(), allProducts.size());
    int totalPages =
        allProducts.isEmpty()
            ? 0
            : (int) Math.ceil((double) allProducts.size() / pagination.size());

    return new Page<>(
        allProducts.subList(fromIndex, toIndex),
        pagination.page(),
        pagination.size(),
        allProducts.size(),
        totalPages,
        pagination.page() == 0,
        pagination.page() >= totalPages - 1);
  }

  public void save(Product product) {
    products.put(product.id().value(), product);
  }
}
