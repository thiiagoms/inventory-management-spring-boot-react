package io.thiiagoms.ims.category.infrastructure.persistence.repository;

import io.thiiagoms.ims.category.infrastructure.persistence.model.CategoryJpa;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpa, UUID> {
  Optional<CategoryJpa> findByTitle(String title);
}
