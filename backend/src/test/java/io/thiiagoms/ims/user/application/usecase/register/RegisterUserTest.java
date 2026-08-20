package io.thiiagoms.ims.user.application.usecase.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.thiiagoms.ims.fixtures.user.domain.UserFake;
import io.thiiagoms.ims.fixtures.user.infrastructure.persistence.repository.UserMemoryRepository;
import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.application.exception.EmailAlreadyExistsException;
import io.thiiagoms.ims.user.application.exception.PhoneAlreadyExistsException;
import io.thiiagoms.ims.user.application.service.UserUniqueness;
import io.thiiagoms.ims.user.domain.Role;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.security.PasswordEncoder;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordHash;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;
import io.thiiagoms.ims.user.domain.valueobject.Phone;

@ExtendWith(MockitoExtension.class)
public class RegisterUserTest {

    private RegisterUserData data;

    @Mock
    private IdentityGenerator identityGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRepository repository;

    private UserUniqueness userUniqueness;

    private RegisterUser useCase;

    @BeforeEach
    void setUp() {
        this.data = new RegisterUserData(
            new Name("Peter Parker"),
            new Email("ilovephp@gmail.com"),
            new PasswordPlain("P4sSw0rd!!#)"),
            new Phone("11999999999")
        );

        this.repository = new UserMemoryRepository();
        this.userUniqueness = new UserUniqueness(repository);
        this.useCase = new RegisterUser(
            repository,
            passwordEncoder,
            userUniqueness,
            identityGenerator
        );
    }

    @Test
    void itRegistersAnUserAndReturnCreatedUserData() {
        Id expectedGeneratedId = new Id("baa86496-638f-4beb-bc03-de2f7589ad63");
        PasswordHash expectedPasswordHashed = new PasswordHash(
            "$2y$12$YAZ54ki7cF3Oa/Em6/9WW.MwaAl65WbjV6nZl63qR9SqCmNBH5RT."
        );

        when(this.identityGenerator.generate()).thenReturn(expectedGeneratedId);
        when(this.passwordEncoder.encode(data.password())).thenReturn(expectedPasswordHashed);

        var user = this.useCase.execute(data);

        assertEquals(expectedGeneratedId.value(), user.id());
        assertEquals(data.name().value(), user.name());
        assertEquals(data.email().value(), user.email());
        assertEquals(data.phone().value(), user.phone());
        assertEquals(Role.MANAGER.name(), user.role());
    }

    @Test
    void itRejectsAnEmailAlreadyOwnedByAnotherUser() {
        repository.save(UserFake.start().withEmail(data.email()).build());

        EmailAlreadyExistsException exception = assertThrows(
            EmailAlreadyExistsException.class,
            () -> useCase.execute(data)
        );

        assertEquals(Email.FIELD, exception.getField());
        assertEquals("An User with this e-mail already exists.", exception.getMessage());

        verify(identityGenerator, never()).generate();
        verify(passwordEncoder, never()).encode(data.password());
    }

    @Test
    void itRejectsAPhoneAlreadyOwnedByAnotherUser() {
        repository.save(UserFake.start()
                .withEmail(new Email("another@example.com"))
                .withPhone(data.phone())
                .build());

        PhoneAlreadyExistsException exception = assertThrows(
                PhoneAlreadyExistsException.class,
                () -> useCase.execute(data));

        assertEquals(Phone.FIELD, exception.getField());
        assertEquals("An User with this phone already exists.", exception.getMessage());
        verify(identityGenerator, never()).generate();
        verify(passwordEncoder, never()).encode(data.password());
    }
}
