package io.thiiagoms.ims.shared.presentation.http.exception;

import io.thiiagoms.ims.shared.application.exception.NotFoundException;
import io.thiiagoms.ims.shared.application.exception.ResourceAlreadyExistsException;
import io.thiiagoms.ims.shared.application.exception.ResourceNotChangedException;
import io.thiiagoms.ims.shared.domain.exception.AuthorizationFailedException;
import io.thiiagoms.ims.shared.domain.exception.ForbiddenAccessException;
import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHttpHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException exception) {
    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            "validation_failed",
            exception.getName(),
            "The query parameter '%s' must be a valid integer.".formatted(exception.getName()));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception) {
    FieldError fieldError =
        exception.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);

    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            "validation_failed",
            fieldError != null ? fieldError.getField() : "request",
            fieldError != null ? fieldError.getDefaultMessage() : "Request validation failed.");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(InvalidDomainArgumentException.class)
  public ResponseEntity<ErrorResponse> handleInvalidArgument(
      InvalidDomainArgumentException exception) {
    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            "validation_failed",
            exception.getField(),
            exception.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(
      ResourceAlreadyExistsException exception) {
    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.CONFLICT.value(),
            "resource_already_exists",
            exception.getField(),
            exception.getMessage());

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(AuthorizationFailedException.class)
  public ResponseEntity<ErrorResponse> handleAuthorizationFailed(
      AuthorizationFailedException exception) {
    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "authorization_failed",
            exception.getField(),
            exception.getMessage());

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(ForbiddenAccessException.class)
  public ResponseEntity<ErrorResponse> handleForbiddenAccess(ForbiddenAccessException exception) {
    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.FORBIDDEN.value(),
            "forbidden_access",
            exception.getField(),
            exception.getMessage());

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exception) {
    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.FORBIDDEN.value(),
            "forbidden_access",
            "authorization",
            "You cannot access another user's profile.");

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }

  @ExceptionHandler(NotFoundException.class)
  ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.NOT_FOUND.value(),
            "resource_not_found",
            exception.getField(),
            exception.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(ResourceNotChangedException.class)
  ResponseEntity<ErrorResponse> handleResourceNotChanged(ResourceNotChangedException exception) {
    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.UNPROCESSABLE_CONTENT.value(),
            "resource_not_changed",
            exception.getField(),
            exception.getMessage());

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
    ErrorResponse response =
        new ErrorResponse(
            Instant.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "internal_server_error",
            "unknow_field",
            exception.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
