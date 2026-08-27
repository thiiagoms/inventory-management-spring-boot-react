package io.thiiagoms.ims.category.application.exception;

import io.thiiagoms.ims.shared.application.exception.ResourceNotChangedException;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public final class CategoryNotChangedException extends ResourceNotChangedException {
  private CategoryNotChangedException() {
    super("The category was not changed.", Id.FIELD);
  }

  public static CategoryNotChangedException create() {
    return new CategoryNotChangedException();
  }
}
