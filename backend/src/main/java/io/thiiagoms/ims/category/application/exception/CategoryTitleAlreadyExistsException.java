package io.thiiagoms.ims.category.application.exception;

import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.shared.application.exception.ResourceAlreadyExistsException;

public final class CategoryTitleAlreadyExistsException extends ResourceAlreadyExistsException {
  private CategoryTitleAlreadyExistsException(String message, String field) {
    super(message, field);
  }

  public static CategoryTitleAlreadyExistsException create() {
    return new CategoryTitleAlreadyExistsException(
        "A category with this title already exists.", Title.FIELD);
  }
}
