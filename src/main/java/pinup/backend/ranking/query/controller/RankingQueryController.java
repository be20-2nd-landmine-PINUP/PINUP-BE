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
// REST API 컨트롤러로 지정 (JSON 형태로 응답 반환)
@RestController
@RequestMapping("/api/rankings/query")
@RequiredArgsConstructor
public class RankingQueryController {
    // 랭킹 조회 관련 비즈니스 로직을 처리하는 서비스
    private final RankingQueryService queryService;

    @GetMapping("/monthly/top100")
    public ResponseEntity<List<TopRankResponse>> getTop100(@RequestParam String ym) {
        validateYm(ym); // YYYY-MM 형식 검증
        // 서비스에서 랭킹 상위 100명 데이터 조회(서비스 호출)
        List<TopRankResponse> body = queryService.getTop100(ym);
        //  캐시 제어 헤더 설정 (브라우저/프록시 캐시 허용)
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "public, max-age=60"); // 클라이언트나 cdn 응답을 최대 60초간 캐시 가능
        // 응답 바디 + 헤더 + 상태코드(200 OK) 함께 반환; (Top100 사용자 정보 DTO 리스트)
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    // 내 랭킹 조회api
    @GetMapping("/monthly/me")
    @PreAuthorize("isAuthenticated()") // 로그인된 사용자만 접근 가능
    public ResponseEntity<MyRankResponse> getMyRank(@RequestParam String ym,
                                                    @AuthenticationPrincipal Principal principal) {
        // @AuthenticationPrincipal Principal principal
        //→ 현재 인증된 사용자 정보(Spring Security에서 주입
        validateYm(ym);
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long userId = resolveUserId(principal);
        // principal(로그인 정보)에서 userId를 추출하는 부분 (직접 구현 필요)
        if (userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        MyRankResponse dto = queryService.getMyRank(ym, userId); // 서비스 호출; db에서 해당 유저의 월간 랭킹 1건 조회

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate");
        // 캐시 제어 헤더: "no-store, no-cache, must-revalidate"
        //→ 내 정보는 개인 데이터이므로 브라우저에 캐시 금지
        // 반환값: MyRankResponse (내 순위 DTO)
        return new ResponseEntity<>(dto, headers, HttpStatus.OK);
    }
    // 반환값; 연월 검증
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
    // 사용자 식별자 추출;
    private Long resolveUserId(Principal principal) {
        // 예: principal.getName()을 통해 이메일 → userId 매핑
        return null; // 팀 규칙에 맞게 구현.. 언젠가
    }
}
