package pinup.backend.ranking.query.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pinup.backend.ranking.query.dto.TopRankResponse;
import pinup.backend.ranking.query.service.RankingQueryService;

import java.util.stream.IntStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RankingQueryControllerTest  {

    @Test
    void top100_returns_list_and_header() throws Exception {
        RankingQueryService mockSvc = Mockito.mock(RankingQueryService.class);

        // 가짜 100명 생성
        List<TopRankResponse> fakeTop100 =
                IntStream.rangeClosed(1, 100)
                        .mapToObj(i -> TopRankResponse.builder()
                                .rank(i) // 전부 서로 다른 랭크로 세팅
                                .userId((long) i)
                                .userName("User"+i)
                                .completedCount(1)
                                .build())
                        .toList();

        Mockito.when(mockSvc.getTop100("2025-10")).thenReturn(fakeTop100);

        RankingQueryController controller = new RankingQueryController(mockSvc);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

        mvc.perform(get("/api/rankings/query/monthly/top100")
                        .param("ym", "2025-10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "public, max-age=60"))
                .andExpect(jsonPath("$.length()").value(100))
                .andExpect(jsonPath("$[0].userName").value("User1"));

        Mockito.verify(mockSvc).getTop100(eq("2025-10"));
    }
}
