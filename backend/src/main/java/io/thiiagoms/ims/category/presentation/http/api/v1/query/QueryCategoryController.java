package io.thiiagoms.ims.category.presentation.http.api.v1.query;

import io.thiiagoms.ims.category.application.usecase.find.FindCategory;
import io.thiiagoms.ims.category.application.usecase.list.ListCategories;
import io.thiiagoms.ims.category.presentation.http.api.v1.CategoryController;
import io.thiiagoms.ims.category.presentation.http.api.v1.shared.CategoryResponse;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryCategoryController implements CategoryController {
  private final FindCategory find;
  private final ListCategories list;

  QueryCategoryController(FindCategory find, ListCategories list) {
    this.find = find;
    this.list = list;
  }

  @GetMapping("/{id}")
  public CategoryResponse show(@PathVariable String id) {
    return CategoryResponse.from(find.execute(new Id(id)));
  }

  @GetMapping
  public List<CategoryResponse> list() {
    return list.execute().stream().map(CategoryResponse::from).toList();
  }
}
