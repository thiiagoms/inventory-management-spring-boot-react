package io.thiiagoms.ims.supplier.application.exception;

import io.thiiagoms.ims.shared.application.exception.NotFoundException;

public final class SupplierNotFoundException extends NotFoundException {
  private SupplierNotFoundException(String message, String field) {
    super(message, field);
  }

  public static SupplierNotFoundException with(String message, String field) {
    return new SupplierNotFoundException(message, field);
  }
}
