package io.thiiagoms.ims.supplier.application.usecase.retrieve;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.application.dto.SupplierOutput;
import io.thiiagoms.ims.supplier.application.service.SupplierFinder;

public class RetrieveSupplier {

  private final SupplierFinder finder;

  public RetrieveSupplier(SupplierFinder finder) {
    this.finder = finder;
  }

  public SupplierOutput execute(Id id) {
    return SupplierOutput.from(finder.byId(id));
  }
}
