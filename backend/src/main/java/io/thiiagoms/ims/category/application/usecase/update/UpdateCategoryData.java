package io.thiiagoms.ims.category.application.usecase.update;

import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Optional;

public record UpdateCategoryData(Id id, Optional<Title> title, Optional<Description> description) {}
