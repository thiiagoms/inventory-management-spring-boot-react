package io.thiiagoms.ims.user.domain.valueobject;

import io.thiiagoms.ims.shared.domain.support.Guard;

public record Token(String value) {

  public static final String FIELD = "token";

  public Token {
    Guard.againstNullOrEmptyOrBlank(FIELD, value);
  }

  @Override
  public String toString() {
    return "{*******************}";
  }
}
