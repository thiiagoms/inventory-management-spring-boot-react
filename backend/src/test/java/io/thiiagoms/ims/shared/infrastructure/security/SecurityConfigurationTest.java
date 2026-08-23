package io.thiiagoms.ims.shared.infrastructure.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTest {

  private final MockMvc mockMvc;

  @Autowired
  SecurityConfigurationTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void itAllowsAnonymousUserRegistrationRequests() throws Exception {
    mockMvc
        .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
        .andExpect(status().isBadRequest());
  }

  @Test
  void itAllowsAnonymousUserAuthenticationRequests() throws Exception {
    mockMvc
        .perform(
            post("/api/users/authenticate").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
        .andExpect(status().isBadRequest());
  }
}
