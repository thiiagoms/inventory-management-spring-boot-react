package io.thiiagoms.ims.user.application.exception;

import io.thiiagoms.ims.shared.application.exception.ApplicationException;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public class UserNotChangedException extends ApplicationException {

  protected UserNotChangedException(String message, String field) {
    super(message, field);
  }

  public static UserNotChangedException create() {
    return new UserNotChangedException("No changes were detected for the user.", Id.FIELD);
  }
}
