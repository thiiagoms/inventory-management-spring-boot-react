package io.thiiagoms.ims.user.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;

public class PasswordPlainTest {

    @ParameterizedTest(name = "[{index}] should create plain password with valid value")
    @ValueSource(strings = {
            "Strong@123",
            "XyZ!5678",
            "My_Pass9$"
    })
    void shouldCreatePlainPasswordWithValidValue(String value) {
        PasswordPlain password = new PasswordPlain(value);
        assertEquals(value, password.value());
    }

    @ParameterizedTest(name = "[{index}] should mask plain password in toString")
    @ValueSource(strings = { "Strong@123" })
    void shouldMaskPlainPasswordInToString(String value) {
        PasswordPlain password = new PasswordPlain(value);
        assertEquals("{*******************}", password.toString());
    }

    @ParameterizedTest(name = "[{index}] should throw when plain password is null/empty/blank: [{0}]")
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   " })
    void shouldThrowWhenPlainPasswordIsNullEmptyOrBlank(String value) {
        InvalidDomainArgumentException exception = assertThrows(
                InvalidDomainArgumentException.class,
                () -> new PasswordPlain(value));

        assertEquals(PasswordPlain.FIELD, exception.getField());
        assertEquals("The field 'password' cannot be null, empty or blank.", exception.getMessage());
    }

    @ParameterizedTest(name = "[{index}] should throw when plain password does not meet policy: {0}")
    @ValueSource(strings = {
            "short1!",
            "alllowercase1!",
            "ALLUPPERCASE1!",
            "NoDigits!!",
            "NoSpecial123",
            "Has Space1!"
    })
    void shouldThrowWhenPlainPasswordDoesNotMeetPolicy(String value) {
        InvalidDomainArgumentException exception = assertThrows(
                InvalidDomainArgumentException.class,
                () -> new PasswordPlain(value));

        var expectedErrorMessage = "Password must be at least 8 characters and include uppercase, lowercase," +
                " digit, and special character.";

        assertEquals(PasswordPlain.FIELD, exception.getField());
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
