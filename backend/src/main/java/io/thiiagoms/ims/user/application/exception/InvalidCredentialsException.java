package io.thiiagoms.ims.user.application.exception;

import io.thiiagoms.ims.shared.application.exception.ApplicationException;

public final class InvalidCredentialsException extends ApplicationException {

    public static final String FIELD = "credentials";

    private InvalidCredentialsException() {
        super("Invalid e-mail or password.", FIELD);
    }

    public static InvalidCredentialsException create() {
        return new InvalidCredentialsException();
    }
}
