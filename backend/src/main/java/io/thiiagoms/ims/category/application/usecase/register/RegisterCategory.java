package io.thiiagoms.ims.category.application.usecase.register;

import io.thiiagoms.ims.category.application.dto.CategoryOutput;
import io.thiiagoms.ims.category.application.service.CategoryUniqueness;
import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import org.springframework.transaction.annotation.Transactional;

public class RegisterCategory {

  private final CategoryRepository repository;

  private final IdentityGenerator identityGenerator;
  private final CategoryUniqueness uniqueness;

  public RegisterCategory(
      CategoryRepository repository,
      IdentityGenerator identityGenerator,
      CategoryUniqueness uniqueness) {
    this.repository = repository;
    this.identityGenerator = identityGenerator;
    this.uniqueness = uniqueness;
  }

  @Transactional
  public CategoryOutput execute(RegisterCategoryData data) {
    uniqueness.ensureTitleIsAvailable(data.title());
    var category = build(data);

    repository.save(category);

    return CategoryOutput.from(category);
  }

  private Category build(RegisterCategoryData data) {
    return Category.register(identityGenerator.generate(), data.title(), data.description());
  }
}
