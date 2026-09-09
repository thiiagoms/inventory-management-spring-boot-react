package io.thiiagoms.ims.fixtures.supplier.infrastructure.persistence.repository;

import io.thiiagoms.ims.shared.domain.pagination.Page;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.domain.Supplier;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class SupplierMemoryRepository implements SupplierRepository {
  private final Map<Id, Supplier> suppliers = new LinkedHashMap<>();

  @Override
  public Optional<Supplier> findById(Id id) {
    return Optional.ofNullable(suppliers.get(id));
  }

  @Override
  public Optional<Supplier> findByCnpj(Cnpj cnpj) {
    return suppliers.values().stream().filter(value -> value.cnpj().equals(cnpj)).findFirst();
  }

  @Override
  public Optional<Supplier> findBySocialName(SocialName socialName) {
    return suppliers.values().stream()
        .filter(value -> value.socialName().equals(socialName))
        .findFirst();
  }

  @Override
  public Page<Supplier> findAll(Pagination pagination) {
    var content = suppliers.values().stream().toList();
    return new Page<>(
        content, 0, pagination.size(), content.size(), content.isEmpty() ? 0 : 1, true, true);
  }

  @Override
  public void save(Supplier supplier) {
    suppliers.put(supplier.id(), supplier);
  }

  @Override
  public void destroy(Id id) {
    suppliers.remove(id);
  }
}
