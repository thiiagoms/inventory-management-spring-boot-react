package io.thiiagoms.ims.fixtures.product.infrastructure.persistence.repository;

import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
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

  public void save(Product product) {
    products.put(product.id().value(), product);
  }
}
