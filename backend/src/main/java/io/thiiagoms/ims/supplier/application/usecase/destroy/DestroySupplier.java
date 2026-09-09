package io.thiiagoms.ims.supplier.application.usecase.destroy;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.application.service.SupplierFinder;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;
import org.springframework.transaction.annotation.Transactional;

public class DestroySupplier {

  private final SupplierFinder finder;

  private final SupplierRepository repository;

  public DestroySupplier(SupplierFinder finder, SupplierRepository repository) {
    this.finder = finder;
    this.repository = repository;
  }

  @Transactional
  public void execute(Id id) {
    finder.byId(id);
    repository.destroy(id);
  }
}
