package io.thiiagoms.ims.user.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import io.thiiagoms.ims.user.domain.security.PasswordEncoder;
import io.thiiagoms.ims.user.domain.valueobject.PasswordHash;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;

@Component
public class BCryptPasswordHasher implements PasswordEncoder {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public PasswordHash encode(PasswordPlain password) {
        var hash = encoder.encode(password.value());
        if (hash == null) {
            throw new IllegalStateException("BCrypt did not produce a password hash.");
        }

        return new PasswordHash(hash);
    }

    @Override
    public Boolean matches(PasswordPlain password, PasswordHash hash) {
        return encoder.matches(password.value(), hash.value());
    }
}
