package io.thiiagoms.ims.user.presentation.http.api.v1.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.thiiagoms.ims.shared.presentation.http.exception.ErrorResponse;
import io.thiiagoms.ims.user.application.dto.AuthenticationOutput;
import io.thiiagoms.ims.user.application.usecase.auth.authenticate.Authenticate;
import io.thiiagoms.ims.user.presentation.http.api.v1.UserController;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Users", description = "User store and authentication operations")
public class AuthenticateController implements UserController {

  private final Authenticate useCase;

  AuthenticateController(Authenticate useCase) {
    this.useCase = useCase;
  }

  @PostMapping("/authenticate")
  @Operation(
      summary = "Authenticate a user",
      description =
          """
            Authenticates a user using their e-mail and password. On success, returns a signed JWT
            and its expiration timestamp and records the user's latest login time.
            """)
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User authenticated successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AuthenticationOutput.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                    {
                                        "token": "eyJhbGciOiJIUzI1NiJ9...signature",
                                        "expiresAt": "2026-08-22T22:00:00Z"
                                    }
                                    """))),
    @ApiResponse(
        responseCode = "400",
        description = "Request fields are blank or violate validation rules",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                    {
                                        "timestamp": "2026-08-22T21:00:00Z",
                                        "status": 400,
                                        "error": "validation_failed",
                                        "field": "email",
                                        "message": "Invalid e-mail address."
                                    }
                                    """))),
    @ApiResponse(
        responseCode = "401",
        description = "The e-mail or password is incorrect",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                    {
                                        "timestamp": "2026-08-22T21:00:00Z",
                                        "status": 401,
                                        "error": "authorization_failed",
                                        "field": "credentials",
                                        "message": "Invalid e-mail or password."
                                    }
                                    """)))
  })
  public AuthenticationOutput authenticate(@Valid @RequestBody AuthenticateRequest request) {
    return useCase.execute(request.toCommand());
  }
}
