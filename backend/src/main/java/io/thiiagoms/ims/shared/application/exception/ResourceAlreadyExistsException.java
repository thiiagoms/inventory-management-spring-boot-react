package io.thiiagoms.ims.shared.application.exception;

public abstract class ResourceAlreadyExistsException extends ApplicationException {
  protected ResourceAlreadyExistsException(String message, String field) {
    super(message, field);
  }
}
