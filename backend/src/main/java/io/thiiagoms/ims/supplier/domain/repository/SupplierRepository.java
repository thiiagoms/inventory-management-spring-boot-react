package io.thiiagoms.ims.supplier.domain.repository;

import io.thiiagoms.ims.shared.domain.pagination.Page;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.domain.Supplier;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;
import java.util.Optional;

public interface SupplierRepository {

  Optional<Supplier> findById(Id id);

  Optional<Supplier> findByCnpj(Cnpj cnpj);

  Optional<Supplier> findBySocialName(SocialName name);

  Page<Supplier> findAll(Pagination pagination);

  void save(Supplier supplier);

  void destroy(Id id);
}
