package io.thiiagoms.ims.category.application.exception;

import io.thiiagoms.ims.shared.application.exception.NotFoundException;

public final class CategoryNotFoundException extends NotFoundException {
  private CategoryNotFoundException(String message, String field) {
    super(message, field);
  }

  public static CategoryNotFoundException with(String message, String field) {
    return new CategoryNotFoundException(message, field);
  }
}
