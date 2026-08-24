package io.thiiagoms.ims.user.presentation.http.api.v1.destroy;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.shared.presentation.http.exception.ErrorResponse;
import io.thiiagoms.ims.user.application.usecase.destroy.DestroyUser;
import io.thiiagoms.ims.user.presentation.http.api.v1.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Users", description = "User profile operations")
public class DestroyController implements UserController {

  private final DestroyUser useCase;

  DestroyController(DestroyUser useCase) {
    this.useCase = useCase;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("#id == authentication.name")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(
      summary = "Delete the authenticated user's profile",
      description = "Only the user identified by the bearer token may delete the matching profile.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Profile deleted successfully"),
    @ApiResponse(responseCode = "401", description = "A valid bearer token is required"),
    @ApiResponse(
        responseCode = "403",
        description = "The authenticated user does not own the requested profile"),
    @ApiResponse(
        responseCode = "404",
        description = "The requested user does not exist",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public void destroy(@PathVariable String id) {
    useCase.execute(new Id(id));
  }
}
