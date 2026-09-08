package io.thiiagoms.ims.supplier.application.exception;

import io.thiiagoms.ims.shared.application.exception.ResourceAlreadyExistsException;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;

public final class SupplierCnpjAlreadyExistsException extends ResourceAlreadyExistsException {
  private SupplierCnpjAlreadyExistsException() {
    super("A supplier with this CNPJ already exists.", Cnpj.FIELD);
  }

  public static SupplierCnpjAlreadyExistsException create() {
    return new SupplierCnpjAlreadyExistsException();
  }
}
