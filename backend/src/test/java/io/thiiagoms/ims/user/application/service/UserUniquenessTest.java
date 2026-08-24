package io.thiiagoms.ims.user.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.fixtures.user.domain.UserFake;
import io.thiiagoms.ims.fixtures.user.infrastructure.persistence.repository.UserMemoryRepository;
import io.thiiagoms.ims.user.application.exception.EmailAlreadyExistsException;
import io.thiiagoms.ims.user.application.exception.PhoneAlreadyExistsException;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserUniquenessTest {

  private Email email;

  private Phone phone;

  private UserRepository repository;

  private UserUniqueness service;

  @BeforeEach
  void setUp() {
    email = new Email("ihatejavascript@gmail.com");
    phone = new Phone("21988887777");
    repository = new UserMemoryRepository();
    service = new UserUniqueness(repository);
  }

  @Test
  void itAllowsAnAvailableEmail() {
    assertDoesNotThrow(() -> service.ensureEmailIsAvailable(email));
  }

  @Test
  void itAllowsAnAvailablePhone() {
    assertDoesNotThrow(() -> service.ensurePhoneIsAvailable(phone));
  }

  @Test
  void itShouldThrowExceptionWhenUserEmailAlreadyExists() {
    repository.save(UserFake.start().withEmail(email).build());
    EmailAlreadyExistsException exception =
        assertThrows(
            EmailAlreadyExistsException.class, () -> service.ensureEmailIsAvailable(email));

    assertEquals(Email.FIELD, exception.getField());
    assertEquals("A user with this e-mail already exists.", exception.getMessage());
  }

  @Test
  void itRejectsAPhoneAlreadyOwnedByAnUser() {
    repository.save(UserFake.start().withPhone(phone).build());

    PhoneAlreadyExistsException exception =
        assertThrows(
            PhoneAlreadyExistsException.class, () -> service.ensurePhoneIsAvailable(phone));

    assertEquals(Phone.FIELD, exception.getField());
    assertEquals("A user with this phone already exists.", exception.getMessage());
  }
}
