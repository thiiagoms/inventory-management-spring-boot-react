package io.thiiagoms.ims.user.application.usecase.auth.authenticate;

import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;

public record AuthenticateData(Email email, PasswordPlain password) {}
