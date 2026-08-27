package io.thiiagoms.ims.category.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Pattern;

public record Title(String value) {

  public static final String FIELD = "title";

  private static final int MIN_LENGTH = 3;
  private static final int MAX_LENGTH = 250;

  private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s+");
  private static final Pattern ALLOWED_CHARS = Pattern.compile("^[\\p{L}\\p{M}'.\\-\\s]+$");

  public Title {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);
    String title = normalize(value);

    validate(title);
    value = title;
  }

  private String normalize(String title) {
    ensureUtf8Encodable(title);

    String compact = MULTIPLE_SPACES.matcher(title.trim().toLowerCase(Locale.ROOT)).replaceAll(" ");

    return toTitleCase(compact);
  }

  private void ensureUtf8Encodable(String title) {
    if (StandardCharsets.UTF_8.newEncoder().canEncode(title)) {
      return;
    }

    throw fail("Title could not be normalized due to invalid UTF-8 input.");
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

  private void validate(String normalizedTitle) {
    ensureTitleContainsOnlyAllowedChars(normalizedTitle);
    ensureTitleLengthIsValid(normalizedTitle);
  }

  private void ensureTitleContainsOnlyAllowedChars(String title) {
    if (ALLOWED_CHARS.matcher(title).matches()) {
      return;
    }

    throw fail("Title must contain only letters, spaces, apostrophes, dots, and hyphens.");
  }

  private void ensureTitleLengthIsValid(String title) {
    int length = title.codePointCount(0, title.length());

    if (length >= MIN_LENGTH && length <= MAX_LENGTH) {
      return;
    }

    throw fail(
        "Title value must be between %d and %d characters long.".formatted(MIN_LENGTH, MAX_LENGTH));
  }

  private InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
