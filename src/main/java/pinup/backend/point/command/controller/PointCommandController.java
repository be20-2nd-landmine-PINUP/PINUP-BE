package pinup.backend.point.command.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pinup.backend.point.command.dto.GrantPointRequest;
import pinup.backend.point.command.dto.UsePointRequest;
import pinup.backend.point.command.service.PointCommandService;

import static pinup.backend.point.command.dto.PointSourceTypeDto.*;


@RestController
@RequestMapping("/api/points")
public class PointCommandController {
    //생성자 주입을 통해 PointCommandService를 사용.
    private final PointCommandService service;
    public PointCommandController(PointCommandService service) { this.service = service; }

    // FEED 전용 (좋아요 적립)
    // 클라이언트에서 특정 피드(게시글 등)에 좋아요를 누를 때 호출됨.
    // 사용자의 userId와 좋아요 대상 sourceId를 받아 포인트를 적립한다.
    @PostMapping("/grant/like")
    public ResponseEntity<Void> grantLike(@Valid @RequestBody GrantPointRequest req) {
        service.grantLike(req.userId(), req.sourceId());
        return ResponseEntity.noContent().build();
    }

    // CAPTURE 전용 (점령 적립)api
    // 사용자가 특정 지역이나 지점을 점령할 때 호출됨.
    // 해당 행동에 대해 포인트를 적립한다.
    @PostMapping("/grant/capture")
    public ResponseEntity<Void> grantCapture(@Valid @RequestBody GrantPointRequest req) {
        service.grantCapture(req.userId(), req.sourceId());
        return ResponseEntity.noContent().build();
    }

    // STORE 전용 (차감)
    //사용자가 상점(Store) 등에서 포인트를 사용하거나 교환할 때 호출됨.
    //요청에 포함된 포인트 값(pointValue)을 차감한다.
    @PostMapping("/use")
    public ResponseEntity<Void> use(@Valid @RequestBody UsePointRequest req) {
        service.use(req.userId(), req.pointValue(), req.sourceId());
        return ResponseEntity.noContent().build();
    }
}

/**
 * 포인트 적립 및 사용과 관련된 명령(Command) 요청을 처리하는 컨트롤러.
 *
 * - 주로 사용자 행동(좋아요, 점령, 사용 등)에 따라 포인트를 적립하거나 차감함.
 * - 실제 비즈니스 로직은 PointCommandService가 담당하며,
 *   이 컨트롤러는 HTTP 요청을 받아 서비스 계층으로 전달하는 역할만 수행함.
 */