package io.thiiagoms.ims.shared.domain.exception;

public class AuthorizationFailedException extends DomainException {

  protected AuthorizationFailedException(String message, String field) {
    super(message, field);
  }

  public static AuthorizationFailedException with(String message, String field) {
    return new AuthorizationFailedException(message, field);
  }
}
