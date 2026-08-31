package io.thiiagoms.ims.product.domain.repository;

import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.pagination.Page;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Optional;

public interface ProductRepository {

  Optional<Product> findById(Id id);

  Optional<Product> findByTitle(Title title);

  Optional<Product> findBySku(Sku sku);

  Page<Product> findAll(Pagination pagination);

  void save(Product product);
}
