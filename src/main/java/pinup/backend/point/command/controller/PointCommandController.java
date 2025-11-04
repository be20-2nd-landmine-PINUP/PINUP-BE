package pinup.backend.point.command.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pinup.backend.point.command.dto.GrantPointRequest;
import pinup.backend.point.command.dto.UsePointRequest;
import pinup.backend.point.command.service.PointCommandService;
@RestController
@RequestMapping("/api/points")
public class PointCommandController {

    private final PointCommandService service;

    public PointCommandController(PointCommandService service) {
        this.service = service;
    }

    // 적립 (피드/영토 등), requestbody로 json에서 DTO로 자동 매핑, 혹은 그 역 순으로.
    // /api/points/grant → FEED_ROLE 또는 CAPTURE_ROLE만 허용
    @PreAuthorize("hasAnyRole('FEED', 'CAPTURE')")
    @PostMapping("/grant")
    public ResponseEntity<Void> grant(@Valid @RequestBody GrantPointRequest req) {
        switch (req.sourceType()) {
            case "LIKE" -> service.grantLike(req.userId(), req.sourceId());
            case "CAPTURE" -> service.grantCapture(req.userId(), req.sourceId());
            default -> throw new IllegalArgumentException("지원하지 않는 sourceType: " + req.sourceType());
        }
        return ResponseEntity.noContent().build();
    }

    // 차감 (스토어)
    // /api/points/use → STORE_ROLE만 허용
    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/use")
    public ResponseEntity<Void> use(@Valid @RequestBody UsePointRequest req) {
        service.use(req.userId(), req.pointValue(), req.sourceId());
        return ResponseEntity.noContent().build();
    }
}
/*
-엔드 포인트 접근 제어
hasRole('STORE')는 실제 권한 문자열이 ROLE_STORE 여야 통과
 */
