package io.thiiagoms.ims.user.domain.security;

import io.thiiagoms.ims.user.domain.valueobject.PasswordHash;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;

public interface PasswordEncoder {

    PasswordHash encode(PasswordPlain password);

    Boolean matches(PasswordPlain password, PasswordHash hash);
}
