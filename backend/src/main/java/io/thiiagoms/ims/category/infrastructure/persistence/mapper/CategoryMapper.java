package io.thiiagoms.ims.category.infrastructure.persistence.mapper;

import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.category.infrastructure.persistence.model.CategoryJpa;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.UUID;

public final class CategoryMapper {
  private CategoryMapper() {}

  public static CategoryJpa toPersistence(Category category) {
    return CategoryJpa.builder()
        .id(UUID.fromString(category.id().value()))
        .title(category.title().value())
        .description(category.description().value())
        .build();
  }

  public static Category toDomain(CategoryJpa category) {
    return Category.rehydrate(
        new Id(category.getId().toString()),
        new Title(category.getTitle()),
        new Description(category.getDescription()));
  }
}
