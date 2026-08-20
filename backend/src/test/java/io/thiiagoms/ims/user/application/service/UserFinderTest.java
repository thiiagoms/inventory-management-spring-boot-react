package io.thiiagoms.ims.user.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.thiiagoms.ims.fixtures.user.domain.UserFake;
import io.thiiagoms.ims.fixtures.user.infrastructure.persistence.repository.UserMemoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.application.exception.UserNotFoundException;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.valueobject.Email;

public class UserFinderTest {

    private Id id;

    private Email email;

    private UserRepository repository;

    private UserFinder service;

    @BeforeEach
    void setUp() {
        this.id = new Id("fa8c01b6-546d-4954-9482-597f3c09a1be");
        this.email = new Email("peter.parker@hotmail.com");
        this.repository = new UserMemoryRepository();
        this.service = new UserFinder(repository);
    }

    @Test
    void itReturnsAnUserWhenTheProvidedIdExists() {
        var expectedUser = UserFake
            .start()
            .withId(id)
            .build();

        repository.save(expectedUser);

        var user = service.byId(id);
        assertEquals(expectedUser, user);
    }

    @Test
    void itReturnsAnUserWhenTheProvidedEmailExists() {
        var expectedUser = UserFake
            .start()
            .withEmail(email)
            .build();

        repository.save(expectedUser);

        var user = service.byEmail(email);
        assertEquals(expectedUser, user);
    }

    @Test
    void itRejectsAnUnknownUserId() {
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> service.byId(id)
        );

        assertEquals(Id.FIELD, exception.getField());
        assertEquals("User not found with the provided id.", exception.getMessage());
    }

    @Test
    void itRejectsAnUnknownUserEmail() {
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> service.byEmail(email)
        );

        assertEquals(Email.FIELD, exception.getField());
        assertEquals("User not found with the provided e-mail.", exception.getMessage());
    }
}
