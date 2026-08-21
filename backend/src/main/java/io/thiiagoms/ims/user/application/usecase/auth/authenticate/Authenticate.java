package io.thiiagoms.ims.user.application.usecase.auth.authenticate;

import io.thiiagoms.ims.user.application.exception.InvalidCredentialsException;
import io.thiiagoms.ims.user.application.exception.UserNotFoundException;
import io.thiiagoms.ims.user.application.service.UserFinder;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.security.AuthenticationToken;
import io.thiiagoms.ims.user.domain.security.PasswordEncoder;
import io.thiiagoms.ims.user.domain.valueobject.Email;

public class Authenticate {

  private final UserFinder finder;

  private final PasswordEncoder encoder;

  private final AuthenticationToken tokenIssuer;

  public Authenticate(UserFinder finder, PasswordEncoder encoder, AuthenticationToken tokenIssuer) {
    this.finder = finder;
    this.encoder = encoder;
    this.tokenIssuer = tokenIssuer;
  }

  public void execute(AuthenticateData data) {

    var user = retrievesUser(data.email());

    if (! ensureCredentialsMatch(user, data)) {
      throw InvalidCredentialsException.create();
    }

  }

  private Boolean ensureCredentialsMatch(User user, AuthenticateData data) {
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
