package io.thiiagoms.ims.user.infrastructure.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    @NotBlank(message = "JWT secret must not be blank.") String secret,
    @Positive(message = "JWT TTL must be greater than zero minutes.") long ttlMinutes) {

  private static final int MINIMUM_SECRET_BYTES = 32;

  public JwtProperties {
    if (secret == null || secret.isBlank()) {
      fail("JWT secret must not be blank.");
    }

    byte[] decodedSecret;
    try {
      decodedSecret = Base64.getDecoder().decode(secret);
    } catch (IllegalArgumentException exception) {
      throw new IllegalArgumentException("JWT secret must be Base64 encoded.", exception);
    }

    if (decodedSecret.length < MINIMUM_SECRET_BYTES) {
      fail("JWT secret must contain at least 32 decoded bytes.");
    }

    if (ttlMinutes <= 0) {
      fail("JWT TTL must be greater than zero minutes.");
    }
  }

  private void fail(String message) {
    throw new IllegalArgumentException(message);
  }
}
