package pinup.backend.store.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class StoreSwaggerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Swagger 문서에 스토어 조회 API 경로가 포함된다")
    void storeQueryEndpointsAreDocumented() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/store/items']").exists())
                .andExpect(jsonPath("$.paths['/store/items/{itemId}']").exists());
    }

    @Test
    @DisplayName("Swagger 문서에 스토어 구매 API 경로가 포함된다")
    void storeCommandEndpointIsDocumented() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/store/items/{itemId}/purchase']").exists())
                .andExpect(jsonPath("$.paths['/store/items/{itemId}/purchase'].post.parameters[?(@.name=='userId')]").exists());
    }

    @Test
    @DisplayName("Swagger 문서에 스토어 관리자 CRUD 경로가 포함된다")
    void storeAdminEndpointsAreDocumented() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/store/admin/items']").exists())
                .andExpect(jsonPath("$.paths['/store/admin/items'].post.requestBody").exists())
                .andExpect(jsonPath("$.paths['/store/admin/items/{itemId}']").exists())
                .andExpect(jsonPath("$.paths['/store/admin/items/{itemId}'].patch.requestBody").exists());
    }

}
