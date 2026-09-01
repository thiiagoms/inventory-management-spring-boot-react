package io.thiiagoms.ims.product.presentation.http.api.v1.register;

import io.thiiagoms.ims.product.application.usecase.register.RegisterProduct;
import io.thiiagoms.ims.product.presentation.http.api.v1.ProductController;
import io.thiiagoms.ims.product.presentation.http.api.v1.shared.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterProductController implements ProductController {
  private final RegisterProduct useCase;

  RegisterProductController(RegisterProduct useCase) {
    this.useCase = useCase;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponse store(@Valid @RequestBody RegisterProductRequest request) {
    return ProductResponse.from(useCase.execute(request.toCommand()));
  }
}
