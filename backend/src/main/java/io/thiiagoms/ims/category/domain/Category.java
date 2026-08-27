package io.thiiagoms.ims.category.domain;

import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.support.Guard;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public class Category {

  private final Id id;

  private Title title;

  private Description description;

  private Category(Id id, Title title, Description description) {
    Guard.againstNull(Id.FIELD, id);
    Guard.againstNull(Title.FIELD, title);
    Guard.againstNull(Description.FIELD, description);
    this.id = id;
    this.title = title;
    this.description = description;
  }

  public static Category register(Id id, Title title, Description description) {
    return new Category(id, title, description);
  }

  public static Category rehydrate(Id id, Title title, Description description) {
    return new Category(id, title, description);
  }

  public Id id() {
    return this.id;
  }

  public Title title() {
    return this.title;
  }

  public Description description() {
    return this.description;
  }

  public void changeTitleTo(Title title) {
    Guard.againstNull(Title.FIELD, title);

    if (this.title.equals(title)) {
      return;
    }

    this.title = title;
  }

  public void changeDescriptionTo(Description description) {
    Guard.againstNull(Description.FIELD, description);

    if (this.description.equals(description)) {
      return;
    }

    this.description = description;
  }
}
