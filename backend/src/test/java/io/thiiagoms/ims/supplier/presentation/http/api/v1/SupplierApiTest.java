package io.thiiagoms.ims.supplier.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.category.presentation.http.api.v1.CategoryApiTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class SupplierApiTest extends CategoryApiTestSupport {
  private static final String ENDPOINT = "/api/suppliers";

  @Autowired
  SupplierApiTest(MockMvc mockMvc) {
    super(mockMvc);
  }

  @Test
  void itCreatesRetrievesUpdatesListsAndDeletesASupplier() throws Exception {
    String token = authenticate();
    String id =
        postJsonAndReturnId(
            ENDPOINT,
            new SupplierRequest(
                "Acme Supplies Ltda",
                "11.222.333/0001-81",
                "Praça da Sé, São Paulo - SP, 01001-000"),
            token);

    getJson(ENDPOINT + "/" + id, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.socialName").value("Acme Supplies Ltda"))
        .andExpect(jsonPath("$.cnpj").value("11222333000181"))
        .andExpect(jsonPath("$.createdAt").isNotEmpty());

    patchJson(
            ENDPOINT + "/" + id,
            new SupplierRequest(
                "Acme Distribution Ltda",
                "11.222.333/0001-81",
                "Avenida Paulista, São Paulo - SP, 01310-100"),
            token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.socialName").value("Acme Distribution Ltda"));

    String page =
        getJson(ENDPOINT + "?page=0&size=100", token)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    assertThat(page).contains(id);

    deleteJson(ENDPOINT + "/" + id, token).andExpect(status().isNoContent());
    getJson(ENDPOINT + "/" + id, token).andExpect(status().isNotFound());
  }

  @Test
  void itRejectsADuplicateCnpj() throws Exception {
    String token = authenticate();
    var request =
        new SupplierRequest(
            "Acme Supplies Ltda", "11.222.333/0001-81", "Praça da Sé, São Paulo - SP, 01001-000");
    postJson(ENDPOINT, request, token).andExpect(status().isCreated());

    postJson(
            ENDPOINT,
            new SupplierRequest("Another Supplier", request.cnpj(), request.address()),
            token)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.field").value("cnpj"));
  }

  @Test
  void itRequiresAuthentication() throws Exception {
    postJson(
            ENDPOINT,
            new SupplierRequest(
                "Acme Supplies Ltda",
                "11.222.333/0001-81",
                "Praça da Sé, São Paulo - SP, 01001-000"))
        .andExpect(status().isUnauthorized());
  }

  private record SupplierRequest(String socialName, String cnpj, String address) {}
}
