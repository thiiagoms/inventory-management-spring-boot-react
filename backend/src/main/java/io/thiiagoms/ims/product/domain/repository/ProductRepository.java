package io.thiiagoms.ims.product.domain.repository;

import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import java.util.Optional;

public interface ProductRepository {

  Optional<Product> findByTitle(Title title);

  Optional<Product> findBySku(Sku sku);

  void save(Product product);
}
