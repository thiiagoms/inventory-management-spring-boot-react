package io.thiiagoms.ims.user.application.exception;

import io.thiiagoms.ims.shared.application.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

  private UserNotFoundException(String message, String field) {
    super(message, field);
  }

  public static UserNotFoundException with(String message, String searchableResource) {
    return new UserNotFoundException(message, searchableResource);
  }
}
