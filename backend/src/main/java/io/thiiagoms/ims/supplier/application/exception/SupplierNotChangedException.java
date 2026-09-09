package io.thiiagoms.ims.supplier.application.exception;

import io.thiiagoms.ims.shared.application.exception.ResourceNotChangedException;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public final class SupplierNotChangedException extends ResourceNotChangedException {
  private SupplierNotChangedException() {
    super("The supplier was not changed.", Id.FIELD);
  }

  public static SupplierNotChangedException create() {
    return new SupplierNotChangedException();
  }
}
