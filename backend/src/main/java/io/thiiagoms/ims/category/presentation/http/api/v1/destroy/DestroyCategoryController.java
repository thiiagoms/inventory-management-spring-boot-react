package io.thiiagoms.ims.category.presentation.http.api.v1.destroy;

import io.thiiagoms.ims.category.application.usecase.destroy.DestroyCategory;
import io.thiiagoms.ims.category.presentation.http.api.v1.CategoryController;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DestroyCategoryController implements CategoryController {
  private final DestroyCategory useCase;

  DestroyCategoryController(DestroyCategory useCase) {
    this.useCase = useCase;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void destroy(@PathVariable String id) {
    useCase.execute(new Id(id));
  }
}
