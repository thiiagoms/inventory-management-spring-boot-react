package io.thiiagoms.ims.shared.presentation.http.exception;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String field,
        String message) {
}
