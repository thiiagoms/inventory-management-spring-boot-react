package io.thiiagoms.ims.supplier.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.util.regex.Pattern;

public record Cnpj(String value) {

  public static final String FIELD = "cnpj";

  private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("[0-9./\\-]+$");
  private static final Pattern NON_DIGITS = Pattern.compile("\\D");
  private static final Pattern REPEATED_DIGITS = Pattern.compile("(\\d)\\1{13}");

  public Cnpj {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);
    if (!ALLOWED_CHARACTERS.matcher(value).matches()) {
      throw fail("CNPJ must contain only numbers and formatting characters.");
    }

    value = NON_DIGITS.matcher(value).replaceAll("");
    if (value.length() != 14
        || REPEATED_DIGITS.matcher(value).matches()
        || !hasValidDigits(value)) {
      throw fail("CNPJ must be valid.");
    }
  }

  private static boolean hasValidDigits(String cnpj) {
    int firstDigit =
        calculateDigit(cnpj.substring(0, 12), new int[] {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
    int secondDigit =
        calculateDigit(
            cnpj.substring(0, 12) + firstDigit, new int[] {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
    return firstDigit == Character.digit(cnpj.charAt(12), 10)
        && secondDigit == Character.digit(cnpj.charAt(13), 10);
  }

  private static int calculateDigit(String source, int[] weights) {
    int sum = 0;

    for (int index = 0; index < weights.length; index++) {
      sum += Character.digit(source.charAt(index), 10) * weights[index];
    }

    int remainder = sum % 11;
    return remainder < 2 ? 0 : 11 - remainder;
  }

  private static InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
