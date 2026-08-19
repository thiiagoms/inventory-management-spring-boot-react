package io.thiiagoms.ims.user.application.exception;

import io.thiiagoms.ims.shared.application.exception.ResourceAlreadyExistsException;
import io.thiiagoms.ims.user.domain.valueobject.Email;

public final class EmailAlreadyExistsException extends ResourceAlreadyExistsException {
    protected EmailAlreadyExistsException(String message, String field) {
        super(message, field);
    }

    public static EmailAlreadyExistsException create() {
        return new EmailAlreadyExistsException("An User with this e-mail already exists.", Email.FIELD);
    }
}
