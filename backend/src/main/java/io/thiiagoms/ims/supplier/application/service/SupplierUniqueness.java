package io.thiiagoms.ims.supplier.application.service;

import io.thiiagoms.ims.supplier.application.exception.SupplierCnpjAlreadyExistsException;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;

public class SupplierUniqueness {
  private final SupplierRepository repository;

  public SupplierUniqueness(SupplierRepository repository) {
    this.repository = repository;
  }

  public void ensureCnpjIsAvailable(Cnpj cnpj) {
    if (repository.findByCnpj(cnpj).isPresent()) {
      throw SupplierCnpjAlreadyExistsException.create();
    }
  }
}
