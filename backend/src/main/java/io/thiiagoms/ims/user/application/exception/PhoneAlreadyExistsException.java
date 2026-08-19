package io.thiiagoms.ims.user.application.exception;

import io.thiiagoms.ims.shared.application.exception.ResourceAlreadyExistsException;
import io.thiiagoms.ims.user.domain.valueobject.Phone;

public final class PhoneAlreadyExistsException extends ResourceAlreadyExistsException {

    private PhoneAlreadyExistsException(String message, String field) {
        super(message, field);
    }

    public static PhoneAlreadyExistsException create() {
        return new PhoneAlreadyExistsException("An User with this phone already exists.", Phone.FIELD);
    }
}
