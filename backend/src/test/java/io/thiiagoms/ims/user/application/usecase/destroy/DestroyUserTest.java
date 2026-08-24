package io.thiiagoms.ims.user.application.usecase.destroy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.fixtures.user.domain.UserFake;
import io.thiiagoms.ims.fixtures.user.infrastructure.persistence.repository.UserMemoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.application.exception.UserNotFoundException;
import io.thiiagoms.ims.user.application.service.UserFinder;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DestroyUserTest {

  private UserRepository repository;

  private DestroyUser useCase;

  @BeforeEach
  void setUp() {
    repository = new UserMemoryRepository();
    useCase = new DestroyUser(new UserFinder(repository), repository);
  }

  @Test
  void itShouldDestroyUserWhenExistsInDatabase() {
    var id = new Id("edd3e65a-781e-4b75-a56c-302c1b72d381");
    repository.save(UserFake.start().withId(id).build());
    useCase.execute(id);

    assertTrue(repository.findById(id).isEmpty());
  }

  @Test
  void itShouldThrowExceptionWhenUserDoesNotExist() {
    var id = new Id("edd3e65a-781e-4b75-a56c-302c1b72d381");

    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> useCase.execute(id));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("User not found with the provided id.", exception.getMessage());
  }
}
