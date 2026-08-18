package io.thiiagoms.ims.shared.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;

public class IdTest {

    @ParameterizedTest(name = "[{index}] valid UUID: {0}")
    @ValueSource(strings = {
            "541d5c1e-2c6b-4a85-a35c-8ab3b4961b2b",
            "019e4279-8f37-7e21-b615-c2d8db59334b"
    })
    void shouldCreateIdWhenValueIsValidUuid(String value) {
        Id id = new Id(value);
        assertEquals(value, id.value());
    }

    @ParameterizedTest(name = "[{index}] blank value: [{0}]")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void shouldThrowExceptionWhenValueIsNullEmptyOrBlank(String value) {
        InvalidDomainArgumentException exception = assertThrows(
                InvalidDomainArgumentException.class,
                () -> new Id(value)
        );

        assertEquals(Id.FIELD, exception.getField());
        assertEquals("The field 'id' cannot be null, empty or blank.", exception.getMessage());
    }

    @ParameterizedTest(name = "[{index}] invalid UUID: {0}")
    @ValueSource(strings = {
            "invalid-id",
            "12345-6789",
            "not-even-close"
    })
    void shouldThrowExceptionWhenValueIsNotValidUuid(String value) {
        InvalidDomainArgumentException exception = assertThrows(
                InvalidDomainArgumentException.class,
                () -> new Id(value)
        );

        assertEquals(Id.FIELD, exception.getField());
        assertEquals("The 'id' must be a valid UUID.", exception.getMessage());
    }
}
