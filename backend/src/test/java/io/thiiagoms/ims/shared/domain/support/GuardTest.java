package io.thiiagoms.ims.shared.domain.support;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;

public class GuardTest {

    private static final String FIELD = "Test-Field";
    private static final String EXPECTED_MESSAGE = "The field 'Test-Field' cannot be null, empty or blank.";

    @ParameterizedTest(name = "[{index}] againstNull() should not throw with valid value: {0}")
    @ValueSource(strings = {"any value", "another value"})
    void againstNullShouldNotThrowWithValidValue(String value) {
        assertDoesNotThrow(() -> Guard.againstNull(FIELD, value));
    }

    @Test
    void againstNullShouldThrowWhenValueIsNull() {
        InvalidDomainArgumentException exception = assertThrows(
                InvalidDomainArgumentException.class,
                () -> Guard.againstNull(FIELD, null)
        );

        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "[{index}] againstNullOrEmptyOrBlank() should not throw with valid value: {0}")
    @ValueSource(strings = {"valid", "John Doe", "test@email.com"})
    void againstNullOrEmptyOrBlankShouldNotThrowWithValidValue(String value) {
        assertDoesNotThrow(() -> Guard.againstNullOrEmptyOrBlank(FIELD, value));
    }

    @ParameterizedTest(
            name = "[{index}] againstNullOrEmptyOrBlank() should throw when value is null, empty or blank: [{0}]"
    )
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void againstNullOrEmptyOrBlankShouldThrowWhenValueIsNullEmptyOrBlank(String value) {
        InvalidDomainArgumentException exception = assertThrows(
                InvalidDomainArgumentException.class,
                () -> Guard.againstNullOrEmptyOrBlank(FIELD, value)
        );

        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
    }
}
