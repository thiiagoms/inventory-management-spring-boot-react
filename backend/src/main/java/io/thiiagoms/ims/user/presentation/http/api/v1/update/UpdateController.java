package io.thiiagoms.ims.user.presentation.http.api.v1.update;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.thiiagoms.ims.shared.presentation.http.exception.ErrorResponse;
import io.thiiagoms.ims.user.application.usecase.update.UpdateUser;
import io.thiiagoms.ims.user.presentation.http.api.v1.UserController;
import io.thiiagoms.ims.user.presentation.http.api.v1.shared.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Users", description = "User store and authentication operations")
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
public class UpdateController implements UserController {

  private final UpdateUser useCase;

  UpdateController(UpdateUser useCase) {
    this.useCase = useCase;
  }

  @PatchMapping("/{id}")
  @PreAuthorize("#id == authentication.name")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(
      summary = "Update the authenticated user's profile",
      description = "Only the user identified by the bearer token may update the matching profile.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Profile updated successfully",
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
                              "name": "Jane Doe",
                              "email": "jane.doe@gmail.com",
                              "phone": "11988887777"
                            }
                            """))),
    @ApiResponse(
        responseCode = "400",
        description = "An update field violates user validation rules",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "401", description = "A valid bearer token is required"),
    @ApiResponse(
        responseCode = "403",
        description = "The authenticated user does not own the requested profile"),
    @ApiResponse(
        responseCode = "404",
        description = "The requested user does not exist",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
        responseCode = "409",
        description = "The provided e-mail or phone belongs to another user",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
        responseCode = "422",
        description = "No profile changes were provided or detected",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public UserResponse update(@PathVariable String id, @Valid @RequestBody UpdateRequest request) {
    return UserResponse.from(useCase.execute(request.toCommand(id)));
  }
}
