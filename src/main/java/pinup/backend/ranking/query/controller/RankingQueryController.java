package pinup.backend.ranking.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pinup.backend.ranking.query.dto.MyRankResponse;
import pinup.backend.ranking.query.dto.TopRankResponse;
import pinup.backend.ranking.query.service.RankingQueryService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/rankings/query")
@RequiredArgsConstructor
public class RankingQueryController {

    private final RankingQueryService queryService;

    @GetMapping("/monthly/top100")
    public ResponseEntity<List<TopRankResponse>> getTop100(@RequestParam String ym) {
        validateYm(ym);
        List<TopRankResponse> body = queryService.getTop100(ym);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "public, max-age=60");

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @GetMapping("/monthly/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MyRankResponse> getMyRank(@RequestParam String ym,
                                                    @AuthenticationPrincipal Principal principal) {
        validateYm(ym);
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long userId = resolveUserId(principal);
        if (userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        MyRankResponse dto = queryService.getMyRank(ym, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate");

        return new ResponseEntity<>(dto, headers, HttpStatus.OK);
    }

    private static void validateYm(String ym) {
        if (ym == null || !ym.matches("^\\d{4}-\\d{2}$")) {
            throw new IllegalArgumentException("ym 형식은 YYYY-MM 입니다.");
        }
        int year = Integer.parseInt(ym.substring(0, 4));
        int month = Integer.parseInt(ym.substring(5, 7));
        if (year < 2000 || year > 2099 || month < 1 || month > 12) {
            throw new IllegalArgumentException("유효하지 않은 ym 값입니다.");
        }
    }

    private Long resolveUserId(Principal principal) {
        // 예: principal.getName()을 통해 이메일 → userId 매핑
        return null; // 팀 규칙에 맞게 구현
    }
}
