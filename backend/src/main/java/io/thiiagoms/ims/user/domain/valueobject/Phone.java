package io.thiiagoms.ims.user.domain.valueobject;

import java.util.regex.Pattern;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Formatted;
import io.thiiagoms.ims.shared.domain.support.Guard;

public record Phone(String value) implements Formatted {

    public static final String FIELD = "phone";

    public static final int LANDLINE_LENGTH = 10;

    public static final int MOBILE_LENGTH = 11;

    private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("[0-9\\s()+.\\-]+");

    private static final Pattern NON_DIGITS = Pattern.compile("\\D");

    public Phone {
        Guard.againstNullOrEmptyOrBlank(FIELD, value);
        validateAllowedCharacters(value);
        value = normalize(value);
        validateLength(value);
    }

    @Override
    public String formatted() {
        return value.length() == MOBILE_LENGTH
                ? formatForMobile()
                : formatForLandline();
    }

    private static String normalize(String phone) {
        return NON_DIGITS.matcher(phone).replaceAll("");
    }

    private String formatForMobile() {
        return "(%s) %s-%s".formatted(
                value.substring(0, 2),
                value.substring(2, 7),
                value.substring(7));
    }

    private String formatForLandline() {
        return "(%s) %s-%s".formatted(
                value.substring(0, 2),
                value.substring(2, 6),
                value.substring(6));
    }

    private void validateAllowedCharacters(String phone) {
        if (!ALLOWED_CHARACTERS.matcher(phone).matches()) {
            fail("Phone must contain only numbers and formatting characters.");
        }
    }

    private void validateLength(String phone) {

        int phoneLength = phone.length();

        if (phoneLength != LANDLINE_LENGTH && phoneLength != MOBILE_LENGTH) {
            fail("Phone must contain 10 or 11 digits.");
        }
    }

    private void fail(String message) {
        throw InvalidDomainArgumentException.with(message, FIELD);
    }
}
