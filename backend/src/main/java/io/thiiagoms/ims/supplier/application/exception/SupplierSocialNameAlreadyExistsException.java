package io.thiiagoms.ims.supplier.application.exception;

import io.thiiagoms.ims.shared.application.exception.ResourceAlreadyExistsException;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;

public final class SupplierSocialNameAlreadyExistsException extends ResourceAlreadyExistsException {

  private SupplierSocialNameAlreadyExistsException() {
    super("A supplier with this Social Name already exists.", SocialName.FIELD);
  }

  public static SupplierSocialNameAlreadyExistsException create() {
    return new SupplierSocialNameAlreadyExistsException();
  }
}
