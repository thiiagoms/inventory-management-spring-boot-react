package io.thiiagoms.ims.supplier.infrastructure.persistence.repository;

import io.thiiagoms.ims.shared.domain.pagination.Page;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.domain.Supplier;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;
import io.thiiagoms.ims.supplier.infrastructure.persistence.mapper.SupplierMapper;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class SupplierRepositoryImpl implements SupplierRepository {
  private final SupplierJpaRepository repository;

  public SupplierRepositoryImpl(SupplierJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<Supplier> findById(Id id) {
    return repository.findById(UUID.fromString(id.value())).map(SupplierMapper::toDomain);
  }

  @Override
  public Optional<Supplier> findByCnpj(Cnpj cnpj) {
    return repository.findByCnpj(cnpj.value()).map(SupplierMapper::toDomain);
  }

  @Override
  public Optional<Supplier> findBySocialName(SocialName socialName) {
    return repository.findBySocialName(socialName.value()).map(SupplierMapper::toDomain);
  }

  @Override
  public Page<Supplier> findAll(Pagination pagination) {
    var pageable =
        PageRequest.of(pagination.page(), pagination.size(), Sort.by("socialName").ascending());
    var suppliers = repository.findAll(pageable);
    return new Page<>(
        suppliers.getContent().stream().map(SupplierMapper::toDomain).toList(),
        suppliers.getNumber(),
        suppliers.getSize(),
        suppliers.getTotalElements(),
        suppliers.getTotalPages(),
        suppliers.isFirst(),
        suppliers.isLast());
  }

  @Override
  public void save(Supplier supplier) {
    repository.save(SupplierMapper.toPersistence(supplier));
  }

  @Override
  public void destroy(Id id) {
    repository.deleteById(UUID.fromString(id.value()));
  }
}
