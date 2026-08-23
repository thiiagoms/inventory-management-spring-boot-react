package io.thiiagoms.ims.user.domain.valueobject;

import io.thiiagoms.ims.shared.domain.support.Guard;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;

public record TokenExpiresAt(Timestamp value) {

  public static final String FIELD = "token_expires_at";

  public TokenExpiresAt {
    Guard.againstNull(FIELD, value);
  }
}
