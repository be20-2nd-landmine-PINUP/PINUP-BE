package pinup.backend.ranking.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.ranking.command.service.RankingCommandService;

import java.time.YearMonth;
import java.util.Map;


// restapi 컨트롤러; rest요청 처리, json 형태로 응답 반환
@RestController
//  "/api/rankings/command"로 들어오는 요청을 이 컨트롤러가 처리한다.
@RequestMapping("/api/rankings/command")
// Lombok의 어노테이션으로, final 필드에 대해 생성자를 자동 생성해준다.
// 즉, 의존성 주입을 위해 생성자를 따로 작성하지 않아도 된다.
@RequiredArgsConstructor
public class RankingCommandController {
    //랭킹 관련 명령(생성, 갱신..) 수행하는 서비스 계층 객체
    // 생성자 주입을 통해 자동으로 주입
    private final RankingCommandService commandService;
    // "POST /api/rankings/command/monthly/build" 요청을 처리한다.


    @PostMapping("/monthly/build")
    public ResponseEntity<Map<String, Object>> build(@RequestParam String ym) {
        // 요청 파라미터 검증
        validateYm(ym);

        // 서비스 계층에 월별 랭킹 집계 위임
        commandService.buildMonthlyRanking(YearMonth.parse(ym));

        // 응답 본문(바디)에 간단한 상태 정보 포함
        Map<String, Object> body = Map.of(
                "status", "accepted",
                "ym", ym,
                "message", "월간 랭킹 집계 요청이 정상적으로 처리되었습니다."
        );

        // HTTP 202 + body 반환
        return ResponseEntity.accepted().body(body);
    }



    // YM(연월)값 형식과 범위 검증한느 유틸리티 메서드
    private static void validateYm(String ym) {
        //NULL이거나 "YYYY-MM" 형태가 아닐 경우 예외 발생.
        if (ym == null || !ym.matches("^\\d{4}-\\d{2}$")) {
            throw new IllegalArgumentException("ym 형식은 YYYY-MM 입니다.");
        }
        // 문자열에서 연도와 월을 분리하여 정수로 반환
        int year = Integer.parseInt(ym.substring(0, 4));
        int month = Integer.parseInt(ym.substring(5, 7));
        //연도는 2000-2099년, 월은 1-12월 사이여야 함. 그 외는 예외처리
        if (year < 2000 || year > 2099 || month < 1 || month > 12) {
            throw new IllegalArgumentException("유효하지 않은 ym 값입니다.");
        }
    }
}
