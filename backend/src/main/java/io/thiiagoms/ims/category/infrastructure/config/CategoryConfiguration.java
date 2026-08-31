package io.thiiagoms.ims.category.infrastructure.config;

import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.category.application.service.CategoryUniqueness;
import io.thiiagoms.ims.category.application.usecase.destroy.DestroyCategory;
import io.thiiagoms.ims.category.application.usecase.register.RegisterCategory;
import io.thiiagoms.ims.category.application.usecase.retrieve.RetrieveCategories;
import io.thiiagoms.ims.category.application.usecase.retrieve.RetrieveCategory;
import io.thiiagoms.ims.category.application.usecase.update.UpdateCategory;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryConfiguration {
  @Bean
  CategoryFinder categoryFinder(CategoryRepository repository) {
    return new CategoryFinder(repository);
  }

  @Bean
  CategoryUniqueness categoryUniqueness(CategoryRepository repository) {
    return new CategoryUniqueness(repository);
  }

  @Bean
  RegisterCategory registerCategory(
      CategoryRepository repository, IdentityGenerator generator, CategoryUniqueness uniqueness) {
    return new RegisterCategory(repository, generator, uniqueness);
  }

  @Bean
  RetrieveCategory retrieveCategory(CategoryFinder finder) {
    return new RetrieveCategory(finder);
  }

  @Bean
  RetrieveCategories retrieveCategories(CategoryRepository repository) {
    return new RetrieveCategories(repository);
  }

  @Bean
  UpdateCategory updateCategory(
      CategoryFinder finder, CategoryRepository repository, CategoryUniqueness uniqueness) {
    return new UpdateCategory(finder, repository, uniqueness);
  }

  @Bean
  DestroyCategory destroyCategory(CategoryFinder finder, CategoryRepository repository) {
    return new DestroyCategory(finder, repository);
  }
}
