package io.thiiagoms.ims.user.presentation.http.api.v1.register;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.thiiagoms.ims.shared.presentation.http.exception.ErrorResponse;
import io.thiiagoms.ims.user.application.usecase.register.RegisterUser;
import io.thiiagoms.ims.user.presentation.http.api.v1.BaseUserApiController;
import io.thiiagoms.ims.user.presentation.http.api.v1.shared.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Users", description = "User management operations")
public class RegisterUserApiController implements BaseUserApiController {

  private final RegisterUser useCase;

  RegisterUserApiController(RegisterUser useCase) {
    this.useCase = useCase;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Register A user",
      description =
          """
                        Creates an manager user user. The name, e-mail, and phone are normalized; both e-mail and
                        phone must be unique. The password is hashed before persistence and must contain at least eight
                        characters, including uppercase, lowercase, numeric, and special characters.
                        """)
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "User registered successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                        {
                                                "id": "2f6d26c2-4c34-4fa2-a90e-469e8b594ae9",
                                                "name": "John Doe",
                                                "email": "john.doe@gmail.com",
                                                "phone": "11999999999"
                                        }
                                        """))),
    @ApiResponse(
        responseCode = "400",
        description = "Request fields are blank or violate user validation rules",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                        {
                                                "timestamp": "2026-08-03T13:00:00Z",
                                                "status": 400,
                                                "error": "validation_failed",
                                                "message": "Invalid e-mail address."
                                        }
                                        """))),
    @ApiResponse(
        responseCode = "409",
        description = "A user with the provided e-mail or phone already exists",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                        {
                                                "timestamp": "2026-08-03T13:00:00Z",
                                                "status": 409,
                                                "error": "resource_already_exists",
                                                "field": "phone",
                                                "message": "A user with this phone already exists."
                                        }
                                        """)))
  })
  public UserResponse store(@Valid @RequestBody RegisterUserRequest request) {
    var user = useCase.execute(request.toCommand());
    return UserResponse.from(user);
  }
}
