package io.thiiagoms.ims.shared.application.exception;

public abstract class ResourceNotChangedException extends ApplicationException {
  protected ResourceNotChangedException(String message, String field) {
    super(message, field);
  }
}
