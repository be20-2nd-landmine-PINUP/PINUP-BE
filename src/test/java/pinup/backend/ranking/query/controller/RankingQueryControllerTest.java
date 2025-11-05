package pinup.backend.ranking.query.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pinup.backend.ranking.query.dto.TopRankResponse;
import pinup.backend.ranking.query.service.RankingQueryService;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RankingQueryControllerTest {

@Test
void top100_returns_100_items_and_cache_header() throws Exception {
    // 1) 목 서비스: 100명의 가짜 TopRankResponse 리스트 리턴
    RankingQueryService mockSvc = Mockito.mock(RankingQueryService.class);
    List<TopRankResponse> fakeTop100 =
            IntStream.rangeClosed(1, 100)
                    .mapToObj(i -> TopRankResponse.builder()
                            .rank(i)                // 단순 1..100
                            .userId((long) i)
                            .userName("User" + i)
                            .completedCount(1)      // 모두 1개로 통일
                            .build())
                    .toList();
    Mockito.when(mockSvc.getTop100("2025-10")).thenReturn(fakeTop100);

    // 2) 컨트롤러에 목 서비스 주입 후 standalone MockMvc 구성
    RankingQueryController controller = new RankingQueryController(mockSvc);
    MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

    // 3) 호출 및 검증
    mvc.perform(get("/api/rankings/query/monthly/top100")
                    .param("ym", "2025-10")
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "public, max-age=60"))
            .andExpect(jsonPath("$.length()").value(100))
            .andExpect(jsonPath("$[0].rank").value(1))
            .andExpect(jsonPath("$[0].userName").value("User1"))
            .andExpect(jsonPath("$[99].rank").value(100));

    // 4) 서비스가 정확히 호출됐는지 확인
    Mockito.verify(mockSvc).getTop100(eq("2025-10"));
}

@Test
void top100_returns_empty_when_service_has_no_data() throws Exception {
    RankingQueryService mockSvc = Mockito.mock(RankingQueryService.class);
    Mockito.when(mockSvc.getTop100("2025-10")).thenReturn(List.of());

    RankingQueryController controller = new RankingQueryController(mockSvc);
    MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

    mvc.perform(get("/api/rankings/query/monthly/top100")
                    .param("ym", "2025-10")
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "public, max-age=60"))
            .andExpect(jsonPath("$.length()").value(0));

    Mockito.verify(mockSvc).getTop100(eq("2025-10"));
}
}
