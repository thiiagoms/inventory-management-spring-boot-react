package io.thiiagoms.ims.user.infrastructure.config;

import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import io.thiiagoms.ims.shared.domain.time.Clock;
import io.thiiagoms.ims.user.application.service.UserFinder;
import io.thiiagoms.ims.user.application.service.UserUniqueness;
import io.thiiagoms.ims.user.application.usecase.auth.authenticate.Authenticate;
import io.thiiagoms.ims.user.application.usecase.register.RegisterUser;
import io.thiiagoms.ims.user.application.usecase.update.UpdateUser;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.security.PasswordEncoder;
import io.thiiagoms.ims.user.domain.security.TokenIssuer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

  @Bean
  UserUniqueness userUniqueness(UserRepository repository) {
    return new UserUniqueness(repository);
  }

  @Bean
  UserFinder userFinder(UserRepository repository) {
    return new UserFinder(repository);
  }

  @Bean
  RegisterUser registerUser(
      UserRepository repository,
      PasswordEncoder encoder,
      UserUniqueness userUniqueness,
      IdentityGenerator identityGenerator) {
    return new RegisterUser(repository, encoder, userUniqueness, identityGenerator);
  }

  @Bean
  Authenticate authenticate(
      UserFinder userFinder,
      TokenIssuer tokenIssuer,
      UserRepository repository,
      PasswordEncoder encoder,
      Clock clock) {
    return new Authenticate(userFinder, tokenIssuer, repository, encoder, clock);
  }

  @Bean
  UpdateUser updateUser(
      UserFinder userFinder,
      PasswordEncoder encoder,
      UserRepository repository,
      UserUniqueness userUniqueness) {
    return new UpdateUser(userFinder, encoder, repository, userUniqueness);
  }
}
