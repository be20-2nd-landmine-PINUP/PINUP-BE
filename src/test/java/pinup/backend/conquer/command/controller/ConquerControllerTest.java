package pinup.backend.conquer.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pinup.backend.conquer.command.application.dto.ConquerRequest;
import pinup.backend.conquer.command.application.dto.ConquerResponse;
import pinup.backend.conquer.command.application.service.ConquerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConquerController.class)
class ConquerControllerTest {

    // By defining a @TestConfiguration, we can manually provide beans to the application context.
    // Here, we are providing a mock of ConquerService.
    @TestConfiguration
    static class ConquerControllerTestConfiguration {
        @Bean
        public ConquerService conquerService() {
            return Mockito.mock(ConquerService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Spring will inject the mock bean we defined in the configuration above.
    @Autowired
    private ConquerService conquerService;

    @Test
    @WithMockUser // Simulates an authenticated user
    void conquerRegion_Success() throws Exception {
        // Given
        ConquerRequest request = new ConquerRequest(37.5665, 126.9780);
        ConquerResponse mockResponse = ConquerResponse.builder()
                .message("Successfully conquered 서울특별시!")
                .regionName("서울특별시")
                .regionDepth1("서울특별시")
                .build();

        // Mock the service call
        when(conquerService.conquerRegion(any(), any(ConquerRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/conquer")
                        .with(csrf()) // Include CSRF token for POST requests if Spring Security is configured
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully conquered 서울특별시!"))
                .andExpect(jsonPath("$.regionName").value("서울특별시"));
    }

    @Test
    @WithMockUser
    void conquerRegion_AlreadyConquered() throws Exception {
        // Given
        ConquerRequest request = new ConquerRequest(37.5665, 126.9780);
        ConquerResponse mockResponse = ConquerResponse.builder()
                .message("You have already conquered this region.")
                .regionName("서울특별시")
                .build();

        when(conquerService.conquerRegion(any(), any(ConquerRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/conquer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("You have already conquered this region."));
    }

    @Test
    @WithMockUser
    void conquerRegion_NotFound() throws Exception {
        // Given
        ConquerRequest request = new ConquerRequest(0.0, 0.0);
        ConquerResponse mockResponse = ConquerResponse.builder()
                .message("No region found at the given coordinates.")
                .build();

        when(conquerService.conquerRegion(any(), any(ConquerRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/conquer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No region found at the given coordinates."));
    }
}
