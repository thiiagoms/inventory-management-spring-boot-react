package io.thiiagoms.ims.fixtures.category.domain;

import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public class CategoryFake {
  private Id id;
  private Title title;
  private Description description;

  private CategoryFake() {
    id = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    title = new Title("Office");
    description = new Description("Office products");
  }

  public static CategoryFake start() {
    return new CategoryFake();
  }

  public CategoryFake withId(Id id) {
    this.id = id;
    return this;
  }

  public CategoryFake withTitle(Title title) {
    this.title = title;
    return this;
  }

  public CategoryFake withDescription(Description description) {
    this.description = description;
    return this;
  }

  public Category build() {
    return Category.rehydrate(id, title, description);
  }
}
