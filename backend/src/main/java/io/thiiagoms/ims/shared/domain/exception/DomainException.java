package io.thiiagoms.ims.shared.domain.exception;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {

    protected String field;

    protected DomainException(String message, String field) {
        super(message);
        this.field = field;
    }
}
