package io.thiiagoms.ims.category.presentation.http.api.v1.update;

import io.thiiagoms.ims.category.application.usecase.update.UpdateCategory;
import io.thiiagoms.ims.category.presentation.http.api.v1.CategoryController;
import io.thiiagoms.ims.category.presentation.http.api.v1.shared.CategoryResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateCategoryController implements CategoryController {
  private final UpdateCategory useCase;

  UpdateCategoryController(UpdateCategory useCase) {
    this.useCase = useCase;
  }

  @PatchMapping("/{id}")
  public CategoryResponse update(
      @PathVariable String id, @RequestBody UpdateCategoryRequest request) {
    return CategoryResponse.from(useCase.execute(request.toCommand(id)));
  }
}
