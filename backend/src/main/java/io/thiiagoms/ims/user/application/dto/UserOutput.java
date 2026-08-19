package io.thiiagoms.ims.user.application.dto;

import io.thiiagoms.ims.user.domain.User;

public record UserOutput(String id, String name, String email, String phone, String role) {

    public static UserOutput from(User user) {
        return new UserOutput(
            user.id().value(),
            user.name().value(),
            user.email().value(),
            user.phone().value(),
            user.role().toString()
        );
    }
}
