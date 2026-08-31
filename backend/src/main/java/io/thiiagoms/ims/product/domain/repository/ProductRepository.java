package io.thiiagoms.ims.product.domain.repository;

import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Optional;

public interface ProductRepository {

  Optional<Product> findById(Id id);

  Optional<Product> findByTitle(Title title);

  Optional<Product> findBySku(Sku sku);

  void save(Product product);
}
