package io.thiiagoms.ims.user.domain.security;

import io.thiiagoms.ims.shared.domain.support.Guard;
import io.thiiagoms.ims.user.domain.valueobject.Token;
import io.thiiagoms.ims.user.domain.valueobject.TokenExpiresAt;

public record AuthenticationToken(Token token, TokenExpiresAt expiresAt) {
  public AuthenticationToken {
    Guard.againstNull(Token.FIELD, token);
    Guard.againstNull(TokenExpiresAt.FIELD, expiresAt);
  }
}
