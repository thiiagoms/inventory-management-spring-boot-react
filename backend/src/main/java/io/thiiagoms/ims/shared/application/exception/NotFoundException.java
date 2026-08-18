package io.thiiagoms.ims.shared.application.exception;

public abstract class NotFoundException extends ApplicationException {
    protected NotFoundException(String message, String field) {
        super(message, field);
    }
}
