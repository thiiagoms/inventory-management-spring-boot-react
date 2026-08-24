package io.thiiagoms.ims.user.application.service;

import io.thiiagoms.ims.user.application.exception.EmailAlreadyExistsException;
import io.thiiagoms.ims.user.application.exception.PhoneAlreadyExistsException;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Phone;

public class UserUniqueness {

  private final UserRepository repository;

  public UserUniqueness(UserRepository repository) {
    this.repository = repository;
  }

  public void ensureEmailIsAvailable(Email email) {
    if (repository.findByEmail(email).isPresent()) {
      throw EmailAlreadyExistsException.create();
    }
  }

  public void ensurePhoneIsAvailable(Phone phone) {
    if (repository.findByPhone(phone).isPresent()) {
      throw PhoneAlreadyExistsException.create();
    }
  }
}
