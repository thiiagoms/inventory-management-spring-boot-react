package io.thiiagoms.ims.user.application.usecase.update;

import io.thiiagoms.ims.user.application.dto.UserOutput;
import io.thiiagoms.ims.user.application.exception.UserNotChangedException;
import io.thiiagoms.ims.user.application.service.UserFinder;
import io.thiiagoms.ims.user.application.service.UserUniqueness;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.security.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class UpdateUser {

  private final UserFinder finder;

  private final PasswordEncoder encoder;

  private final UserRepository repository;

  private final UserUniqueness userUniqueness;

  public UpdateUser(
      UserFinder finder,
      PasswordEncoder encoder,
      UserRepository repository,
      UserUniqueness userUniqueness) {
    this.finder = finder;
    this.encoder = encoder;
    this.repository = repository;
    this.userUniqueness = userUniqueness;
  }

  @Transactional
  public UserOutput execute(UpdateUserData data) {
    var user = finder.byId(data.id());

    Boolean hasChanges = update(data, user);

    if (!hasChanges) {
      throw UserNotChangedException.create();
    }

    repository.save(user);

    return UserOutput.from(user);
  }

  private Boolean update(UpdateUserData data, User user) {
    boolean hasChanges = updateName(user, data);
    hasChanges |= updateEmail(user, data);
    hasChanges |= updatePhone(user, data);
    hasChanges |= updatePassword(user, data);

    return hasChanges;
  }

  private boolean updateName(User user, UpdateUserData data) {
    return data.name()
        .filter(name -> !name.equals(user.name()))
        .map(
            name -> {
              user.changeNameTo(name);
              return true;
            })
        .orElse(false);
  }

  private boolean updateEmail(User user, UpdateUserData data) {
    return data.email()
        .filter(email -> !email.equals(user.email()))
        .map(
            email -> {
              userUniqueness.ensureEmailIsAvailable(email);
              user.changeEmailTo(email);
              return true;
            })
        .orElse(false);
  }

  private boolean updatePassword(User user, UpdateUserData data) {
    return data.password()
        .filter(password -> !encoder.matches(password, user.password()))
        .map(
            password -> {
              user.changePasswordTo(encoder.encode(password));
              return true;
            })
        .orElse(false);
  }

  private boolean updatePhone(User user, UpdateUserData data) {
    return data.phone()
        .filter(phone -> !phone.equals(user.phone()))
        .map(
            phone -> {
              userUniqueness.ensurePhoneIsAvailable(phone);
              user.changePhoneTo(phone);
              return true;
            })
        .orElse(false);
  }
}
