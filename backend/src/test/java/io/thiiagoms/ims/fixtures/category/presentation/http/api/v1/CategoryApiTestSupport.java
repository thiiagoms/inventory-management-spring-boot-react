package io.thiiagoms.ims.fixtures.category.presentation.http.api.v1;

import io.thiiagoms.ims.fixtures.shared.presentation.http.ApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.UserApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.register.RegisterUserRequestBuilder;
import org.springframework.test.web.servlet.MockMvc;

public abstract class CategoryApiTestSupport extends ApiTestSupport {
  protected static final String ENDPOINT = "/api/categories";

  private final UserApiTestSupport users;

  protected CategoryApiTestSupport(MockMvc mockMvc) {
    super(mockMvc);
    this.users = new UserApiTestSupport(this);
  }

  protected String authenticate() throws Exception {
    var user = RegisterUserRequestBuilder.start().build();
    users.createUserAndReturnId(user);
    return users.authenticateAndReturnToken(user);
  }

  protected String createCategory(String token, String title, String description) throws Exception {
    return postJsonAndReturnId(ENDPOINT, new CategoryRequest(title, description), token);
  }

  public record CategoryRequest(String title, String description) {}
}
