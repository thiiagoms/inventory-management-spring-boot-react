package io.thiiagoms.ims.supplier.infrastructure.persistence.repository;

import io.thiiagoms.ims.supplier.infrastructure.persistence.model.SupplierJpa;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierJpaRepository extends JpaRepository<SupplierJpa, UUID> {
  Optional<SupplierJpa> findByCnpj(String cnpj);

  Optional<SupplierJpa> findBySocialName(String socialName);
}
