package io.thiiagoms.ims.user.application.exception;

import io.thiiagoms.ims.shared.domain.exception.AuthorizationFailedException;

public final class InvalidCredentialsException extends AuthorizationFailedException {

  public static final String FIELD = "credentials";

  private InvalidCredentialsException() {
    super("Invalid e-mail or password.", FIELD);
  }

  public static InvalidCredentialsException create() {
    return new InvalidCredentialsException();
  }
}
