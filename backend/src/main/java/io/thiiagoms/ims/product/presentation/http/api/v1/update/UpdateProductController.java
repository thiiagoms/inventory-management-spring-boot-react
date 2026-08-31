package io.thiiagoms.ims.product.presentation.http.api.v1.update;

import io.thiiagoms.ims.product.application.usecase.update.UpdateProduct;
import io.thiiagoms.ims.product.presentation.http.api.v1.ProductController;
import io.thiiagoms.ims.product.presentation.http.api.v1.shared.ProductResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateProductController implements ProductController {
  private final UpdateProduct useCase;

  UpdateProductController(UpdateProduct useCase) {
    this.useCase = useCase;
  }

  @PatchMapping("/{id}")
  public ProductResponse update(
      @PathVariable String id, @RequestBody UpdateProductRequest request) {
    return ProductResponse.from(useCase.execute(request.toCommand(id)));
  }
}
