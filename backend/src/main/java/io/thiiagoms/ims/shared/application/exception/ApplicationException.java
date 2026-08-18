package io.thiiagoms.ims.shared.application.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    protected String field;

    protected ApplicationException(String message, String field) {
        super(message);
        this.field = field;
    }
}

