package io.thiiagoms.ims.user.application.dto;

import io.thiiagoms.ims.user.domain.security.AuthenticationToken;

public record AuthenticationOutput(String token, String expiresAt) {

  public static AuthenticationOutput from(AuthenticationToken authenticationToken) {
    return new AuthenticationOutput(
        authenticationToken.token().value(), authenticationToken.expiresAt().value().value());
  }
}
