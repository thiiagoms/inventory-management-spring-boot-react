package io.thiiagoms.ims.user.application.usecase.register;

import org.springframework.transaction.annotation.Transactional;

import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import io.thiiagoms.ims.user.application.dto.UserOutput;
import io.thiiagoms.ims.user.application.service.UserUniqueness;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.security.PasswordEncoder;

public class RegisterUser {

    private final UserRepository repository;

    private final UserUniqueness userUniqueness;

    private final PasswordEncoder encoder;

    private final IdentityGenerator identityGenerator;

    public RegisterUser(
        UserRepository repository,
        PasswordEncoder encoder,
        UserUniqueness userUniqueness,
        IdentityGenerator identityGenerator
    ) {
        this.encoder = encoder;
        this.repository = repository;
        this.userUniqueness = userUniqueness;
        this.identityGenerator = identityGenerator;
    }

    @Transactional
    public UserOutput execute(RegisterUserData data) {

        userUniqueness.ensureEmailIsAvailable(data.email());
        userUniqueness.ensurePhoneIsAvailable(data.phone());

        var user = build(data);

        repository.save(user);

        return UserOutput.from(user);
    }

    private User build(RegisterUserData data) {
        return User.register(
            identityGenerator.generate(),
            data.name(),
            data.email(),
            data.phone(),
            encoder.encode(data.password())
        );
    }
}
