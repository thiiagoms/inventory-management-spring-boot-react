package io.thiiagoms.ims.user.domain.valueobject;

import io.thiiagoms.ims.shared.domain.support.Guard;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;

public record TokenIssuedAt(Timestamp value) {

  public static final String FIELD = "token_issued_at";

  public TokenIssuedAt {
    Guard.againstNull(FIELD, value);
  }
}
