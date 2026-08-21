package io.thiiagoms.ims.user.domain.security;

import io.thiiagoms.ims.shared.domain.support.Guard;
import io.thiiagoms.ims.user.domain.valueobject.Token;
import io.thiiagoms.ims.user.domain.valueobject.TokenIssuedAt;

public record AuthenticationToken(Token token, TokenIssuedAt expiresAt) {
  public AuthenticationToken {
    Guard.againstNull(Token.FIELD, token);
    Guard.againstNull(TokenIssuedAt.FIELD, expiresAt);
  }
}
