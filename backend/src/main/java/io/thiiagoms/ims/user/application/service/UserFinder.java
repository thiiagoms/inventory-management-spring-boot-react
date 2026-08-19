package io.thiiagoms.ims.user.application.service;

import java.util.Optional;
import java.util.function.Supplier;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.application.exception.UserNotFoundException;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.valueobject.Email;

public class UserFinder {

    private final UserRepository repository;

    public UserFinder(UserRepository repository) {
        this.repository = repository;
    }

    public User byId(Id id) {
        return findOrFail(
                () -> repository.findById(id),
                () -> UserNotFoundException.with("User not found with the provided id.", Id.FIELD));
    }

    public User byEmail(Email email) {
        return findOrFail(
                () -> repository.findByEmail(email),
                () -> UserNotFoundException.with("User not found with the provided e-mail.", Email.FIELD));
    }

    private <T> T findOrFail(
            Supplier<Optional<T>> resolver,
            Supplier<RuntimeException> exception) {
        return resolver.get()
                .orElseThrow(exception);
    }
}
