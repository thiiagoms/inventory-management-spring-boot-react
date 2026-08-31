package io.thiiagoms.ims.product.presentation.http.api.v1.destroy;

import io.thiiagoms.ims.product.application.usecase.destroy.DestroyProduct;
import io.thiiagoms.ims.product.presentation.http.api.v1.ProductController;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DestroyProductController implements ProductController {

  private final DestroyProduct useCase;

  DestroyProductController(DestroyProduct useCase) {
    this.useCase = useCase;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void destroy(@PathVariable String id) {
    useCase.execute(new Id(id));
  }
}
