package io.thiiagoms.ims.category.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.Test;

public class CategoryTest {

  @Test
  void itRegistersACategoryWithGivenValues() {
    var id = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    var title = new Title("Office");
    var description = new Description("Office products");

    Category category = Category.register(id, title, description);

    assertEquals(id, category.id());
    assertEquals(title, category.title());
    assertEquals(description, category.description());
  }

  @Test
  void itRehydratesACategoryWithGivenValues() {
    var id = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    var title = new Title("Office");
    var description = new Description("Office products");

    Category category = Category.rehydrate(id, title, description);

    assertEquals(id, category.id());
    assertEquals(title, category.title());
    assertEquals(description, category.description());
  }

  @Test
  void itChangesTheCategoryTitle() {
    var category = CategoryFake.start().build();
    var newTitle = new Title("Home office");

    category.changeTitleTo(newTitle);

    assertEquals(newTitle, category.title());
  }

  @Test
  void itChangesTheCategoryDescription() {
    var category = CategoryFake.start().build();
    var newDescription = new Description("Products for home offices");

    category.changeDescriptionTo(newDescription);

    assertEquals(newDescription, category.description());
  }

  @Test
  void itDoesNotChangeTheCategoryTitleWhenItIsUnchanged() {
    var currentTitle = new Title("Home office");
    var equivalentTitle = new Title("  home   OFFICE ");
    var category = CategoryFake.start().withTitle(currentTitle).build();

    category.changeTitleTo(equivalentTitle);

    assertSame(currentTitle, category.title());
  }

  @Test
  void itDoesNotChangeTheCategoryDescriptionWhenItIsUnchanged() {
    var currentDescription = new Description("Products for home offices");
    var equivalentDescription = new Description("  PRODUCTS   for HOME offices ");
    var category = CategoryFake.start().withDescription(currentDescription).build();

    category.changeDescriptionTo(equivalentDescription);

    assertSame(currentDescription, category.description());
  }

  @Test
  void itRejectsARegistrationWithoutAnId() {
    var exception =
        assertThrows(
            InvalidDomainArgumentException.class,
            () -> Category.register(null, new Title("Office"), new Description("Office products")));

    assertEquals(Id.FIELD, exception.getField());
  }

  @Test
  void itRejectsARegistrationWithoutATitle() {
    var exception =
        assertThrows(
            InvalidDomainArgumentException.class,
            () ->
                Category.register(
                    new Id("3780baf2-deed-448d-a763-ce7b06efd394"),
                    null,
                    new Description("Office products")));

    assertEquals(Title.FIELD, exception.getField());
  }

  @Test
  void itRejectsARegistrationWithoutADescription() {
    var exception =
        assertThrows(
            InvalidDomainArgumentException.class,
            () ->
                Category.register(
                    new Id("3780baf2-deed-448d-a763-ce7b06efd394"), new Title("Office"), null));

    assertEquals(Description.FIELD, exception.getField());
  }

  @Test
  void itRejectsChangingTheTitleToNull() {
    var category = CategoryFake.start().build();

    var exception =
        assertThrows(InvalidDomainArgumentException.class, () -> category.changeTitleTo(null));

    assertEquals(Title.FIELD, exception.getField());
  }

  @Test
  void itRejectsChangingTheDescriptionToNull() {
    var category = CategoryFake.start().build();

    var exception =
        assertThrows(
            InvalidDomainArgumentException.class, () -> category.changeDescriptionTo(null));

    assertEquals(Description.FIELD, exception.getField());
  }
}
