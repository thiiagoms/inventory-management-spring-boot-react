package io.thiiagoms.ims.category.application.usecase.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.thiiagoms.ims.category.application.exception.CategoryTitleAlreadyExistsException;
import io.thiiagoms.ims.category.application.service.CategoryUniqueness;
import io.thiiagoms.ims.category.domain.repository.CategoryRepository;
import io.thiiagoms.ims.category.domain.valueobject.Description;
import io.thiiagoms.ims.category.domain.valueobject.Title;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository.CategoryMemoryRepository;
import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RegisterCategoryTest {
  private RegisterCategoryData data;

  @Mock private IdentityGenerator identityGenerator;

  private CategoryRepository repository;

  private RegisterCategory useCase;

  @BeforeEach
  void setUp() {
    data = new RegisterCategoryData(new Title("Office"), new Description("Office products"));
    repository = new CategoryMemoryRepository();
    useCase =
        new RegisterCategory(repository, identityGenerator, new CategoryUniqueness(repository));
  }

  @Test
  void itRegistersACategoryAndReturnsCreatedCategoryData() {
    var expectedGeneratedId = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    when(identityGenerator.generate()).thenReturn(expectedGeneratedId);

    var category = useCase.execute(data);

    assertEquals(expectedGeneratedId.value(), category.id());
    assertEquals(data.title().value(), category.title());
    assertEquals(data.description().value(), category.description());
  }

  @Test
  void itRejectsATitleAlreadyOwnedByAnotherCategory() {
    repository.save(CategoryFake.start().withTitle(data.title()).build());

    CategoryTitleAlreadyExistsException exception =
        assertThrows(CategoryTitleAlreadyExistsException.class, () -> useCase.execute(data));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals("A category with this title already exists.", exception.getMessage());
    verify(identityGenerator, never()).generate();
  }
}
