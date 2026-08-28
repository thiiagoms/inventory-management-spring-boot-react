package io.thiiagoms.ims.category.application.dto;

import io.thiiagoms.ims.category.domain.Category;

public record CategoryOutput(String id, String title, String description) {
  public static CategoryOutput from(Category category) {
    return new CategoryOutput(
        category.id().value(), category.title().value(), category.description().value());
  }
}
