package io.thiiagoms.ims.user.application.usecase.auth.authenticate;

import io.thiiagoms.ims.shared.domain.time.Clock;
import io.thiiagoms.ims.user.application.dto.AuthenticationOutput;
import io.thiiagoms.ims.user.application.exception.InvalidCredentialsException;
import io.thiiagoms.ims.user.application.exception.UserNotFoundException;
import io.thiiagoms.ims.user.application.service.UserFinder;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.security.PasswordEncoder;
import io.thiiagoms.ims.user.domain.security.TokenIssuer;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.TokenIssuedAt;
import org.springframework.transaction.annotation.Transactional;

public class Authenticate {

  private final UserFinder finder;

  private final TokenIssuer tokenIssuer;

  private final PasswordEncoder encoder;

  private final UserRepository repository;

  private final Clock clock;

  public Authenticate(
      UserFinder finder,
      TokenIssuer tokenIssuer,
      UserRepository repository,
      PasswordEncoder encoder,
      Clock clock) {
    this.clock = clock;
    this.finder = finder;
    this.encoder = encoder;
    this.repository = repository;
    this.tokenIssuer = tokenIssuer;
  }

  @Transactional
  public AuthenticationOutput execute(AuthenticateData data) {

    var user = retrievesUser(data.email());

    if (!ensureCredentialsMatch(user, data)) {
      throw InvalidCredentialsException.create();
    }

    var authenticatedAt = clock.now();
    var token = tokenIssuer.issueFor(user, new TokenIssuedAt(authenticatedAt));

    user.recordLoginAt(authenticatedAt);
    repository.save(user);

    return AuthenticationOutput.from(token);
  }

  private boolean ensureCredentialsMatch(User user, AuthenticateData data) {
    return encoder.matches(data.password(), user.password());
  }

  private User retrievesUser(Email email) {
    try {
      return finder.byEmail(email);
    } catch (UserNotFoundException exception) {
      throw InvalidCredentialsException.create();
    }
  }
}
