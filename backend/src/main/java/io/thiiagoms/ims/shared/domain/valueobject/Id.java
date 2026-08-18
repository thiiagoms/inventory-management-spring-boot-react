package io.thiiagoms.ims.shared.domain.valueobject;

import java.util.UUID;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;

public record Id(String value) {

    public static final String FIELD = "id";

    public Id {
        Guard.againstNullOrEmptyOrBlank(FIELD, value);
        validate(value);
    }

    private void validate(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw InvalidDomainArgumentException.with("The 'id' must be a valid UUID.", FIELD);
        }
    }
}
