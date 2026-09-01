package io.thiiagoms.ims.category.presentation.http.api.v1.retrieve;

import io.thiiagoms.ims.category.application.usecase.retrieve.RetrieveCategories;
import io.thiiagoms.ims.category.application.usecase.retrieve.RetrieveCategory;
import io.thiiagoms.ims.category.presentation.http.api.v1.CategoryController;
import io.thiiagoms.ims.category.presentation.http.api.v1.shared.CategoryPageResponse;
import io.thiiagoms.ims.category.presentation.http.api.v1.shared.CategoryResponse;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetrieveCategoryController implements CategoryController {
  private final RetrieveCategory retrieveCategory;
  private final RetrieveCategories retrieveCategories;

  RetrieveCategoryController(
      RetrieveCategory retrieveCategory, RetrieveCategories retrieveCategories) {
    this.retrieveCategory = retrieveCategory;
    this.retrieveCategories = retrieveCategories;
  }

  @GetMapping("/{id}")
  public CategoryResponse show(@PathVariable String id) {
    return CategoryResponse.from(retrieveCategory.execute(new Id(id)));
  }

  @GetMapping
  public CategoryPageResponse index(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
    return CategoryPageResponse.from(retrieveCategories.execute(new Pagination(page, size)));
  }
}
