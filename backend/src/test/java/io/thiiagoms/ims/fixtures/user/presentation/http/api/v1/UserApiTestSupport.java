package io.thiiagoms.ims.fixtures.user.presentation.http.api.v1;

import io.thiiagoms.ims.fixtures.shared.presentation.http.ApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.register.RegisterUserRequestBuilder;
import io.thiiagoms.ims.user.presentation.http.api.v1.auth.AuthenticateRequest;
import io.thiiagoms.ims.user.presentation.http.api.v1.register.RegisterRequest;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class UserApiTestSupport {

  public static final String ENDPOINT = "/api/users";

  private final ApiTestSupport apiTestSupport;

  public UserApiTestSupport(ApiTestSupport apiTestSupport) {
    this.apiTestSupport = apiTestSupport;
  }

  public String createUserAndReturnId() throws Exception {
    return createUserAndReturnId(RegisterUserRequestBuilder.start().build());
  }

  public String createUserAndReturnId(RegisterRequest request) throws Exception {
    return apiTestSupport.postJsonAndReturnId(ENDPOINT, request);
  }

  public String authenticateAndReturnToken(RegisterRequest registeredUser) throws Exception {
    var request = new AuthenticateRequest(registeredUser.email(), registeredUser.password());
    return apiTestSupport.postJsonAndReturnField(
        ENDPOINT + "/authenticate", request, "token", MockMvcResultMatchers.status().isOk());
  }
}
