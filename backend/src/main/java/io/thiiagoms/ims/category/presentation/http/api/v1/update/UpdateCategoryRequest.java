package io.thiiagoms.ims.category.presentation.http.api.v1.update;

import io.thiiagoms.ims.category.application.usecase.update.UpdateCategoryData;
import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Optional;

public record UpdateCategoryRequest(String title, String description) {
  public UpdateCategoryData toCommand(String id) {
    return new UpdateCategoryData(
        new Id(id),
        Optional.ofNullable(title).map(Title::new),
        Optional.ofNullable(description).map(Description::new));
  }
}
