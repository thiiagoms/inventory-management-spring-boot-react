package io.thiiagoms.ims.shared.domain.exception;

public final class InvalidDomainArgumentException extends DomainException {

    protected InvalidDomainArgumentException(String message, String field) {
        super(message, field);
    }

    public static InvalidDomainArgumentException with(String message, String field) {
        return new InvalidDomainArgumentException(message, field);
    }
}
