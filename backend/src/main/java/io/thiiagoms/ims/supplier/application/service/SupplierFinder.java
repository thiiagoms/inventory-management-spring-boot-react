package io.thiiagoms.ims.supplier.application.service;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.application.exception.SupplierNotFoundException;
import io.thiiagoms.ims.supplier.domain.Supplier;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;

public class SupplierFinder {
  private final SupplierRepository repository;

  public SupplierFinder(SupplierRepository repository) {
    this.repository = repository;
  }

  public Supplier byId(Id id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                SupplierNotFoundException.with(
                    "Supplier not found with the provided id.", Id.FIELD));
  }
}
