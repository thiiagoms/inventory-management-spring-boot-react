package io.thiiagoms.ims.user.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.thiiagoms.ims.fixtures.user.domain.UserFake;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;
import io.thiiagoms.ims.user.domain.valueobject.TokenIssuedAt;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

public class JwtAuthenticationTokenIssuerTest {

  private static final String SECRET =
      "VGhpcy1pcy1hLXRlc3Qtb25seS1qd3Qtc2VjcmV0LXdpdGgtYXQtbGVhc3QtMzItYnl0ZXM=";

  @Test
  void itIssuesAValidSignedAuthenticationToken() {
    var ttlMinutes = 180L;
    var properties = new JwtProperties(SECRET, ttlMinutes);
    var issuer = new JwtAuthenticationTokenIssuer(properties);
    var user = UserFake.start().build();
    var issuedAtInstant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    var issuedAt = new TokenIssuedAt(new Timestamp(issuedAtInstant.toString()));

    var authenticationToken = issuer.issueFor(user, issuedAt);

    var signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    var claims =
        Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(authenticationToken.token().value())
            .getPayload();
    var expectedExpiration = issuedAtInstant.plus(ttlMinutes, ChronoUnit.MINUTES);

    assertEquals(user.id().value(), claims.getSubject());
    assertEquals(user.email().value(), claims.get("email", String.class));
    assertEquals(user.role().name(), claims.get("role", String.class));
    assertEquals(issuedAtInstant, claims.getIssuedAt().toInstant());
    assertEquals(expectedExpiration, claims.getExpiration().toInstant());
    assertEquals(expectedExpiration.toString(), authenticationToken.expiresAt().value().value());
  }
}
