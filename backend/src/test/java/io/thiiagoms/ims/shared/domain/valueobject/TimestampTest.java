package io.thiiagoms.ims.shared.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;

public class TimestampTest {

    @ParameterizedTest(name = "[{index}] valid timestamp: {0}")
    @ValueSource(strings = {
            "2026-08-04T10:15:30Z",
            "2024-02-29T23:59:59.123Z"
    })
    void itCreatesATimestampWhenTheDateIsValid(String value) {
        var timestamp = new Timestamp(value);
        assertEquals(value, timestamp.value());
    }

    @ParameterizedTest(name = "[{index}] blank timestamp: [{0}]")
    @NullSource
    @EmptySource
    @ValueSource(strings = { " ", "   " })
    void itRejectsANullEmptyOrBlankTimestamp(String value) {
        InvalidDomainArgumentException exception = assertThrows(
                InvalidDomainArgumentException.class,
                () -> new Timestamp(value));

        assertEquals(Timestamp.FIELD, exception.getField());
        assertEquals(
                "The field 'timestamp' cannot be null, empty or blank.",
                exception.getMessage());
    }

    @ParameterizedTest(name = "[{index}] invalid timestamp: {0}")
    @ValueSource(strings = {
            "not-a-date",
            "2026-02-30T10:00:00Z",
            "2026-08-04"
    })
    void itRejectsAnInvalidTimestamp(String value) {
        InvalidDomainArgumentException exception = assertThrows(
                InvalidDomainArgumentException.class,
                () -> new Timestamp(value));

        assertEquals(Timestamp.FIELD, exception.getField());
        assertEquals(
                "Timestamp must be a valid ISO-8601 date-time.",
                exception.getMessage());
    }
}
