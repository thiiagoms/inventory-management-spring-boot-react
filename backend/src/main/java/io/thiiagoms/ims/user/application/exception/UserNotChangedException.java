package io.thiiagoms.ims.user.application.exception;

import io.thiiagoms.ims.shared.application.exception.ResourceNotChangedException;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public class UserNotChangedException extends ResourceNotChangedException {

  protected UserNotChangedException(String message) {
    super(message, Id.FIELD);
  }

  public static UserNotChangedException create() {
    return new UserNotChangedException("No changes were detected for the user.");
  }
}
