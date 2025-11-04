package pinup.backend.conquer.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pinup.backend.conquer.command.application.dto.ConquerEndRequest;
import pinup.backend.conquer.command.application.dto.ConquerEndResponse;
import pinup.backend.conquer.command.application.dto.ConquerStartRequest;
import pinup.backend.conquer.command.application.dto.ConquerStartResponse;
import pinup.backend.conquer.command.application.service.ConquerSessionService;
import pinup.backend.conquer.command.domain.entity.Region;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConquerController.class)
class ConquerControllerTest {

    @TestConfiguration
    static class ConquerControllerTestConfiguration {
        @Bean
        public ConquerSessionService conquerSessionService() {
            return Mockito.mock(ConquerSessionService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConquerSessionService conquerSessionService;

    @Test
    @WithMockUser
    @DisplayName("정복 시작 API 호출 테스트")
    void startConquering() throws Exception {
        // Given
        ConquerStartRequest request = new ConquerStartRequest(37.5665, 126.9780);
        ConquerStartResponse mockResponse = new ConquerStartResponse(1L);

        when(conquerSessionService.startConquering(any(), any(ConquerStartRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/conquer/start")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("정복 종료 API - 성공")
    void endConquering_Success() throws Exception {
        // Given
        ConquerEndRequest request = new ConquerEndRequest(1L, 37.5665, 126.9780);
        Region mockRegion = Mockito.mock(Region.class);
        when(mockRegion.getRegionName()).thenReturn("서울특별시");
        when(mockRegion.getRegionDepth1()).thenReturn("서울특별시");
        when(mockRegion.getRegionDepth2()).thenReturn(null);
        when(mockRegion.getRegionDepth3()).thenReturn(null);

        ConquerEndResponse mockResponse = ConquerEndResponse.of("SUCCESS", "Successfully conquered 서울특별시!", mockRegion);

        when(conquerSessionService.endConquering(any(), any(ConquerEndRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/conquer/end")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Successfully conquered 서울특별시!"))
                .andExpect(jsonPath("$.regionName").exists()); // Check that region details are in the response
    }

    @Test
    @WithMockUser
    @DisplayName("정복 종료 API - 실패 (시간 미달)")
    void endConquering_Failure_Time() throws Exception {
        // Given
        ConquerEndRequest request = new ConquerEndRequest(1L, 37.5665, 126.9780);
        ConquerEndResponse mockResponse = ConquerEndResponse.of("FAILED", "The conquest requires at least 2 hours.");

        when(conquerSessionService.endConquering(any(), any(ConquerEndRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/conquer/end")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.message").value("The conquest requires at least 2 hours."));
    }

    @Test
    @WithMockUser
    @DisplayName("정복 종료 API - 실패 (지역 불일치)")
    void endConquering_Failure_Region() throws Exception {
        // Given
        ConquerEndRequest request = new ConquerEndRequest(1L, 37.1234, 127.5678);
        ConquerEndResponse mockResponse = ConquerEndResponse.of("FAILED", "You are not in the same region where you started.");

        when(conquerSessionService.endConquering(any(), any(ConquerEndRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/conquer/end")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.message").value("You are not in the same region where you started."));
    }
}