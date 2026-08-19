package io.thiiagoms.ims.user.domain.valueobject;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Pattern;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;

public record Name(String value) {

    public static final String FIELD = "name";

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 150;

    private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s+");
    private static final Pattern ALLOWED_CHARS = Pattern.compile("^[\\p{L}\\p{M}'.\\-\\s]+$");

    public Name {
        Guard.againstNullOrEmptyOrBlank(FIELD, value);
        String name = normalize(value);
        validate(name);
        value = name;
    }
private String normalize(String rawName) {
        ensureUtf8Encodable(rawName);

        String compact = MULTIPLE_SPACES
                .matcher(rawName.trim().toLowerCase(Locale.ROOT))
                .replaceAll(" ");

        return toTitleCase(compact);
    }

    private void ensureUtf8Encodable(String name) {
        if (StandardCharsets.UTF_8.newEncoder().canEncode(name)) {
            return;
        }

        fail("Name could not be normalized due to invalid UTF-8 input.");
    }

    private String toTitleCase(String source) {
        StringBuilder result = new StringBuilder(source.length());
        boolean capitalizeNext = true;

        for (int index = 0; index < source.length(); ) {
            int codePoint = source.codePointAt(index);
            result.appendCodePoint(resolveCase(codePoint, capitalizeNext));
            capitalizeNext = nextCapitalizationState(codePoint, capitalizeNext);
            index += Character.charCount(codePoint);
        }

        return result.toString();
    }

    private int resolveCase(int codePoint, boolean capitalizeNext) {
        if (!capitalizeNext || !Character.isLetter(codePoint)) {
            return codePoint;
        }

        return Character.toTitleCase(codePoint);
    }

    private boolean nextCapitalizationState(int codePoint, boolean currentState) {
        if (isWordSeparator(codePoint)) {
            return true;
        }

        if (Character.isLetter(codePoint)) {
            return false;
        }

        return currentState;
    }

    private boolean isWordSeparator(int codePoint) {
        return Character.isWhitespace(codePoint)
                || codePoint == '\''
                || codePoint == '-'
                || codePoint == '.';
    }

    private void validate(String normalizedName) {
        ensureNameContainsOnlyAllowedChars(normalizedName);
        ensureNameLengthIsValid(normalizedName);
    }

    private void ensureNameContainsOnlyAllowedChars(String name) {
        if (ALLOWED_CHARS.matcher(name).matches()) {
            return;
        }

        fail("Name must contain only letters, spaces, apostrophes, dots, and hyphens.");
    }

    private void ensureNameLengthIsValid(String name) {
        int length = name.codePointCount(0, name.length());

        if (length >= MIN_LENGTH && length <= MAX_LENGTH) {
            return;
        }

        String message = "Name value must be between %d and %d characters long.".formatted(MIN_LENGTH, MAX_LENGTH);
        fail(message);
    }

    private void fail(String message) {
        throw InvalidDomainArgumentException.with(message, FIELD);
    }
}
