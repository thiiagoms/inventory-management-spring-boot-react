package io.thiiagoms.ims.category.application.usecase.register;

import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;

public record RegisterCategoryData(Title title, Description description) {}
