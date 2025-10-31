package pinup.backend.ranking.common.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException e) {
        Map<String, Object> body = Map.of(
                "error", "BAD_REQUEST",
                "message", e.getMessage()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception e) {
        Map<String, Object> body = Map.of(
                "error", "INTERNAL_SERVER_ERROR",
                "message", e.getMessage()
        );
        return ResponseEntity.internalServerError().body(body);
    }
}
