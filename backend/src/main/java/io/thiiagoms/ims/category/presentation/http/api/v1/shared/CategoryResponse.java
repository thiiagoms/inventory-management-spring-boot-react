package io.thiiagoms.ims.category.presentation.http.api.v1.shared;

import io.thiiagoms.ims.category.application.dto.CategoryOutput;

public record CategoryResponse(String id, String title, String description) {
  public static CategoryResponse from(CategoryOutput output) {
    return new CategoryResponse(output.id(), output.title(), output.description());
  }
}
