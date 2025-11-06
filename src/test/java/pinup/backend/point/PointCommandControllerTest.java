package pinup.backend.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import pinup.backend.point.command.controller.PointCommandController;
import pinup.backend.point.command.service.PointCommandService;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;   // ✅ 중요: static import
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PointCommandControllerTest {

    MockMvc mvc;
    ObjectMapper mapper;
    PointCommandService commandService; // Mockito mock

    @BeforeEach
    void setup() {
        // 1) ObjectMapper (java.time 지원)
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // 2) JSON 메시지 컨버터 & Validator (Standalone에선 수동 등록 필요)
        var jsonConverter = new MappingJackson2HttpMessageConverter(mapper);
        var validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        // 3) 서비스 mock + 컨트롤러 생성
        commandService = mock(PointCommandService.class);
        var controller = new PointCommandController(commandService);

        // 4) IllegalArgumentException → 400으로 매핑하는 테스트용 Advice (옵션)
        var advice = new TestAdvice();

        // 5) MockMvc 빌드
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(jsonConverter)
                .setValidator(validator)
                .setControllerAdvice(advice)
                .build();
    }

    /* ============ /api/points/grant/like ============ */

    @Test
    void grant_like_ok() throws Exception {
        var payload = Map.of(
                "userId", 1L,
                "sourceId", 101L
        );

        MvcResult res = mvc.perform(post("/api/points/grant/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        MockHttpServletResponse response = res.getResponse();
        System.out.println("✅ 포인트 부여 성공 (LIKE)");
        System.out.println("   ├─ userId   : " + payload.get("userId"));
        System.out.println("   ├─ sourceId : " + payload.get("sourceId"));
        System.out.println("   └─ status   : " + response.getStatus());

        verify(commandService).grantLike(1L, 101L);
        verifyNoMoreInteractions(commandService);
    }

    /* ============ /api/points/grant/capture ============ */

    @Test
    void grant_capture_ok() throws Exception {
        var payload = Map.of(
                "userId", 2L,
                "sourceId", 999L  // territoryId
        );

        MvcResult res = mvc.perform(post("/api/points/grant/capture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        MockHttpServletResponse response = res.getResponse();
        System.out.println("✅ 포인트 부여 성공 (CAPTURE)");
        System.out.println("   ├─ userId      : " + payload.get("userId"));
        System.out.println("   ├─ territoryId : " + payload.get("sourceId"));
        System.out.println("   └─ status      : " + response.getStatus());

        verify(commandService).grantCapture(2L, 999L);
        verifyNoMoreInteractions(commandService);
    }

    /* ============ /api/points/use ============ */

    @Test
    void use_ok() throws Exception {
        var payload = Map.of(
                "userId", 1L,
                "pointValue", 1500,
                "sourceId", 20231101L
        );

        MvcResult result = mvc.perform(post("/api/points/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.printf(" [%s] 포인트 차감 성공: userId=%s, point=%s%n",
                result.getResponse().getStatus(),
                payload.get("userId"),
                payload.get("pointValue"));

        verify(commandService).use(1L, 1500, 20231101L);
        verifyNoMoreInteractions(commandService);
    }

    // ===== 테스트 전용 예외 매핑 (IllegalArgumentException -> 400) =====
    @org.springframework.web.bind.annotation.RestControllerAdvice
    static class TestAdvice {
        @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
        public org.springframework.http.ResponseEntity<String> badReq(IllegalArgumentException e) {
            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
