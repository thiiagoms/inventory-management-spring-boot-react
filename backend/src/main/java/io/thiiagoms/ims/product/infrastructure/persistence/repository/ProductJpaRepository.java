package io.thiiagoms.ims.product.infrastructure.persistence.repository;

import io.thiiagoms.ims.product.infrastructure.persistence.model.ProductJpa;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductJpa, UUID> {
  Optional<ProductJpa> findByTitle(String title);

  Optional<ProductJpa> findBySku(String sku);
}
