package pinup.backend.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import pinup.backend.point.query.dto.PointLogResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;     // static import 중요
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;  // 콘솔 덤프
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PointQueryControllerTest {

    MockMvc mvc;
    ObjectMapper mapper;
    PointQueryService queryService; // Mockito mock

    @BeforeEach
    void setup() {
        // 1) ObjectMapper(java.time 지원 + ISO 문자열 직렬화)
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 2) JSON 컨버터 & Validator 등록
        var jsonConverter = new MappingJackson2HttpMessageConverter(mapper);
        var validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        // 3) 서비스 mock + 컨트롤러 생성
        queryService = mock(PointQueryService.class);
        var controller = new PointQueryController(queryService);

        // 4) MockMvc 빌드
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(jsonConverter)
                .setValidator(validator)
                .build();
    }

    /* ============ /api/points/{userId} (balance) ============ */
    @Test
    void get_balance_ok_print() throws Exception {
        when(queryService.getBalance(1L)).thenReturn(new PointBalanceResponse(1L, 85));

        MvcResult res = mvc.perform(get("/api/points/{userId}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                // charset 포함 응답도 허용
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.totalPoint").value(85))
                .andReturn();

        MockHttpServletResponse response = res.getResponse();
        System.out.println("✅ 잔액 조회 성공");
        System.out.println("   ├─ userId     : 1");
        System.out.println("   └─ totalPoint : 85 (status=" + response.getStatus() + ")");

        verify(queryService).getBalance(1L);
        verifyNoMoreInteractions(queryService);
    }

    /* ============ /api/points/{userId}/logs (logs) ============ */
    @Test
    void get_logs_ok_print() throws Exception {
        // 고정 시각(안정적 테스트)
        LocalDateTime t1 = LocalDateTime.now().minusDays(1).withNano(0);
        LocalDateTime t2 = LocalDateTime.now().minusDays(2).withNano(0);

        List<PointLogResponse> logs = List.of(
                new PointLogResponse(31L, 1L, 202510L, "CAPTURE", 10, t1),
                new PointLogResponse(30L, 1L, 101L,    "LIKE",     5, t2)
        );
        when(queryService.getLogs(1L, 20, 0)).thenReturn(logs);

        MvcResult res = mvc.perform(get("/api/points/{userId}/logs", 1L)
                        .param("limit", "20")
                        .param("offset", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].logId").value(31))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].sourceType").value("CAPTURE"))
                .andExpect(jsonPath("$[0].pointValue").value(10))
                .andExpect(jsonPath("$[1].sourceType").value("LIKE"))
                .andExpect(jsonPath("$[1].pointValue").value(5))
                .andReturn();

        MockHttpServletResponse response = res.getResponse();
        System.out.println("📋 로그 조회 성공");
        System.out.println("   ├─ rows   : " + logs.size());
        System.out.println("   ├─ first  : " + logs.get(0).sourceType() + " +" + logs.get(0).pointValue());
        System.out.println("   └─ status : " + response.getStatus());

        verify(queryService).getLogs(1L, 20, 0);
        verifyNoMoreInteractions(queryService);
    }
}