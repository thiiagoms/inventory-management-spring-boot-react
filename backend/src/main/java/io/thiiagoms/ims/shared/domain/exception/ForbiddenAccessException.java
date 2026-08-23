package io.thiiagoms.ims.shared.domain.exception;

public class ForbiddenAccessException extends DomainException {

  protected ForbiddenAccessException(String message, String field) {
    super(message, field);
  }

  public static ForbiddenAccessException with(String message, String field) {
    return new ForbiddenAccessException(message, field);
  }
}
