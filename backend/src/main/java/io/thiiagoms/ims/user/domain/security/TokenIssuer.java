package io.thiiagoms.ims.user.domain.security;

import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.valueobject.TokenIssuedAt;

public interface TokenIssuer {
  AuthenticationToken issueFor(User user, TokenIssuedAt issuedAt);
}
