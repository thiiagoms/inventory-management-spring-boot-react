package io.thiiagoms.ims.shared.domain.valueobject;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;

public record Timestamp(String value) {

    public static final String FIELD = "timestamp";

    public Timestamp {
        Guard.againstNullOrEmptyOrBlank(FIELD, value);
        validate(value);
    }

    private void validate(String value) {
        try {
            DateTimeFormatter.ISO_INSTANT.parse(value);
        } catch (DateTimeParseException exception) {
            throw InvalidDomainArgumentException.with(
                    "Timestamp must be a valid ISO-8601 date-time.",
                    FIELD);
        }
    }
}
