package io.thiiagoms.ims.supplier.application.usecase.retrieve;

import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.supplier.application.dto.SupplierPageOutput;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;

public class RetrieveSuppliers {
  private final SupplierRepository repository;

  public RetrieveSuppliers(SupplierRepository repository) {
    this.repository = repository;
  }

  public SupplierPageOutput execute(Pagination pagination) {
    return SupplierPageOutput.from(repository.findAll(pagination));
  }
}
