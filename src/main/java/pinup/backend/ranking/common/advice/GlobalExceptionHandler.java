package pinup.backend.ranking.common.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
// 전역 예외 처리용 컨트롤러 어드바이스
// 애플리케이션 전역에서 발생하는 예외를 가로채 json 형태로 응답
@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
    컨트롤러에서 IllegalArgumentException이 던져지면 이 메서드가 실행됩니다.
    (예: validateYm() 메서드에서 형식이 잘못됐을 때)
    응답 바디;
    {
    "error": "BAD_REQUEST",
    "message": "ym 형식은 YYYY-MM 입니다."
    }
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException e) {
        Map<String, Object> body = Map.of(
                "error", "BAD_REQUEST",
                "message", e.getMessage()
        );
        return ResponseEntity.badRequest().body(body);
    }
    /*일반 처리; 모든 Exception (상위 예외 클래스)을 포괄 처리합니다.
    // 응답 HTTP 상태 코드: 500 Internal Server Error
    {
     "error": "INTERNAL_SERVER_ERROR",
     "message": "이미 해당 월 집계가 실행 중입니다."
    }
    */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception e) {
        Map<String, Object> body = Map.of(
                "error", "INTERNAL_SERVER_ERROR",
                "message", e.getMessage()
        );
        return ResponseEntity.internalServerError().body(body);
    }
}
// 목적; API 응답의 일관성 유지
