package io.thiiagoms.ims.user.presentation.http.api.v1.shared.response;

import io.thiiagoms.ims.user.application.dto.UserOutput;

public record UserResponse(
    String id,
    String name,
    String email,
    String phone
) {
    public static UserResponse from(UserOutput user) {
        return new UserResponse(
            user.id(),
            user.name(),
            user.email(),
            user.phone()
        );
    }
}
