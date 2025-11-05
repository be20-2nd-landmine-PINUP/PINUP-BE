package pinup.backend.conquer.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import pinup.backend.conquer.command.application.dto.ConquerEndRequest;
import pinup.backend.conquer.command.application.dto.ConquerStartRequest;
import pinup.backend.conquer.command.domain.entity.ConquerSession;
import pinup.backend.conquer.command.domain.repository.ConquerSessionRepository;

import java.time.Duration;
import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/dummy_regions.sql", "/dummy_user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RealConquerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConquerSessionRepository conquerSessionRepository;

    @Test
    @WithMockUser
    @DisplayName("Real 정복 종료 API - 성공 (실제 DB 데이터 기반)")
    void RealEndConqueringTest() throws Exception {
        // Given: Start a conquer session
        ConquerStartRequest startRequest = new ConquerStartRequest(25.0, 75.0); // Coordinates within 서울특별시, 성북구 (Y, X)
        String startResponseContent = mockMvc.perform(post("/conquer/start")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long sessionId = objectMapper.readTree(startResponseContent).get("sessionId").asLong();

        // Simulate conquest duration (at least 2 hours)
        ConquerSession session = conquerSessionRepository.findById(sessionId).orElseThrow();
        session.setStartedAt(Instant.now().minus(Duration.ofHours(3))); // Set startedAt 3 hours ago
        conquerSessionRepository.save(session);

        // When & Then: End the conquer session
        // The coordinates (25, 75) are within '성북구'
        ConquerEndRequest endRequest = new ConquerEndRequest(sessionId, 25.0, 75.0); // Latitude, Longitude (Y, X)

        mockMvc.perform(post("/conquer/end")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Successfully conquered 성북1동!"))
                .andExpect(jsonPath("$.regionName").value("성북1동")) // Expecting the most specific region name
                .andExpect(jsonPath("$.regionDepth1").value("서울특별시"))
                .andExpect(jsonPath("$.regionDepth2").value("성북구"))
                .andExpect(jsonPath("$.regionDepth3").value("성북1동"));
    }
}
