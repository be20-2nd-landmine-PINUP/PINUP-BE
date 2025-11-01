package pinup.backend.point.query.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.point.query.dto.PointBalanceResponse;
import pinup.backend.point.query.dto.PointLogResponse;
import pinup.backend.point.query.service.PointQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointQueryController {

    private final PointQueryService service;

    public PointQueryController(PointQueryService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PointBalanceResponse> balance(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getBalance(userId));
    }

    // 포인트 로그 조회
    @GetMapping("/{userId}/logs")
    public ResponseEntity<List<PointLogResponse>> logs(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = "20") int limit,
                                                       @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(service.getLogs(userId, limit, offset));
    }
}
