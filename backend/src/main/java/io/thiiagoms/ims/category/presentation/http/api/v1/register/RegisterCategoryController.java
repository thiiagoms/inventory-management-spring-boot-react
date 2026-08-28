package io.thiiagoms.ims.category.presentation.http.api.v1.register;

import io.thiiagoms.ims.category.application.usecase.register.RegisterCategory;
import io.thiiagoms.ims.category.presentation.http.api.v1.CategoryController;
import io.thiiagoms.ims.category.presentation.http.api.v1.shared.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterCategoryController implements CategoryController {
  private final RegisterCategory useCase;

  RegisterCategoryController(RegisterCategory useCase) {
    this.useCase = useCase;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryResponse store(@Valid @RequestBody RegisterCategoryRequest request) {
    return CategoryResponse.from(useCase.execute(request.toCommand()));
  }
}
