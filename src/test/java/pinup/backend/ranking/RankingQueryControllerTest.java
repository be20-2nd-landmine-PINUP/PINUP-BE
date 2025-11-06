package pinup.backend.ranking;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pinup.backend.ranking.query.controller.RankingQueryController;
import pinup.backend.ranking.query.dto.TopRankResponse;
import pinup.backend.ranking.query.service.RankingQueryService;

import java.util.stream.IntStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

class RankingQueryControllerTest  {

    @Test
    void top100_returns_list_and_header() throws Exception {
        RankingQueryService mockSvc = Mockito.mock(RankingQueryService.class);

        // 가짜 100명 생성
        List<TopRankResponse> fakeTop100 =
                IntStream.rangeClosed(1, 100)
                        .mapToObj(i -> TopRankResponse.builder()
                                .rank(i)
                                .userId((long) i)
                                .userName("User" + i)
                                .completedCount(1)
                                .build())
                        .toList();

        Mockito.when(mockSvc.getTop100("2025-10")).thenReturn(fakeTop100);

        RankingQueryController controller = new RankingQueryController(mockSvc);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller)
                // (옵션) 잘못된 ym 등 예외를 400으로 매핑하려면 전역 예외 핸들러 추가
                //.setControllerAdvice(new ApiExceptionHandler())
                .build();

        mvc.perform(get("/api/rankings/query/monthly/top100")
                        .param("ym", "2025-10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "public, max-age=60"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // ✅ 배열 길이는 hasSize로 검증
                .andExpect(jsonPath("$", hasSize(100)))
                .andExpect(jsonPath("$[0].userName").value("User1"));

        Mockito.verify(mockSvc).getTop100(eq("2025-10"));
    }
}
