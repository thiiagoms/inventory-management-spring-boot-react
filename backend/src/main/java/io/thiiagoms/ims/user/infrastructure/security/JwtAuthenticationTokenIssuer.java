package io.thiiagoms.ims.user.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.security.AuthenticationToken;
import io.thiiagoms.ims.user.domain.security.TokenIssuer;
import io.thiiagoms.ims.user.domain.valueobject.Token;
import io.thiiagoms.ims.user.domain.valueobject.TokenExpiresAt;
import io.thiiagoms.ims.user.domain.valueobject.TokenIssuedAt;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public final class JwtAuthenticationTokenIssuer implements TokenIssuer {

  private final SecretKey signingKey;

  private final long ttlMinutes;

  public JwtAuthenticationTokenIssuer(JwtProperties properties) {
    signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secret()));
    ttlMinutes = properties.ttlMinutes();
  }

  @Override
  public AuthenticationToken issueFor(User user, TokenIssuedAt issuedAt) {
    var issuedAtInstant = Instant.parse(issuedAt.value().value());
    var expiresAtInstant = issuedAtInstant.plus(ttlMinutes, ChronoUnit.MINUTES);

    var token =
        Jwts.builder()
            .subject(user.id().value())
            .claim("email", user.email().value())
            .claim("role", user.role().name())
            .issuedAt(Date.from(issuedAtInstant))
            .expiration(Date.from(expiresAtInstant))
            .signWith(signingKey, Jwts.SIG.HS256)
            .compact();

    return new AuthenticationToken(
        new Token(token), new TokenExpiresAt(new Timestamp(expiresAtInstant.toString())));
  }
}
