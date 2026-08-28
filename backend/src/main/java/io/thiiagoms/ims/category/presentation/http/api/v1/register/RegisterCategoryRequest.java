package io.thiiagoms.ims.category.presentation.http.api.v1.register;

import io.thiiagoms.ims.category.application.usecase.register.RegisterCategoryData;
import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import jakarta.validation.constraints.NotBlank;

public record RegisterCategoryRequest(@NotBlank String title, @NotBlank String description) {
  public RegisterCategoryData toCommand() {
    return new RegisterCategoryData(new Title(title), new Description(description));
  }
}
