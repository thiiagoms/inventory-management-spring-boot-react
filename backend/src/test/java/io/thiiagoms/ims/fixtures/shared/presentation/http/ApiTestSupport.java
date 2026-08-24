package io.thiiagoms.ims.fixtures.shared.presentation.http;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ApiTestSupport {

  protected final MockMvc mockMvc;

  protected final ObjectMapper objectMapper;

  protected ApiTestSupport(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
    this.objectMapper = new ObjectMapper();
  }

  protected ResultActions postJson(String endpoint, Object request) throws Exception {
    return mockMvc.perform(
        post(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
  }

  protected ResultActions postJson(String endpoint, Object request, String accessToken)
      throws Exception {
    return mockMvc.perform(
        post(endpoint)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
  }

  protected ResultActions getJson(String endpoint) throws Exception {
    return mockMvc.perform(get(endpoint).accept(MediaType.APPLICATION_JSON));
  }

  protected ResultActions getJson(String endpoint, String accessToken) throws Exception {
    return mockMvc.perform(
        get(endpoint)
            .header("Authorization", "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON));
  }

  protected ResultActions deleteJson(String endpoint, String accessToken) throws Exception {
    return mockMvc.perform(
        delete(endpoint)
            .header("Authorization", "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON));
  }

  protected ResultActions deleteJson(String endpoint) throws Exception {
    return mockMvc.perform(delete(endpoint).accept(MediaType.APPLICATION_JSON));
  }

  protected ResultActions patchJson(String endpoint, Object request) throws Exception {
    return mockMvc.perform(
        patch(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
  }

  protected ResultActions patchJson(String endpoint, Object request, String accessToken)
      throws Exception {
    return mockMvc.perform(
        patch(endpoint)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
  }

  public String postJsonAndReturnId(String endpoint, Object request) throws Exception {
    return postJsonAndReturnField(endpoint, request, "id", status().isCreated());
  }

  public String postJsonAndReturnId(String endpoint, Object request, String accessToken)
      throws Exception {
    String response =
        postJson(endpoint, request, accessToken)
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return readId(response);
  }

  public String postJsonAndReturnField(
      String endpoint,
      Object request,
      String field,
      org.springframework.test.web.servlet.ResultMatcher expectedStatus)
      throws Exception {
    String response =
        postJson(endpoint, request)
            .andExpect(expectedStatus)
            .andReturn()
            .getResponse()
            .getContentAsString();

    return objectMapper.readTree(response).get(field).asString();
  }

  protected String readId(String response) throws Exception {
    return objectMapper.readTree(response).get("id").asString();
  }
}
